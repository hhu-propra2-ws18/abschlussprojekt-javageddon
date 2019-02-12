package hhu.propra2.javageddon.teils.web;

import java.util.List;
import java.util.Optional;

import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ArtikelController {

    @Value("${baseurl}")
    private String baseurl;

    @Autowired
    private ArtikelRepository alleArtikel;

    @GetMapping("/")
    public String artikelListe(Model m){
        m.addAttribute("baseurl", baseurl);
        m.addAttribute("alleArtikel", alleArtikel.findByAktiv(true));
        return "start";
    }
/*
    @GetMapping("/edit/{id}")
    public String editPerson(Model m, @PathVariable("id") int id) {
        Person p = personen.getPerson(id);
        m.addAttribute("baseurl", baseurl);
        m.addAttribute("person", p);
        return "edit";
    }

    @PostMapping("/edit")
    public String changePerson(Model m, Person p, String skillList) {
        personen.merge(p, skillList);
        return "redirect:" + baseurl;
    }

    @GetMapping("/add")
    public String addPerson() {
        return "redirect:" + baseurl + "/edit/" + personen.newPerson().getId();
    } */
}