package hhu.propra2.javageddon.teils.serviceTest;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.dataaccess.BeschwerdeRepository;
import hhu.propra2.javageddon.teils.dataaccess.ReservierungRepository;
import hhu.propra2.javageddon.teils.model.*;
import hhu.propra2.javageddon.teils.services.ArtikelService;
import hhu.propra2.javageddon.teils.services.BeschwerdeService;
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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
@SpringBootTest
public class BeschwerdeServiceTest {


    @Autowired
    private BenutzerRepository benRepo;
    @Autowired
    private ArtikelRepository artRepo;

    @Autowired
    private ReservierungRepository resRepo;

    @Autowired
    private BeschwerdeRepository besRepo;

    @Autowired
    private BeschwerdeService bService = new BeschwerdeService(besRepo);

    LocalDate currentDay = LocalDate.now();

    ArrayList<String> fotos = new ArrayList<String>();
    Benutzer heidi = Benutzer.builder().name("Harald").email("har@tom.de").build();
    Benutzer harry = Benutzer.builder().name("Harald").email("har@tom.de").build();
    Adresse ad = Adresse.builder().hausnummer("5").strasse("Hauptstrasse").ort("berlin").plz(4004).build();
    Artikel hamster = Artikel.builder().titel("Hamster").eigentuemer(heidi).standort(ad).fotos(fotos).beschreibung("?").kaution(1).kostenTag(1).build();
    Reservierung res = Reservierung.builder().start(currentDay).ende(currentDay).artikel(hamster).leihender(harry).build();
    Beschwerde b1 = Beschwerde.builder().reservierung(res).nutzer(heidi).kommentar("blah").bearbeitet(false).build();
    Beschwerde b2 = Beschwerde.builder().reservierung(res).nutzer(harry).kommentar("blaa").bearbeitet(true).build();
    Beschwerde b3 = Beschwerde.builder().reservierung(res).nutzer(heidi).kommentar("blab").bearbeitet(false).build();
    

    @Before
    public void testInit() {
        heidi = benRepo.save(heidi);
        harry = benRepo.save(harry);
        hamster = artRepo.save(hamster);
        res = resRepo.save(res);
        b1 = besRepo.save(b1);
        b2 = besRepo.save(b2);
        b3 = besRepo.save(b3);
    }

    @After
    public void testDelete(){
        besRepo.deleteAll();
        resRepo.deleteAll();
        artRepo.deleteAll();
        benRepo.deleteAll();
    }
    
    @Test
    public void findUnbearbeitetBeschwerdenCorrectly() {
        List<Beschwerde> unbearbeiteteBeschwerden = bService.findAllUnbearbeitetBeschwerden();
        assertThat(unbearbeiteteBeschwerden).containsExactlyInAnyOrder(b1, b3);
    }

    @Test
    public void retrievesCorrectly() {
        besRepo.delete(b3);
        b3 = bService.addBeschwerde(b3);
        assertThat(bService.findBeschwerdeById(b3.getId())).isEqualTo(b3);
    }
}
