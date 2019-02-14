package hhu.propra2.javageddon.teils.web;

import java.util.*;

import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
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
    public ModelAndView benutzerSubmit(@ModelAttribute Benutzer benutzer){
        alleBenutzer.save(benutzer);
        return new ModelAndView("redirect:benutzer/?" + benutzer.getId());
    }
}