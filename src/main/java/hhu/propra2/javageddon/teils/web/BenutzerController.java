package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.dataaccess.ProPay;
import hhu.propra2.javageddon.teils.model.Aufladung;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.ProPayUser;
import hhu.propra2.javageddon.teils.model.Transaktion;
import hhu.propra2.javageddon.teils.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class BenutzerController {

    @Autowired
    private BenutzerService alleBenutzer;
    
    @Autowired
    private ArtikelService alleArtikel;
    
    @Autowired
    private ReservierungService alleReservierungen;

    @Autowired
    private TransaktionService alleTransaktionen;


    @GetMapping("/registrieren")
    public String neuerBenutzer(Model m){
        m.addAttribute("benutzer", new Benutzer());
        return "benutzer_registrieren";
    }

    @PostMapping("/registrieren")
    public String benutzerSubmit(@ModelAttribute Benutzer benutzer, Model m){

        System.out.println("Enters");
        m.addAttribute("existingEmailError", alleBenutzer.isDuplicateEmail(benutzer));
        m.addAttribute("existingNameError", alleBenutzer.isDuplicateName(benutzer));
        System.out.println("Exists");
        m.addAttribute("nameRequired", alleBenutzer.isEmptyName(benutzer));
        m.addAttribute("emailRequired", alleBenutzer.isEmptyEmail(benutzer));
        System.out.println("required");
        m.addAttribute("passwordRequired", alleBenutzer.isEmptyPassword(benutzer));
        System.out.println("password");
        m.addAttribute("benutzer",benutzer);
        System.out.println("benutzer");
        if(alleBenutzer.hasIncorrectInput(benutzer)) {
        	return "benutzer_registrieren";
        }else {
            alleBenutzer.addBenutzer(benutzer);
            //return "redirect:benutzer?=" + benutzer.getId();
            return "redirect:profil_ansicht";
        }
    }

    @GetMapping("/benutzer_anmelden")
    public String login(){
        return "benutzer_anmelden";
    }
    

    @GetMapping("/profil_ansicht")
    public String benutzerAnsicht(Model m){
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)currentUser).getUsername();
        Long id = alleBenutzer.getIdByName(username);

        m.addAttribute("proPayUser",ProPay.getProPayUser(username));
        m.addAttribute("benutzer", alleBenutzer.findBenutzerById(id));
        m.addAttribute("alleArtikel", alleArtikel.findArtikelByEigentuemer(alleBenutzer.findBenutzerById(id)));
        m.addAttribute("alleReservierungen", alleReservierungen.findReservierungByLeihender(alleBenutzer.findBenutzerById(id)));
        m.addAttribute("alleAnfragen", alleReservierungen.findReservierungByArtikelEigentuemerAndNichtBearbeitet(alleBenutzer.findBenutzerById(id)));
        return "profil_ansicht";
    }

    @GetMapping("/proPay_details")
    public String proPaySicht(Model m){
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)currentUser).getUsername();
        Aufladung aufladung = new Aufladung();

        m.addAttribute("aufladung", aufladung);
        m.addAttribute("proPayUser",ProPay.getProPayUser(username));
        m.addAttribute("alleTransaktionen",alleTransaktionen);
        return "proPay_details";
    }

    @PostMapping("/proPay_Aufladen")
    public String proPayAufladen(@ModelAttribute Aufladung aufladung){
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)currentUser).getUsername();
        Long id = alleBenutzer.getIdByName(username);

        ProPayUser proPayUser = ProPay.getProPayUser(username);
        aufladung.setProPayUser(proPayUser);
        ProPay.heresTheMoney(aufladung);

        //Neue Transaktion für diese Aufladung erzeugen
        Transaktion transaktion = new Transaktion();
        transaktion.setBetrag((int) aufladung.getBetrag());
        transaktion.setKontoinhaber(alleBenutzer.findBenutzerById(id));
        transaktion.setVerwendungszweck("Aufladung über Teils!");

        return "redirect:proPay_details";

    }
}