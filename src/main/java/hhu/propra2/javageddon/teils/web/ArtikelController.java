package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Adresse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ArtikelController {


    @Autowired
    private ArtikelRepository alleArtikel;

    @Autowired
    private BenutzerRepository alleBenutzer;

    @GetMapping("/")
    public String artikelListe(Model m){
        m.addAttribute("alleArtikel", alleArtikel.findByAktiv(true));
        return "start";
    }

    /*
    Diese Methode greift auf das Dateisystem des Dockercontainers zu und liefert das angefragte Bild aus.
    */
    @ResponseBody
    @RequestMapping(value = "/fotos/{id}", method = GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource getImageAsResource(@PathVariable("id") String id) {
        return new FileSystemResource("fotos/" + id + ".jpg");
    }

    @RequestMapping(value = "/details", method = GET)
    public String getDetailsByArtikelId( Model m, @RequestParam("id") long id) {
        Artikel artikel = alleArtikel.findById(id);
        Benutzer eigentuemer = artikel.getEigentuemer();

        m.addAttribute("artikel", artikel);
        m.addAttribute("eigentuemer", eigentuemer);

        return "artikel_details";
    }

    @GetMapping("/artikel_erstellen")
    public String artikelErstellen(Model m){
        m.addAttribute("artikel", new Artikel());
        m.addAttribute("standort", new Adresse());
        return "artikel_erstellen";
    }

    @PostMapping("/artikel_erstellen")
    public String erstelleArtikel(@ModelAttribute Artikel artikel, @ModelAttribute Adresse adresse){
        artikel.setStandort(adresse);
        artikel.setFotos(new ArrayList<String>());
        artikel.setEigentuemer(alleBenutzer.findById(1)); // TODO EINGELOGGTER USER
        alleArtikel.save(artikel);
        return "redirect:/fotoupload/" + artikel.getId();
    }

/*
    @GetMapping("/edit/{id}")
    public String editPerson(Model m, @PathVariable("id") int id) {
        Person p = personen.getPerson(id);
        m.addAttribute("person", p);
        return "edit";
    }

    @PostMapping("/edit")
    public String changePerson(Model m, Person p, String skillList) {
        personen.merge(p, skillList);
        return "redirect:"
    }

    @GetMapping("/add")
    public String addPerson() {
        return "redirect:" + "/edit/" + personen.newPerson().getId();
    } */
}