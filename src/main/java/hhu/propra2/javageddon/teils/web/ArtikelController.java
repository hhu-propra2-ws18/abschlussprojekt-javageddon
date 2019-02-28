package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.dataaccess.BeschwerdeRepository;
import hhu.propra2.javageddon.teils.dataaccess.VerkaufRepository;
import hhu.propra2.javageddon.teils.dataaccess.ProPay;
import hhu.propra2.javageddon.teils.model.*;
import hhu.propra2.javageddon.teils.services.ArtikelService;
import hhu.propra2.javageddon.teils.services.BenutzerService;
import hhu.propra2.javageddon.teils.services.ReservierungService;
import hhu.propra2.javageddon.teils.services.VerkaufArtikelService;
import hhu.propra2.javageddon.teils.services.VerkaufService;
import hhu.propra2.javageddon.teils.services.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ArtikelController {


    @Autowired
    private ArtikelService alleArtikel;

    @Autowired
    private BenutzerService alleBenutzer;

    @Autowired
    private ReservierungService alleReservierungen;

    @Autowired
    private BeschwerdeRepository alleBeschwerden;

    @Autowired
    private TransaktionService alleTransaktionen;

    @Autowired
    private VerkaufArtikelService alleVerkaufArtikel;

    @Autowired
    private VerkaufService alleVerkaeufe;

    @GetMapping("/")
    public String artikelListe(Model m){
        alleReservierungen.decideVerfuegbarkeit();
        m.addAttribute("alleArtikel", alleArtikel.findAllAktivArtikel());
        m.addAttribute("alleVerkaufArtikel", alleVerkaufArtikel.findAllArtikel());
        m.addAttribute("anzahlBeschwerden", alleBeschwerden.findAllByBearbeitet(false).size());
        return "start";
    }

    /*
    Diese Methode greift auf das Dateisystem des Dockercontainers zu und liefert das angefragte Bild aus.
    */
    @ResponseBody
    @RequestMapping(value = "/fotos/{id}", method = GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource getImageAsResource(@PathVariable("id") String id) {
        return new FileSystemResource("fotos/" + id + ".jpg");
    }

    @RequestMapping(value = "/details", method = GET)
    public String getDetailsByArtikelId( Model m, @RequestParam("id") long id) {
        alleReservierungen.decideVerfuegbarkeit();
        Artikel artikel = alleArtikel.findArtikelById(id);
        List<Reservierung> akzeptierteReservierungen = alleReservierungen.findCurrentReservierungByArtikelAndAkzeptiertAndNichtZurueckerhalten(artikel);
        List<Reservierung> reservierungenInBearbeitung = alleReservierungen.findCurrentReservierungByArtikelAndBearbeitet(artikel);
        List<Reservierung> artikelReservierungen = new ArrayList<Reservierung>();
        artikelReservierungen.addAll(akzeptierteReservierungen);
        artikelReservierungen.addAll(reservierungenInBearbeitung);
        m.addAttribute("alleReservierungen", alleReservierungen.orderByDate(artikelReservierungen));
        m.addAttribute("artikel", artikel);
        return "artikel_details";
    }

    @RequestMapping(value = "/verkauf/details", method = GET)
    public String getDetailsByArtikelIdVerkauf( Model m, @RequestParam("id") long id) {
        VerkaufArtikel artikel = alleVerkaufArtikel.findArtikelById(id);
        m.addAttribute("artikel", artikel);
        return "verkaufartikel_details";
    }

    @GetMapping("/artikel_erstellen")
    public String artikelErstellen(Model m){
        m.addAttribute("artikel", new Artikel());
        m.addAttribute("adresse", new Adresse());
        return "artikel_erstellen";
    }

    @PostMapping("/artikel_erstellen")
    public String erstelleArtikel(@Valid Artikel artikel, BindingResult artikelBindingResult, @Valid Adresse adresse, BindingResult standortBindingResult){
        if(artikelBindingResult.hasErrors() || standortBindingResult.hasErrors()){
            return "artikel_erstellen";
        }else {
            Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails) currentUser).getUsername();
            Long id = alleBenutzer.getIdByName(username);

            artikel.setStandort(adresse);
            artikel.setFotos(new ArrayList<String>());
            artikel.setEigentuemer(alleBenutzer.findBenutzerById(id));
            alleArtikel.addArtikel(artikel);
            return "redirect:/fotoupload/" + artikel.getId();
        }
    }

    @GetMapping("/verkaufartikel_erstellen")
    public String verkaufartikelErstellen(Model m){
        m.addAttribute("verkaufArtikel", new VerkaufArtikel());
        m.addAttribute("adresse", new Adresse());
        return "verkaufartikel_erstellen";
    }

    @PostMapping("/verkaufartikel_erstellen")
    public String erstelleVerkaufArtikel(@Valid VerkaufArtikel verkaufArtikel, BindingResult verkaufArtikelBindingResult, @Valid Adresse adresse, BindingResult standortBindingResult){
        if(verkaufArtikelBindingResult.hasErrors() || standortBindingResult.hasErrors()){
            return "verkaufartikel_erstellen";
        }else {
            Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails) currentUser).getUsername();
            Long id = alleBenutzer.getIdByName(username);

            verkaufArtikel.setStandort(adresse);
            verkaufArtikel.setFotos(new ArrayList<String>());
            verkaufArtikel.setEigentuemer(alleBenutzer.findBenutzerById(id));
            alleVerkaufArtikel.addArtikel(verkaufArtikel);
            return "redirect:/verkauf/fotoupload/" + verkaufArtikel.getId();
        }
    }


    @RequestMapping(value = "/reservieren", method = GET)
    public String artikelReservieren(Model m, @RequestParam("id") long id, @RequestParam(value = "error", defaultValue = "false", required = false) boolean error){
        alleReservierungen.decideVerfuegbarkeit();
        Reservierung reservierung = new Reservierung();
        Artikel artikel = alleArtikel.findArtikelById(id);
        reservierung.setStart(LocalDate.now());
        reservierung.setEnde(LocalDate.now());
        m.addAttribute("artikel", artikel);
        m.addAttribute("reservierung",reservierung);
        List<Reservierung> akzeptierteReservierungen = alleReservierungen.findCurrentReservierungByArtikelAndAkzeptiertAndNichtZurueckerhalten(artikel);
        List<Reservierung> reservierungenInBearbeitung = alleReservierungen.findCurrentReservierungByArtikelAndBearbeitet(artikel);
        List<Reservierung> artikelReservierungen = new ArrayList<Reservierung>();
        artikelReservierungen.addAll(akzeptierteReservierungen);
        artikelReservierungen.addAll(reservierungenInBearbeitung);
        m.addAttribute("alleReservierungen", alleReservierungen.orderByDate(artikelReservierungen));
        m.addAttribute("error", error);
        return "artikel_reservieren";
    }

    @PostMapping("/reservieren")
    public String reserviereArtikel(@ModelAttribute Reservierung reservierung, @ModelAttribute Artikel artikel){
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)currentUser).getUsername();
        Long id = alleBenutzer.getIdByName(username);
        reservierung.setBearbeitet(false);
        reservierung.setAkzeptiert(false);
        artikel = alleArtikel.findArtikelById(artikel.getId());
        reservierung.setArtikel(artikel);
        reservierung.setLeihender(alleBenutzer.findBenutzerById(id));
        ProPayUser proPayUser = ProPay.getProPayUser(username);
        if(artikel.getEigentuemer().equals(reservierung.getLeihender())){
            return "redirect:/reservieren?id=" + reservierung.getArtikel().getId() + "&error=true";
        }
        if(!alleReservierungen.hasEnoughMoney(reservierung,(int) proPayUser.getVerfuegbaresGuthaben())) {
            return "redirect:/reservieren?id=" + reservierung.getArtikel().getId() + "&error=true";
        }
        if(alleReservierungen.isAllowedReservierungsDate(reservierung.getArtikel(), reservierung.getStart(), reservierung.getEnde())){

            Reservations kaution = new Reservations();
            Reservations miete = new Reservations();
            kaution.setAmount(artikel.getKaution());
            kaution.setId(1);
            proPayUser.addReservation(kaution);
            reservierung.setKautionsId(ProPay.executeReservation(kaution,artikel.getEigentuemer(), reservierung.getLeihender()).getId());

            miete.setId(2);
            miete.setAmount(artikel.getKostenTag()*reservierung.calculateReservierungsLength());
            proPayUser.addReservation(miete);
            reservierung.setMieteId(ProPay.executeReservation(miete,artikel.getEigentuemer(), reservierung.getLeihender()).getId());

            alleReservierungen.addReservierung(reservierung);
            return "redirect:/";
        }else {
            return "redirect:/reservieren?id=" + reservierung.getArtikel().getId() + "&error=true";
        }
    }

    @GetMapping("/artikel_update/{id}/{aktiv}")
    public String updateArtikel(Model model, @ModelAttribute Artikel artikel, @PathVariable long id, @PathVariable String aktiv) {
        Artikel aktuellerArtikel = alleArtikel.findArtikelById(id);
        model.addAttribute("artikel", aktuellerArtikel);
        boolean active = Boolean.parseBoolean(aktiv);
        aktuellerArtikel.setAktiv(active);
        alleArtikel.addArtikel(aktuellerArtikel);
        return "redirect:/profil_ansicht/";
    }

    @RequestMapping(value = "/beschwerde", method = GET)
    public String artikelBeschweren(Model m, @RequestParam("id") long id, @ModelAttribute Reservierung reservierung){
        Beschwerde beschwerde = new Beschwerde();
        beschwerde.setKommentar("");
        m.addAttribute("reservierung",alleReservierungen.findReservierungById(id));
        m.addAttribute("beschwerde", beschwerde);
        return "artikel_beschwerde";
    }

    @PostMapping("/beschwerde")
    public String beschwereArtikel(@ModelAttribute Reservierung reservierung, @ModelAttribute Beschwerde beschwerde){
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)currentUser).getUsername();
        Long id = alleBenutzer.getIdByName(username);
        beschwerde.setReservierung(reservierung);
        beschwerde.setBearbeitet(false);
        beschwerde.setNutzer(alleBenutzer.findBenutzerById(id));
        alleBeschwerden.save(beschwerde);
        return "redirect:/profil_ansicht/";
    }

    @GetMapping("/reservierung_update/{id}/{akzeptiert}")
    public String updateReservierung(Model model, @ModelAttribute Reservierung reservierung, @PathVariable long id, @PathVariable String akzeptiert) {
        Reservierung aktuelleReservierung = alleReservierungen.findReservierungById(id);
        model.addAttribute("reservierung", aktuelleReservierung);
        boolean accepted = Boolean.parseBoolean(akzeptiert);
        aktuelleReservierung.setBearbeitet(true);
        aktuelleReservierung.setAkzeptiert(accepted);
        if (!accepted) {
            ProPay.releaseReservationKaution(aktuelleReservierung);
            ProPay.releaseReservationMiete(aktuelleReservierung);
        }
        alleReservierungen.addReservierung(aktuelleReservierung);
        return "redirect:/profil_ansicht/";
    }

    @GetMapping("/verkauf_update/{id}/{akzeptiert}")
    public String updateReservierung(Model model, @ModelAttribute Verkauf verkauf, @PathVariable long id, @PathVariable String akzeptiert) {
        Verkauf aktuellerVerkauf = alleVerkaeufe.findVerkaufById(id);
        model.addAttribute("verkauf", aktuellerVerkauf);
        boolean accepted = Boolean.parseBoolean(akzeptiert);
        if(!accepted) {
            aktuellerVerkauf.getArtikel().setVerfuegbar(true);
            ProPay.releaseVerkaufsPreis(aktuellerVerkauf);

        }else {
            ProPay.punishVerkaufsPreis(aktuellerVerkauf);
            Transaktion transaktion = new Transaktion();
            transaktion.setDatum(LocalDate.now());
            transaktion.setBetrag(-aktuellerVerkauf.getArtikel().getVerkaufsPreis());
            transaktion.setKontoinhaber(aktuellerVerkauf.getKaeufer());
            transaktion.setVerwendungszweck("Teils Kauf: " + aktuellerVerkauf.getArtikel().getTitel());
            alleTransaktionen.addTransaktion(transaktion);

            Transaktion transaktionEigentuemer = new Transaktion();
            transaktionEigentuemer.setDatum(LocalDate.now());
            transaktionEigentuemer.setBetrag(aktuellerVerkauf.getArtikel().getVerkaufsPreis());
            transaktionEigentuemer.setKontoinhaber(aktuellerVerkauf.getArtikel().getEigentuemer());
            transaktionEigentuemer.setVerwendungszweck("Teils Verkauf: " + aktuellerVerkauf.getArtikel().getTitel());
            alleTransaktionen.addTransaktion(transaktionEigentuemer);
        }
            aktuellerVerkauf.setBearbeitet(true);
            aktuellerVerkauf.setAkzeptiert(accepted);
            alleVerkaeufe.addVerkauf(aktuellerVerkauf);

        return "redirect:/profil_ansicht/";
    }

    @GetMapping("/reservierung_update/{id}")
    public String updateReservierung(Model model, @ModelAttribute Reservierung reservierung, @PathVariable long id) {
        Reservierung aktuelleReservierung = alleReservierungen.findReservierungById(id);
        model.addAttribute("reservierung", aktuelleReservierung);
        aktuelleReservierung.setZurueckerhalten(true);
        aktuelleReservierung.getArtikel().setVerfuegbar(true);
        alleReservierungen.addReservierung(aktuelleReservierung);

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

        return "redirect:/profil_ansicht/";
    }

    @GetMapping("/reservierung_sichtbar/{id}")
    public String updateSichtbarkeit(Model model, @ModelAttribute Reservierung reservierung, @PathVariable long id){
        Reservierung aktuelleReservierung = alleReservierungen.findReservierungById(id);
        alleReservierungen.deleteReservierung(aktuelleReservierung);
        return "redirect:/profil_ansicht/";
    }

    @GetMapping("/verkauf_sichtbar/{id}")
    public String updateSichtbarkeitVerkauf(Model model, @ModelAttribute Verkauf verkauf, @PathVariable long id){
        Verkauf aktuellerVerkauf = alleVerkaeufe.findVerkaufById(id);
        alleVerkaeufe.deleteVerkauf(aktuellerVerkauf);
        return "redirect:/profil_ansicht/";
    }

    @GetMapping("/reservierung_zurueckgeben/{id}")
    public String updateZurueckgeben(Model model, @ModelAttribute Reservierung reservierung, @PathVariable long id){
        Reservierung aktuelleReservierung = alleReservierungen.findReservierungById(id);
        model.addAttribute("reservierung", aktuelleReservierung);
        aktuelleReservierung.setZurueckgegeben(true);
        alleReservierungen.addReservierung(aktuelleReservierung);
        return "redirect:/profil_ansicht/";
    }

    @RequestMapping(value = "/kaufen", method = GET)
    public String artikelKaufen(Model m, @RequestParam("id") long id) {
        VerkaufArtikel aktuellerArtikel = alleVerkaufArtikel.findArtikelById(id);
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)currentUser).getUsername();
        Long benutzerid = alleBenutzer.getIdByName(username);
        Verkauf verkauf = new Verkauf();
        verkauf.setKaeufer(alleBenutzer.findBenutzerById(benutzerid));
        m.addAttribute("artikel", aktuellerArtikel);
        ProPayUser proPayUser = ProPay.getProPayUser(username);
        if(aktuellerArtikel.getEigentuemer().equals(verkauf.getKaeufer())){
            return "redirect:/reservieren?id=" + verkauf.getArtikel().getId() + "&error=true";
        }
        if(!alleReservierungen.hasEnoughMoney(verkauf,(int) proPayUser.getVerfuegbaresGuthaben())) {
            return "redirect:/reservieren?id=" + verkauf.getArtikel().getId() + "&error=true";
        }

        Reservations verkaufsRes = new Reservations();
        verkaufsRes.setAmount(verkauf.getArtikel().getVerkaufsPreis());
        proPayUser.addReservation(verkaufsRes);
        verkauf.setVerkaufsId(ProPay.executeReservation(verkaufsRes,verkauf.getArtikel().getEigentuemer(), verkauf.getKaeufer()).getId());


        aktuellerArtikel.setVerfuegbar(false);
        alleVerkaufArtikel.addArtikel(aktuellerArtikel);
        verkauf.setArtikel(aktuellerArtikel);
        alleVerkaeufe.addVerkauf(verkauf);
        return "redirect:/profil_ansicht/";
    }


    @RequestMapping(value = "/loeschen", method = GET)
    public String artikelLoeschen(@RequestParam("id") long id) {
        Verkauf verkauf = alleVerkaeufe.findVerkaufById(id);
        VerkaufArtikel aktuellerArtikel = alleVerkaufArtikel.findArtikelById(verkauf.getArtikel().getId());
        alleVerkaeufe.deleteVerkauf(verkauf);
        alleVerkaufArtikel.deleteArtikel(aktuellerArtikel);
        return "redirect:/profil_ansicht/";
    }

}