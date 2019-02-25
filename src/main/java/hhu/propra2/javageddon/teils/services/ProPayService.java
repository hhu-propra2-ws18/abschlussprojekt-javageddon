package hhu.propra2.javageddon.teils.services;

import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.ProPayUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProPayService {

    public ProPayUser getProPayUser(Benutzer benutzer){
        String name = benutzer.getName();

        RestTemplate restTemplate = new RestTemplate();
        ProPayUser proPayUser = restTemplate.getForObject("localhost:8888/account/"+name,ProPayUser.class);

        return proPayUser;
    }
}
