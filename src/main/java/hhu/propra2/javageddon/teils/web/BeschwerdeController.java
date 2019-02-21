package hhu.propra2.javageddon.teils.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BeschwerdeController {


    @GetMapping("/admin_clearing")
    public String adminClearing() {
        return "admin_clearing";
    }

}
