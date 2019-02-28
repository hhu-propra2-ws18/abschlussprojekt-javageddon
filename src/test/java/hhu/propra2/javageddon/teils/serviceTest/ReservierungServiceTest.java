package hhu.propra2.javageddon.teils.serviceTest;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.dataaccess.ReservierungRepository;
import hhu.propra2.javageddon.teils.model.Adresse;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Reservierung;
import hhu.propra2.javageddon.teils.services.ReservierungService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
@SpringBootTest
public class ReservierungServiceTest {

    @Autowired
    private ReservierungRepository resRepo;
    @Autowired
    private BenutzerRepository benRepo;
    @Autowired
    private ArtikelRepository artRepo;

    @Autowired
    ReservierungService rService = new ReservierungService(resRepo);

    LocalDate currentDay = LocalDate.now();
    LocalDate futureDay = LocalDate.now().plusDays(10);
    LocalDate pastDay = LocalDate.now().minusDays(10);

    Benutzer heidi = Benutzer.builder().name("Harald").email("har@tom.de").build();
    Adresse ad = Adresse.builder().hausnummer("5").strasse("Hauptstrasse").ort("berlin").plz(4004).build();
    Artikel hamster = Artikel.builder().titel("Hamster").eigentuemer(heidi).standort(ad).beschreibung("?").kaution(1).kostenTag(1).build();
    Artikel fahrrad = Artikel.builder().titel("fahrrad").aktiv(true).eigentuemer(heidi).standort(ad).beschreibung("?").kaution(1).kostenTag(1).build();

    Reservierung currentRes = Reservierung.builder().start(currentDay).ende(currentDay).artikel(hamster).bearbeitet(false)
            .akzeptiert(false).zurueckerhalten(false).zurueckgegeben(false).sichtbar(true).build();
    Reservierung futureRes = Reservierung.builder().start(futureDay).ende(futureDay).artikel(hamster).bearbeitet(false)
            .akzeptiert(false).zurueckerhalten(false).zurueckgegeben(false).sichtbar(true).build();
    Reservierung pastRes = Reservierung.builder().start(pastDay).ende(pastDay).artikel(hamster).build();
    Reservierung farFutureRes = Reservierung.builder().start(currentDay.plusYears(2))
            .ende(currentDay.plusYears(2).plusDays(10)).artikel(hamster).bearbeitet(false)
            .akzeptiert(false).zurueckerhalten(false).zurueckgegeben(false).sichtbar(true).build();
    @Before
    public void testInit() {
        heidi = benRepo.save(heidi);
        hamster = artRepo.save(hamster);
        currentRes = rService.addReservierung(currentRes);
        pastRes = rService.addReservierung(pastRes);
        futureRes = rService.addReservierung(futureRes);
    }

    @After
    public void testDelete(){
        resRepo.deleteAll();
        artRepo.deleteAll();
        benRepo.deleteAll();
    }

    @Test
    public void selectsOnlyCurrentReservierungen() {
        List<Reservierung> aktuelleReservierungen = rService.findCurrentReservierungByArtikelOrderedByDate(hamster);
        assertThat(aktuelleReservierungen).containsExactly(currentRes, futureRes);
    }

    @Test
    public void selectsZeroReservierungen() {
        fahrrad = artRepo.save(fahrrad);
        List<Reservierung> aktuelleReservierungen = rService.findCurrentReservierungByArtikelOrderedByDate(fahrrad);
        assertThat(aktuelleReservierungen).isNotNull();
        assertThat(aktuelleReservierungen).isInstanceOf(List.class);
        assertThat(aktuelleReservierungen).isEmpty();
    }

    @Test
    public void antragStartDateIsBeforeCurrentDate() {
        assertThat(rService.isAllowedReservierungsDate(hamster, pastDay, currentDay)).isEqualTo(false);
    }

    @Test
    public void antragEndeDateIsBeforeAntragStartDate() {
        assertThat(rService.isAllowedReservierungsDate(hamster, currentDay, pastDay)).isEqualTo(false);
    }

    @Test
    public void antragDateIsAllowed() {
        assertThat(rService.isAllowedReservierungsDate(hamster, currentDay.plusDays(5), currentDay.plusDays(5))).isEqualTo(true);
    }

    @Test
    public void startDateIsNotAllowed(){
        assertThat(rService.isAllowedReservierungsDate(hamster, futureDay, futureDay.plusDays(5))).isEqualTo(false);
    }

    @Test
    public void endDateIsNotAllowed(){
        assertThat(rService.isAllowedReservierungsDate(hamster, currentDay.minusDays(5), currentDay)).isEqualTo(false);
    }

    @Test
    public void reservierungInterferesWithAnotherReservierung(){
        assertThat(rService.isAllowedReservierungsDate(hamster, currentDay.minusDays(5), currentDay.plusDays(5))).isEqualTo(false);
    }

    @Test
    public void reservierungIsBlockedCompletelyByAnotherReservierung(){
        farFutureRes = rService.addReservierung(farFutureRes);
        assertThat(rService.isAllowedReservierungsDate(hamster,
                currentDay.plusYears(2).plusDays(2),
                currentDay.plusYears(2).plusDays(4))).isEqualTo(false);
    }

    @Test
    public void reservierungBlockedByStart(){
        farFutureRes = rService.addReservierung(farFutureRes);
        assertThat(rService.isAllowedReservierungsDate(hamster,
                currentDay.plusYears(2).plusDays(2),
                currentDay.plusYears(2).plusDays(14))).isEqualTo(false);
    }

    @Test
    public void reservierungBlockedByEnde(){
        farFutureRes = rService.addReservierung(farFutureRes);
        assertThat(rService.isAllowedReservierungsDate(hamster,
                currentDay.plusYears(2).minusDays(2),
                currentDay.plusYears(2).plusDays(4))).isEqualTo(false);
    }

    /*@Test
    public void threeReservierungenOneVerfuegbar(){

        //status code 4 for futureRes
        futureRes.setZurueckerhalten(false);
        futureRes.setBearbeitet(true);
        futureRes.setAkzeptiert(true);
        futureRes.setStart(start);

        res3.setZurueckgegeben(false);
            res3.setBearbeitet(true);
            res3.setAkzeptiert(true);
            res3.setEnde(ende);
            assertThat(res3.ermittleStatus()).isEqualTo(6); }


    }*/




}
