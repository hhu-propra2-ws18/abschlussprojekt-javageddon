package hhu.propra2.javageddon.teils.modelTest;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.dataaccess.ReservierungRepository;
import hhu.propra2.javageddon.teils.model.Adresse;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Reservierung;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
@SpringBootTest
public class ReservierungRepoTest {

    @Autowired
    ReservierungRepository resRepo;

    @Autowired
    ArtikelRepository artRepo;

    @Autowired
    BenutzerRepository benRepo;

    Benutzer hans = Benutzer.builder().name("Hans").email("hans@tomtom.com").build();
    Benutzer heidi = Benutzer.builder().name("Heidi").email("heidi@heidi.com").build();
    Adresse ad1 = Adresse.builder().hausnummer("5e").strasse("Hauptstrasse").ort("koeln").plz(4000).build();
    Artikel fahrrad = Artikel.builder().titel("fahrrad").aktiv(true).adresse(ad1).eigentuemer(hans).build();
    Artikel hamster =  Artikel.builder().titel("gwendolin").aktiv(false).adresse(ad1).eigentuemer(hans).build();
    Artikel kochtopf =  Artikel.builder().titel("kochtopf").adresse(ad1).aktiv(true).eigentuemer(heidi).build();
    Reservierung r1 = Reservierung.builder().leihender(heidi).artikel(hamster).build();
    Reservierung r2 = Reservierung.builder().leihender(hans).artikel(kochtopf).build();
    Reservierung r3 = Reservierung.builder().leihender(heidi).artikel(hamster).build();
    Reservierung r4 = Reservierung.builder().leihender(heidi).artikel(fahrrad).build();



    @Before
    public void testInit() {
        hans = benRepo.save(hans);
        heidi = benRepo.save(heidi);
        fahrrad = artRepo.save(fahrrad);
        hamster = artRepo.save(hamster);
        kochtopf = artRepo.save(kochtopf);
        r1 = resRepo.save(r1);

    }

    @After
    public void testDelete(){

        resRepo.deleteAll();
        artRepo.deleteAll();
        benRepo.deleteAll();
    }

    @Test
    public void retrievesReservierungCorrectly(){
        Optional<Reservierung> retrievedReservierung = resRepo.findById(r1.getId());
        assertThat(retrievedReservierung.isPresent());
        assertThat(retrievedReservierung.get()).isEqualTo(r1);
    }

    @Test
    public void findsReservierungByArtikel(){
        r2 = resRepo.save(r2);
        r3 = resRepo.save(r3);
        List<Reservierung> hamsterReservierungen = resRepo.findByArtikel(hamster);
        assertThat(hamsterReservierungen).containsExactlyInAnyOrder(r1, r3);
    }

    @Test
    public void findsReservierungByLeihender(){
        r2 = resRepo.save(r2);
        r3 = resRepo.save(r3);
        List<Reservierung> heidiReservierungen = resRepo.findByLeihender(heidi);
        assertThat(heidiReservierungen).containsExactlyInAnyOrder(r1, r3);
    }

    @Test
    public void findsReservierungByArtikelAndLeihender(){
        r2 = resRepo.save(r2);
        r3 = resRepo.save(r3);
        r4 = resRepo.save(r4);
        List<Reservierung> heidiAndFahrradReservierungen = resRepo.findByArtikelAndLeihender(fahrrad,heidi);
        assertThat(heidiAndFahrradReservierungen).containsExactlyInAnyOrder(r4);

    }

    @Test
    public void updatesReservierungCorrectly(){
        r1.setAkzeptiert(true);
        r1.setBearbeitet(true);
        resRepo.save(r1);
        Optional<Reservierung> retrievedReservierung = resRepo.findById(r1.getId());
        assertThat(retrievedReservierung.isPresent());
        assertThat(retrievedReservierung.get().getBearbeitet()).isEqualTo(true);
        assertThat(retrievedReservierung.get().getAkzeptiert()).isEqualTo(true);
    }

    @Test
    public void findsNothingByArtikel(){
        r2 = resRepo.save(r2);
        r3 = resRepo.save(r3);
        List<Reservierung> fahrradReservierungen = resRepo.findByArtikel(fahrrad);
        assertThat(fahrradReservierungen).isEmpty();
    }



}
