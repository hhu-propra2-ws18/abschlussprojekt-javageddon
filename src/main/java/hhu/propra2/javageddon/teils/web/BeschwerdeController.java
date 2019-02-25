package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.dataaccess.BeschwerdeRepository;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Beschwerde;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
public class BeschwerdeController {

    @Autowired
    private BeschwerdeRepository alleBeschwerden;

    @Autowired
    private BenutzerRepository alleBenutzer;


    @GetMapping("/admin_clearing")
    public String adminClearing(Model m) {
        m.addAttribute("alleBeschwerden", alleBeschwerden.getAllByBearbeitetFalse());
        return "admin_clearing";
    }


    @RequestMapping(value = "/clearing/{id}/{uid}", method = GET)
    public String updateBeschwerde(Model m, @PathVariable("id") long id, @PathVariable("uid") long uid){
        m.addAttribute("alleBeschwerden", alleBeschwerden.getAllByBearbeitetFalse());
        Benutzer benutzer = alleBenutzer.findById(uid);
        Beschwerde beschwerde = alleBeschwerden.findById(id).get();
        beschwerde.setHatRecht(benutzer);
        beschwerde.setBearbeitet(true);
        alleBeschwerden.save(beschwerde);
        return "redirect:/admin_clearing";
    }
}
