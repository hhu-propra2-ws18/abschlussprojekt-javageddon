package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ArtikelController {


    @Autowired
    private ArtikelRepository alleArtikel;

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
/*
    @GetMapping("/artikel/{id}")
    public String detailAnsicht(Model m, @PathVariable Long id) {

        m.addAttribute("artikel", alleArtikel.findById(id));

        return "artikel_details";
    }
*/
    @RequestMapping(value = "/details", method = GET)
    @ResponseBody
    public String getDetailsByArtikelId( Model m, @RequestParam("id") long id) {
        m.addAttribute("artikel", alleArtikel.findById(id));
        return "artikel_details";
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