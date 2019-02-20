package hhu.propra2.javageddon.teils.modelTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.model.Adresse;
import hhu.propra2.javageddon.teils.model.Artikel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.model.Benutzer;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
@SpringBootTest
public class ArtikelRepoTest {

    ////////////////REQUIRED OBJECTS/////////////////////////////////////////////////////

    @Autowired
    ArtikelRepository artRepo;

    @Autowired
    BenutzerRepository benRepo;

    Benutzer tom = Benutzer.builder().name("Tom").email("tom@tomtom.com").build();
    Adresse ad1 = Adresse.builder().hausnummer("5e").strasse("Hauptstrasse").ort("koeln").plz(4000).build();
    Artikel fahrrad = Artikel.builder().titel("fahrrad").aktiv(true).eigentuemer(tom).standort(ad1).build();
    Artikel hamster =  Artikel.builder().titel("gwendolin").aktiv(false).eigentuemer(tom).standort(ad1).build();
    Artikel kochtopf =  Artikel.builder().titel("kochtopf").standort(ad1).aktiv(true).build();

    ////////////////////////////////////PREPARATIONS////////////////////////////////////////////////

    @Before
    public void testInit() {
        tom = benRepo.save(tom);
        fahrrad = artRepo.save(fahrrad);
    }

    @After
    public void testDelete(){
        artRepo.deleteAll();
        benRepo.deleteAll();
    }

    /////////////////////////////TESTS FOR RETRIEVAL OF OBJECTS///////////////////////////////////////////////////

    @Test
    public void retrievesArtikelById(){
        Artikel retrievedArtikel = artRepo.findById(fahrrad.getId());
        assertThat(retrievedArtikel).isEqualTo(fahrrad);
    }

    @Test
    public void findsArticlesByFalseActivity(){
        hamster = artRepo.save(hamster);
        kochtopf = artRepo.save(kochtopf);
        List<Artikel> inaktiveArtikel = artRepo.findByAktiv(false);
        assertThat(inaktiveArtikel).containsExactly(hamster);

    }

    @Test
    public void findArticlesByTrueActivity(){
        hamster = artRepo.save(hamster);
        kochtopf = artRepo.save(kochtopf);
        List<Artikel> aktiveArtikel = artRepo.findByAktiv(true);
        assertThat(aktiveArtikel).containsExactlyInAnyOrder(fahrrad, kochtopf);
    }
    
    @Test
    public void findArticlesByEigentuemer(){
        hamster = artRepo.save(hamster);
        kochtopf = artRepo.save(kochtopf);
        List<Artikel> aktiveArtikel = artRepo.findByEigentuemer(tom);
        assertThat(aktiveArtikel).containsExactlyInAnyOrder(fahrrad, hamster);
    }

    ////////////////////////////////////TESTS FOR UPDATING OBJECTS////////////////////////////////////////////


    @Test
    public void updatesArtikelCorrectly(){
        Adresse ad2 = Adresse.builder().ort("berlin").plz(5000).build();
        fahrrad.setTitel("megahammerfahrrad");
        fahrrad.setStandort(ad2);
        artRepo.save(fahrrad);
        Artikel retrievedArtikel = artRepo.findById(fahrrad.getId());
        assertThat(retrievedArtikel).isEqualTo(fahrrad);
    }

    ////////////////////////TESTS FOR RETRIEVEAL OF SEVERAL DIFFERENT OBJECTS////////////////////////////////////////////

    @Test
    public void savesAndRetrievesSeveralArticlesWithDifferentIDs(){
        hamster = artRepo.save(hamster);
        kochtopf = artRepo.save(kochtopf);
        Artikel retrievedArtikel = artRepo.findById(fahrrad.getId());
        Artikel retrievedArtikel2 = artRepo.findById(hamster.getId());
        Artikel retrievedArtikel3 = artRepo.findById(kochtopf.getId());

        Long id1 = retrievedArtikel.getId();
        Long id2 = retrievedArtikel2.getId();
        Long id3 = retrievedArtikel3.getId();

        assertThat(!(id1.equals(id2)));
        assertThat(!(id2.equals(id3)));
        assertThat(!(id3.equals(id1)));
    }

    ////////////////////////////////////////TESTS FOR CONNECTION BETWEEN OBJECTS////////////////////////////////////////

    @Test
    public void connectsArtikelAndBenutzer(){
        fahrrad.setEigentuemer(benRepo.findById(tom.getId()));
        artRepo.save(fahrrad);
        Artikel retrievedAtrikel = artRepo.findById(fahrrad.getId());
        assertThat(retrievedAtrikel.getEigentuemer()).isEqualTo(tom);
    }
}

