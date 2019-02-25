package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.dataaccess.BeschwerdeRepository;
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


    @GetMapping("/admin_clearing")
    public String adminClearing(Model m) {
        m.addAttribute("alleBeschwerden", alleBeschwerden.findAll());
        return "admin_clearing";
    }


    @RequestMapping(value = "/clearing", method = GET)
    public String updateBeschwerde(Model m, @RequestParam("id") long id) {
        m.addAttribute("alleBeschwerden", alleBeschwerden.findAll());
        Beschwerde beschwerde = alleBeschwerden.findById(id).get();
        beschwerde.setBearbeitet(true);
        alleBeschwerden.save(beschwerde);
        return "admin_clearing";
    }
}
