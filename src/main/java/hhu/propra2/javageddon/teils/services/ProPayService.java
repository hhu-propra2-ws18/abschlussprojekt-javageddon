package hhu.propra2.javageddon.teils.services;

import hhu.propra2.javageddon.teils.model.*;
import org.springframework.http.HttpEntity;
import hhu.propra2.javageddon.teils.model.Aufladung;
import hhu.propra2.javageddon.teils.model.ProPayUser;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetAddress;

public class ProPayService {

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

    public static void releaseVerkaufsPreis(Verkauf verkauf){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(URL_RESERVATION + "release/" + verkauf.getKaeufer().getName()+
                "?reservationId=" + verkauf.getVerkaufsId(), "?reservationId=" +
                verkauf.getVerkaufsId(),ProPayUser.class);
    }

    public static void punishVerkaufsPreis(Verkauf verkauf){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(URL_RESERVATION + "punish/" + verkauf.getKaeufer().getName()+
                "?reservationId=" + verkauf.getVerkaufsId(), "?reservationId=" +
                verkauf.getVerkaufsId(),ProPayUser.class);
    }


    public static void releaseReservationKaution(Reservierung reservierung){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(URL_RESERVATION + "release/" + reservierung.getLeihender().getName()+
                                      "?reservationId=" + reservierung.getKautionsId(), "?reservationId=" +
                                      reservierung.getKautionsId(),ProPayUser.class);
    }

    public static void releaseReservationMiete(Reservierung reservierung){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(URL_RESERVATION + "release/" + reservierung.getLeihender().getName()+
                "?reservationId=" + reservierung.getMieteId(), "?reservationId=" +
                reservierung.getMieteId(),ProPayUser.class);
    }

    public static void punishReservationKaution(Reservierung reservierung){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(URL_RESERVATION + "punish/" + reservierung.getLeihender().getName()+
                "?reservationId=" + reservierung.getKautionsId(), "?reservationId=" +
                reservierung.getKautionsId(),ProPayUser.class);
    }

    public static void punishReservationMiete(Reservierung reservierung){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(URL_RESERVATION + "punish/" + reservierung.getLeihender().getName()+
                "?reservationId=" + reservierung.getMieteId(), "?reservationId=" +
                reservierung.getMieteId(),ProPayUser.class);
    }

    public static Boolean checkConnection() {

        try {
            return InetAddress.getByName("proPay").isReachable(500);
        } catch (IOException e) {
            return false;
        }

    }



}
