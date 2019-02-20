package hhu.propra2.javageddon.teils.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.*;
import javax.validation.Valid;

import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.services.ArtikelService;
import hhu.propra2.javageddon.teils.services.BenutzerService;
import hhu.propra2.javageddon.teils.services.ReservierungService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;

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

        m.addAttribute("existingEmailError", alleBenutzer.isDuplicateEmail(benutzer));
        m.addAttribute("existingNameError", alleBenutzer.isDuplicateName(benutzer));
            
        m.addAttribute("nameRequired", alleBenutzer.isEmptyName(benutzer));
        m.addAttribute("emailRequired", alleBenutzer.isEmptyEmail(benutzer));
        
        m.addAttribute("benutzer",benutzer);

        if(alleBenutzer.hasIncorrectInput(benutzer)) {
        	return "benutzer_registrieren";
        }
        else {
            alleBenutzer.addBenutzer(benutzer);
            return "redirect:benutzer?=" + benutzer.getId();
        }
    }
    //Still need to addAttributes for Reservierungen
    @RequestMapping(value = "/benutzer", method = GET)
    public String benutzerProfil( Model m, @RequestParam("id") long id) {
    	m.addAttribute("benutzer", alleBenutzer.findBenutzerById(id));
    	m.addAttribute("alleArtikel", alleArtikel.findArtikelByEigentuemer(alleBenutzer.findBenutzerById(id)));
    	m.addAttribute("alleReservierungen", alleReservierungen.findReservierungByLeihender(alleBenutzer.findBenutzerById(id)));
    	return "profil_ansicht";
    }
}