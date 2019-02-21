package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.dataaccess.BeschwerdeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BeschwerdeController {

    @Autowired
    private BeschwerdeRepository alleBeschwerden;

    @GetMapping("/admin_clearing")
    public String adminClearing(Model m) {
        m.addAttribute("alleBeschwerden", alleBeschwerden.findAll());
        return "admin_clearing";
    }

}
