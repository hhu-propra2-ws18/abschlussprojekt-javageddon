package hhu.propra2.javageddon.teils.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.model.Adresse;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Reservierung;
import hhu.propra2.javageddon.teils.services.ArtikelService;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
@SpringBootTest
public class ArtikelServiceTest {


    @Autowired
    private BenutzerRepository benRepo;
    @Autowired
    private ArtikelRepository artRepo;

    @Autowired
    private ArtikelService aService;

    ArrayList<String> fotos = new ArrayList<String>();
    Benutzer heidi = Benutzer.builder().name("Harald").email("har@tom.de").build();
    Adresse ad = Adresse.builder().hausnummer("5").strasse("Hauptstrasse").ort("berlin").plz(4004).build();
    Artikel hamster = Artikel.builder().titel("Hamster").eigentuemer(heidi).standort(ad).fotos(fotos).build();
    

    @Before
    public void testInit() {
        heidi = benRepo.save(heidi);
        hamster = artRepo.save(hamster);
    }

    @After
    public void testDelete(){
        artRepo.deleteAll();
        benRepo.deleteAll();
    }
    
    @Test
    public void updateFotosCorrectly() {
        ArrayList<String> neueFotos = new ArrayList<String>(Arrays.asList("foto1","foto2"));
        aService.updateFotosArtikel(hamster, neueFotos);
        assertThat(hamster.getFotos()).containsExactlyInAnyOrder("foto1", "foto2");
    } 
}
