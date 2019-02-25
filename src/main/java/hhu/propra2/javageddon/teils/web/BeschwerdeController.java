package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.dataaccess.BeschwerdeRepository;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Beschwerde;
import hhu.propra2.javageddon.teils.services.BenutzerService;
import hhu.propra2.javageddon.teils.services.BeschwerdeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
public class BeschwerdeController {

    @Autowired
    private BeschwerdeService alleBeschwerden;

    @Autowired
    private BenutzerService alleBenutzer;


    @GetMapping("/admin_clearing")
    public String adminClearing(Model m) {
        m.addAttribute("alleBeschwerden", alleBeschwerden.findAllUnbearbeitetBeschwerden());
        return "admin_clearing";
    }


    @RequestMapping(value = "/clearing/{id}/{uid}", method = GET)
    public String updateBeschwerde(Model m, @PathVariable("id") long id, @PathVariable("uid") long uid){
        m.addAttribute("alleBeschwerden", alleBeschwerden.findAllUnbearbeitetBeschwerden());
        Benutzer benutzer = alleBenutzer.findBenutzerById(uid);
        Beschwerde beschwerde = alleBeschwerden.findBeschwerdeById(id);
        beschwerde.setHatRecht(benutzer);
        beschwerde.setBearbeitet(true);
        alleBeschwerden.addBeschwerde(beschwerde);
        return "redirect:/admin_clearing";
    }
}
