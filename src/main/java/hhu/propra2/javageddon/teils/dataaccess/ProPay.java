package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.*;
import org.springframework.web.client.RestTemplate;

public class ProPay {

     static final String URL_ACCOUNT = "http://proPay:8888/account/";
    static final String URL_RESERVATION = "http://proPay:8888/reservation/";

        public static ProPayUser getProPayUser(String username) {

            RestTemplate restTemplate = new RestTemplate();

            // Send request with GET method and default Headers.
            return  restTemplate.getForObject(URL_ACCOUNT + username, ProPayUser.class);

        }

        public static void heresTheMoney(Aufladung aufladung){

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(URL_ACCOUNT + aufladung.getProPayUser().getAccount() + "?amount=" +(int)aufladung.getBetrag(),"amount="+ (int)aufladung.getBetrag(),ProPayUser.class);
        }

    public static void executeTransaktion(Ueberweisung ueberweisung){

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(URL_ACCOUNT + ueberweisung.getKontoinhaber().getAccount()+ "/transfer/" +
                                    ueberweisung.getZielKonto().getAccount() + "?amount=" + ueberweisung.getBetrag(),
                                    "amount="+ (int)ueberweisung.getBetrag(),ProPayUser.class);
    }


    public static Reservations executeReservation(Reservations reservations, Benutzer zielKonto, Benutzer kontoinhaber){

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(URL_RESERVATION + "reserve/" + kontoinhaber.getName()+ "/" +
                                        zielKonto.getName() + "?amount=" + reservations.getAmount(),
                                    "amount="+ (int)reservations.getAmount(),Reservations.class);
    }

}
