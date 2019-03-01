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
import java.util.ArrayList;
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

    Reservierung currentRes = Reservierung.builder().start(currentDay).ende(currentDay).artikel(hamster).bearbeitet(true)
            .akzeptiert(false).zurueckerhalten(false).zurueckgegeben(false).build();
    Reservierung futureRes = Reservierung.builder().start(futureDay).ende(futureDay).artikel(hamster).bearbeitet(false)
            .akzeptiert(false).zurueckerhalten(false).zurueckgegeben(false).build();
    Reservierung pastRes = Reservierung.builder().start(pastDay).ende(pastDay).artikel(hamster).bearbeitet(false)
            .akzeptiert(false).zurueckerhalten(false).zurueckgegeben(false).build();
    Reservierung farFutureRes = Reservierung.builder().start(currentDay.plusYears(2))
            .ende(currentDay.plusYears(2).plusDays(10)).artikel(hamster).bearbeitet(false)
            .akzeptiert(false).zurueckerhalten(false).zurueckgegeben(false).build();

    Reservierung currentRes2 = Reservierung.builder().start(currentDay).ende(futureDay).artikel(hamster).bearbeitet(true)
            .akzeptiert(true).zurueckerhalten(false).zurueckgegeben(false).build();
    Reservierung currentRes3 = Reservierung.builder().start(pastDay).ende(currentDay).artikel(hamster).bearbeitet(true)
            .akzeptiert(true).zurueckerhalten(false).zurueckgegeben(false).build();
    Reservierung currentRes4 = Reservierung.builder().start(pastDay).ende(futureDay.plusDays(3)).artikel(hamster).bearbeitet(true)
            .akzeptiert(true).zurueckerhalten(false).zurueckgegeben(false).build();
    Reservierung currentRes5 = Reservierung.builder().start(pastDay).ende(pastDay).artikel(hamster).bearbeitet(true)
            .akzeptiert(true).zurueckerhalten(false).zurueckgegeben(false).build();
    Reservierung pastRes2 = Reservierung.builder().start(pastDay).ende(currentDay).artikel(hamster).bearbeitet(false)
            .akzeptiert(false).zurueckerhalten(false).zurueckgegeben(false).build();
    Reservierung pastRes3 = Reservierung.builder().start(pastDay).ende(futureDay).artikel(hamster).bearbeitet(false)
            .akzeptiert(false).zurueckerhalten(false).zurueckgegeben(false).build();




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
    public void findsCurrentLendings(){
        currentRes2 = rService.addReservierung(currentRes2);
        currentRes3 = rService.addReservierung(currentRes3);
        currentRes4 = rService.addReservierung(currentRes4);
        currentRes5 = rService.addReservierung(currentRes5);


        List<Reservierung> result = rService.findReservierungByArtikelEigentuemerAndNichtAbgeschlossen(heidi);
        assertThat(result).containsExactly(currentRes5,currentRes3,currentRes2,currentRes4);
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

    @Test
    public void reservierungBlockedByOldReservation(){
        currentRes5 = rService.addReservierung(currentRes5);
        assertThat(rService.isAllowedReservierungsDate(hamster,
                currentDay,
                currentDay.plusYears(2).plusDays(4))).isEqualTo(false);
    }

    @Test
    public void reservierungNotBlockedByReservationWithStatus7(){
        pastRes2 = rService.addReservierung(pastRes2);
        pastRes3 = rService.addReservierung(pastRes3);
        rService.deleteReservierung(currentRes);
        assertThat(rService.isAllowedReservierungsDate(hamster,
                currentDay,
                currentDay.plusDays(4))).isEqualTo(true);
    }

    @Test
    public void findReservierungByAbgelaufenerFrist(){
        Benutzer hans = Benutzer.builder().name("Hans").email("hans@hans.de").build();
        benRepo.save(hans);
        currentRes5.setLeihender(hans);
        rService.addReservierung(currentRes5);
        assertThat(rService.fristAbgelaufeneReservierungen(hans)).containsExactly(currentRes5);
    }

    @Test
    public void findsReservierungByArtikelAndNichtBearbeitet(){
        rService.deleteReservierung(currentRes);
        Reservierung currentRes6 = Reservierung.builder().start(currentDay).ende(currentDay).artikel(hamster).bearbeitet(false)
                .akzeptiert(false).zurueckerhalten(false).zurueckgegeben(false).build();
        rService.addReservierung(currentRes6);
        assertThat(rService.findCurrentReservierungByArtikelAndBearbeitet(hamster)).containsExactlyInAnyOrder(currentRes6,futureRes);
    }

    @Test
    public void checksReservationOrder(){
        List<Reservierung> reservierungen = new ArrayList<Reservierung>();
        reservierungen.add(futureRes);
        reservierungen.add(pastRes);
        reservierungen.add(currentRes);
        assertThat(rService.orderByDate(reservierungen)).containsExactly(pastRes,currentRes,futureRes);
    }

    @Test
    public void fourReservierungenArticlesOfOneReservierungVerfuegbar(){

        //status code 4 for futureRes
        futureRes.setZurueckerhalten(false);
        futureRes.setBearbeitet(true);
        futureRes.setAkzeptiert(true);
        futureRes.setStart(pastDay);

        //status code 6 for pastRes
        pastRes.setZurueckgegeben(false);
        pastRes.setBearbeitet(true);
        pastRes.setAkzeptiert(true);

        //status code 1 and LocalDate = now for currentRes
        currentRes.setBearbeitet(false);

        farFutureRes = rService.addReservierung(farFutureRes);
        currentRes = rService.addReservierung(currentRes);
        futureRes = rService.addReservierung(futureRes);
        pastRes = rService.addReservierung(pastRes);

        rService.decideVerfuegbarkeit();

        assertThat(rService.findReservierungById(futureRes.getId()).getArtikel().isVerfuegbar()).isEqualTo(false);
        assertThat(rService.findReservierungById(pastRes.getId()).getArtikel().isVerfuegbar()).isEqualTo(false);
        assertThat(rService.findReservierungById(currentRes.getId()).getArtikel().isVerfuegbar()).isEqualTo(false);
        assertThat(rService.findReservierungById(farFutureRes.getId()).getArtikel().isVerfuegbar()).isEqualTo(false);

    }
}
