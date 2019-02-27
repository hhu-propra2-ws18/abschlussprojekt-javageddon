package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.dataaccess.BeschwerdeRepository;
import hhu.propra2.javageddon.teils.dataaccess.ProPay;
import hhu.propra2.javageddon.teils.model.*;
import hhu.propra2.javageddon.teils.services.ArtikelService;
import hhu.propra2.javageddon.teils.services.BenutzerService;
import hhu.propra2.javageddon.teils.services.ReservierungService;
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

    @GetMapping("/")
    public String artikelListe(Model m){
        m.addAttribute("alleArtikel", alleArtikel.findAllAktivArtikel());
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
        Artikel artikel = alleArtikel.findArtikelById(id);
        List<Reservierung> artikelReservierungen = alleReservierungen.findCurrentReservierungByArtikelOrderedByDate(artikel);
        m.addAttribute("alleReservierungen", artikelReservierungen);
        m.addAttribute("artikel", artikel);
        return "artikel_details";
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

    @RequestMapping(value = "/reservieren", method = GET)
    public String artikelReservieren(Model m, @RequestParam("id") long id, @RequestParam(value = "error", defaultValue = "false", required = false) boolean error){
        Reservierung reservierung = new Reservierung();
        Artikel artikel = alleArtikel.findArtikelById(id);
        reservierung.setStart(LocalDate.now());
        reservierung.setEnde(LocalDate.now());
        m.addAttribute("artikel", artikel);
        m.addAttribute("reservierung",reservierung);
        List<Reservierung> artikelReservierungen = alleReservierungen.findCurrentReservierungByArtikelOrderedByDate(artikel);
        m.addAttribute("alleReservierungen", artikelReservierungen);
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
        reservierung.setArtikel(alleArtikel.findArtikelById(artikel.getId()));
        reservierung.setLeihender(alleBenutzer.findBenutzerById(id));
        ProPayUser proPayUser = ProPay.getProPayUser(username);
        if(!alleReservierungen.hasEnoughMoney(reservierung,(int) proPayUser.getVerfuegbaresGuthaben())) {
            return "redirect:/reservieren?id=" + reservierung.getArtikel().getId() + "&error=true";
        }
        if(alleReservierungen.isAllowedReservierungsDate(reservierung.getArtikel(), reservierung.getStart(), reservierung.getEnde())){
            alleReservierungen.addReservierung(reservierung);

            Reservations kaution = new Reservations();
            Reservations miete = new Reservations();
            kaution.setAmount(artikel.getKaution());
            proPayUser.addReservation(kaution);
            kaution = ProPay.executeReservation(kaution,artikel.getEigentuemer(), reservierung.getLeihender());

            miete.setAmount(artikel.getKostenTag()*reservierung.calculateReservierungsLength());
            proPayUser.addReservation(miete);
            reservierung.setMieteId(ProPay.executeReservation(miete,artikel.getEigentuemer(), reservierung.getLeihender()).getId());
            

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
        alleReservierungen.addReservierung(aktuelleReservierung);
        return "redirect:/profil_ansicht/";
    }

    @GetMapping("/reservierung_update/{id}")
    public String updateReservierung(Model model, @ModelAttribute Reservierung reservierung, @PathVariable long id) {
        Reservierung aktuelleReservierung = alleReservierungen.findReservierungById(id);
        model.addAttribute("reservierung", aktuelleReservierung);
        aktuelleReservierung.setZurueckerhalten(true);
        alleReservierungen.addReservierung(aktuelleReservierung);
        return "redirect:/profil_ansicht/";
    }

    @GetMapping("/reservierung_sichtbar/{id}")
    public String updateSichtbarkeit(Model model, @ModelAttribute Reservierung reservierung, @PathVariable long id){
        Reservierung aktuelleReservierung = alleReservierungen.findReservierungById(id);
        model.addAttribute("reservierung", aktuelleReservierung);
        aktuelleReservierung.setSichtbar(false);
        alleReservierungen.addReservierung(aktuelleReservierung);
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
}