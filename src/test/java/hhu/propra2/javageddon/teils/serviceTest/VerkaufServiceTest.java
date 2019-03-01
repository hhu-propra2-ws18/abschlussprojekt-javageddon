package hhu.propra2.javageddon.teils.serviceTest;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.dataaccess.VerkaufArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.VerkaufRepository;
import hhu.propra2.javageddon.teils.model.*;
import hhu.propra2.javageddon.teils.services.ArtikelService;
import hhu.propra2.javageddon.teils.services.VerkaufArtikelService;
import hhu.propra2.javageddon.teils.services.VerkaufService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
@SpringBootTest
public class VerkaufServiceTest {


    @Autowired
    private BenutzerRepository benRepo;
    @Autowired
    private VerkaufRepository verRepo;

    @Autowired
    private VerkaufArtikelRepository artRepo;

    @Autowired
    private VerkaufService aService = new VerkaufService(verRepo);

    ArrayList<String> fotos = new ArrayList<String>();
    Benutzer heidi = Benutzer.builder().name("Harald").email("har@tom.de").build();
    Adresse ad = Adresse.builder().hausnummer("5").strasse("Hauptstrasse").ort("berlin").plz(4004).build();
    VerkaufArtikel hamster = VerkaufArtikel.builder().titel("Hamster").eigentuemer(heidi).standort(ad).fotos(fotos).beschreibung("?").verkaufsPreis(10).build();
    Verkauf verkauf = Verkauf.builder().artikel(hamster).kaeufer(heidi).build();

    @Before
    public void testInit() {
        heidi = benRepo.save(heidi);
        hamster = artRepo.save(hamster);
        verkauf = aService.addVerkauf(verkauf);
    }

    @After
    public void testDelete(){
        verRepo.deleteAll();
        artRepo.deleteAll();
        benRepo.deleteAll();
    }
    
    @Test
    public void HasEnoughMoney() {

        assertThat(aService.hasEnoughMoney(verkauf, 11)).isEqualTo(true);
    }

    @Test
    public void DoesNotHaveEnoughMoney() {

        assertThat(aService.hasEnoughMoney(verkauf, 9)).isEqualTo(false);
    }
}
