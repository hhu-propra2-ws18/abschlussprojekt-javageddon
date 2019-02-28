package hhu.propra2.javageddon.teils.services;

import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.InetAddress;

@Controller
public class ProPayService {


    public static Boolean checkConnection() {

        try {
            return InetAddress.getByName("proPay").isReachable(500);
        } catch (IOException e) {
            return false;
        }


    }
}
