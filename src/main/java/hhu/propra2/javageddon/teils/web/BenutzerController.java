package hhu.propra2.javageddon.teils.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BenutzerController {

    @Autowired
    private BenutzerRepository alleBenutzer;

    @GetMapping("/registrieren")
    public String neuerBenutzer(Model m){
        m.addAttribute("benutzer", new Benutzer());
        return "benutzer_registrieren";
    }

    @PostMapping("/registrieren")
    public ModeAndView benutzerSubmit(@ModelAttribute Benutzer benutzer){
        benutzer.setMeineArtikel(new List<Artikel>());
        alleBenutzer.save(benutzer);
        return new ModelAndView("redirect:benutzer/?" + benutzer.getId());
    }
}