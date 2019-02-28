package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.dataaccess.BeschwerdeRepository;
import hhu.propra2.javageddon.teils.dataaccess.ProPay;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Beschwerde;
import hhu.propra2.javageddon.teils.model.Reservierung;
import hhu.propra2.javageddon.teils.model.Transaktion;
import hhu.propra2.javageddon.teils.services.BenutzerService;
import hhu.propra2.javageddon.teils.services.BeschwerdeService;
import hhu.propra2.javageddon.teils.services.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
public class BeschwerdeController {

    @Autowired
    private BeschwerdeService alleBeschwerden;

    @Autowired
    private BenutzerService alleBenutzer;

    @Autowired
    private TransaktionService alleTransaktionen;


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
        Reservierung aktuelleReservierung = beschwerde.getReservierung();
        aktuelleReservierung.setZurueckerhalten(true);
        aktuelleReservierung.setZurueckgegeben(true);
        alleBeschwerden.addBeschwerde(beschwerde);
        if(benutzer == aktuelleReservierung.getLeihender()){
            ProPay.releaseReservationKaution(aktuelleReservierung);
            ProPay.punishReservationMiete(aktuelleReservierung);
            Transaktion transaktion = new Transaktion();
            transaktion.setDatum(LocalDate.now());
            transaktion.setBetrag(-aktuelleReservierung.calculateReservierungsCost());
            transaktion.setKontoinhaber(aktuelleReservierung.getLeihender());
            transaktion.setVerwendungszweck("Teils Ausleihe: " + aktuelleReservierung.getArtikel().getTitel());
            alleTransaktionen.addTransaktion(transaktion);

            Transaktion transaktionEigentuemer = new Transaktion();
            transaktionEigentuemer.setDatum(LocalDate.now());
            transaktionEigentuemer.setBetrag(aktuelleReservierung.calculateReservierungsCost());
            transaktionEigentuemer.setKontoinhaber(aktuelleReservierung.getArtikel().getEigentuemer());
            transaktionEigentuemer.setVerwendungszweck("Teils Leihe: " + aktuelleReservierung.getArtikel().getTitel());
            alleTransaktionen.addTransaktion(transaktionEigentuemer);
        } else if(benutzer == aktuelleReservierung.getArtikel().getEigentuemer()){
            ProPay.punishReservationMiete(aktuelleReservierung);
            ProPay.punishReservationKaution(aktuelleReservierung);
            Transaktion transaktion = new Transaktion();
            transaktion.setDatum(LocalDate.now());
            transaktion.setBetrag(-aktuelleReservierung.calculateReservierungsCost());
            transaktion.setKontoinhaber(aktuelleReservierung.getLeihender());
            transaktion.setVerwendungszweck("Teils Ausleihe: " + aktuelleReservierung.getArtikel().getTitel());
            alleTransaktionen.addTransaktion(transaktion);

            transaktion.setDatum(LocalDate.now());
            transaktion.setBetrag(-aktuelleReservierung.getArtikel().getKaution());
            transaktion.setKontoinhaber(aktuelleReservierung.getLeihender());
            transaktion.setVerwendungszweck("Teils Clearing hat sich für den Eigentümer vom Artikel: " + aktuelleReservierung.getArtikel().getTitel() + " entschieden");
            alleTransaktionen.addTransaktion(transaktion);

            Transaktion transaktionEigentuemer = new Transaktion();
            transaktionEigentuemer.setDatum(LocalDate.now());
            transaktionEigentuemer.setBetrag(aktuelleReservierung.calculateReservierungsCost());
            transaktionEigentuemer.setKontoinhaber(aktuelleReservierung.getArtikel().getEigentuemer());
            transaktionEigentuemer.setVerwendungszweck("Teils Leihe: " + aktuelleReservierung.getArtikel().getTitel());
            alleTransaktionen.addTransaktion(transaktionEigentuemer);

            transaktionEigentuemer.setDatum(LocalDate.now());
            transaktionEigentuemer.setBetrag(aktuelleReservierung.getArtikel().getKaution());
            transaktionEigentuemer.setKontoinhaber(aktuelleReservierung.getArtikel().getEigentuemer());
            transaktionEigentuemer.setVerwendungszweck("Teils Clearing hat sich für Sie entschieden (" + aktuelleReservierung.getArtikel().getTitel() + ")");
            alleTransaktionen.addTransaktion(transaktionEigentuemer);
        }
        alleBeschwerden.deleteBeschwerde(beschwerde);
        return "redirect:/admin_clearing";
    }
}
