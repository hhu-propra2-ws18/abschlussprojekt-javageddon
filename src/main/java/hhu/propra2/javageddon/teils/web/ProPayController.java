package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.dataaccess.ProPay;
import hhu.propra2.javageddon.teils.model.Aufladung;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.ProPayUser;
import hhu.propra2.javageddon.teils.model.Transaktion;
import hhu.propra2.javageddon.teils.services.ArtikelService;
import hhu.propra2.javageddon.teils.services.BenutzerService;
import hhu.propra2.javageddon.teils.services.ReservierungService;
import hhu.propra2.javageddon.teils.services.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProPayController {

    @Autowired
    private BenutzerService alleBenutzer;

    @Autowired
    private TransaktionService alleTransaktionen;

    @GetMapping("/proPay_details")
    public String proPaySicht(Model m){
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)currentUser).getUsername();
        Benutzer benutzer = alleBenutzer.findBenutzerById(alleBenutzer.getIdByName(username));
        Aufladung aufladung = new Aufladung();

        m.addAttribute("aufladung", aufladung);
        m.addAttribute("proPayUser", ProPay.getProPayUser(username));
        m.addAttribute("alleTransaktionen",alleTransaktionen.findTransaktionenByKontoinhaber(benutzer));
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

        alleTransaktionen.addTransaktion(transaktion);

        return "redirect:proPay_details";

    }
}
