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
    Artikel hamster = Artikel.builder().titel("Hamster").eigentuemer(heidi).standort(ad).build();


    Reservierung currentRes = Reservierung.builder().ende(currentDay).artikel(hamster).build();
    Reservierung futureRes = Reservierung.builder().ende(futureDay).artikel(hamster).build();
    Reservierung pastRes = Reservierung.builder().ende(pastDay).artikel(hamster).build();

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
        List<Reservierung> aktuelleReservierungen = rService.findCurrentByArtikel(hamster);
        assertThat(aktuelleReservierungen).containsExactlyInAnyOrder(currentRes, futureRes);
    }
}
