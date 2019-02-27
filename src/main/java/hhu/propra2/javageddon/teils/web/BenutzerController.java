package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.services.ArtikelService;
import hhu.propra2.javageddon.teils.services.BenutzerService;
import hhu.propra2.javageddon.teils.services.ReservierungService;
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
    
    @Autowired ReservierungService alleReservierungen;


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
        m.addAttribute("benutzer", alleBenutzer.findBenutzerById(id));
        m.addAttribute("alleArtikel", alleArtikel.findArtikelByEigentuemer(alleBenutzer.findBenutzerById(id)));
        m.addAttribute("alleReservierungen", alleReservierungen.findReservierungByLeihenderAndSichtbar(alleBenutzer.findBenutzerById(id),true));
        m.addAttribute("alleAnfragen", alleReservierungen.findReservierungByArtikelEigentuemerAndNichtBearbeitet(alleBenutzer.findBenutzerById(id)));
        m.addAttribute("aktuelleAusleihen", alleReservierungen.findReservierungByArtikelEigentuemerAndNichtAbgeschlossen(alleBenutzer.findBenutzerById(id)));
        m.addAttribute("alleWarnungen", alleReservierungen.fristAbgelaufeneReservierungen(alleBenutzer.findBenutzerById(id)));
        return "profil_ansicht";
    }
}