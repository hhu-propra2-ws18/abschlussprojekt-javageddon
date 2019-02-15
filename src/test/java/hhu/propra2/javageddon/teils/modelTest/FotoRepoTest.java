package hhu.propra2.javageddon.teils.modelTest;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.FotoRepository;
import hhu.propra2.javageddon.teils.model.Adresse;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Foto;
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
public class FotoRepoTest {

    @Autowired
    FotoRepository fotoRepo;

    @Autowired
    ArtikelRepository artRepo;

    Adresse ad1 = Adresse.builder().hausnummer("5e").strasse("Hauptstrasse").ort("koeln").plz(4000).build();
    Artikel fahrrad = Artikel.builder().titel("fahrrad").aktiv(true).adresse(ad1).build();
    Artikel hamster =  Artikel.builder().titel("gwendolin").aktiv(true).adresse(ad1).build();
    Foto bild1 = Foto.builder().link("/home/bilder/mein_hamster.jpg").artikel(hamster).build();
    Foto bild2 = Foto.builder().link("/home/bilder/gwendolin.jpg").artikel(hamster).build();
    Foto pic3 = Foto.builder().link("/home/bilder/mein_fahrrad.jpg").artikel(fahrrad).build();

    @Before
    public void testInit() {

        hamster = artRepo.save(hamster);
        fahrrad = artRepo.save(fahrrad);
        bild1 = fotoRepo.save(bild1);

    }

    @After
    public void testDelete(){
        fotoRepo.deleteAll();
    }

    @Test
    public void retrievesFotoCorrectly(){

        Optional<Foto> retrievedFoto = fotoRepo.findById(bild1.getId());
        assertThat(retrievedFoto.isPresent());
        assertThat(retrievedFoto.get().getLink()).isEqualTo(bild1.getLink());
        assertThat(retrievedFoto.get().getArtikel().getId()).isEqualTo(hamster.getId());
    }

    @Test
    public void updatesBenutzerCorrectly(){
        bild1.setLink("abcdefg");
        bild1.setArtikel(fahrrad);
        fotoRepo.save(bild1);
        Optional<Foto> retrievedFoto = fotoRepo.findById(bild1.getId());
        assertThat(retrievedFoto.isPresent());
        assertThat(retrievedFoto.get().getLink()).isEqualTo("abcdefg");
        assertThat(retrievedFoto.get().getArtikel().getId()).isEqualTo(fahrrad.getId());

    }

    @Test
    public void savesAndRetrievesSeveralFotosWithDifferentIds(){
        bild2 = fotoRepo.save(bild2);
        pic3 = fotoRepo.save(pic3);
        Optional<Foto> retrievedFoto = fotoRepo.findById(bild1.getId());
        Optional<Foto> retrievedFoto2 = fotoRepo.findById(bild2.getId());
        Optional<Foto> retrievedFoto3 = fotoRepo.findById(pic3.getId());

        Long id1 = retrievedFoto.get().getId();
        Long id2 = retrievedFoto2.get().getId();
        Long id3 = retrievedFoto3.get().getId();

        assertThat(!(id1.equals(id2)));
        assertThat(!(id2.equals(id3)));
        assertThat(!(id3.equals(id1)));
    }

    @Test
    public void oneFotoIsFoundByArtikel(){
        List<Foto> fotoList = fotoRepo.findByArtikel(hamster);
        assertThat(fotoList.size()).isEqualTo(1);
        assertThat(fotoList.get(0).getId()).isEqualTo(bild1.getId());
    }

    @Test
    public void twoFotosAreFoundByArtikel(){
        bild2 = fotoRepo.save(bild2);
        pic3 = fotoRepo.save(pic3);
        List<Foto> fotoList = fotoRepo.findByArtikel(hamster);
        assertThat(fotoList.size()).isEqualTo(2);
        assertThat(fotoList.get(0).getId()).isNotEqualTo(pic3.getId());
        assertThat(fotoList.get(1).getId()).isNotEqualTo(pic3.getId());
    }

}
