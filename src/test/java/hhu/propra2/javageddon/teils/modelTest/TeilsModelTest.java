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
public class TeilsModelTest {

	////////////////REQUIRED OBJECTS/////////////////////////////////////////////////////

	@Autowired
	BenutzerRepository testRepo;

	@Autowired
	ArtikelRepository artRepo;
	
	Benutzer tom = Benutzer.builder().name("Tom").email("tom@tomtom.com").build();
	Benutzer herbert = Benutzer.builder().name("Herbert").email("her@bert.com").build();
	Benutzer jimbo = Benutzer.builder().name("Jimbo").email("jjj@j.org").build();
	Adresse ad1 = Adresse.builder().hausnummer("5e").strasse("Hauptstrasse").ort("koeln").plz(4000).build();
	Artikel fahrrad = Artikel.builder().titel("fahrrad").aktiv(true).adresse(ad1).build();
	Artikel hamster =  Artikel.builder().titel("gwendolin").aktiv(false).adresse(ad1).build();
	Artikel kochtopf =  Artikel.builder().titel("kochtopf").adresse(ad1).aktiv(true).build();

	////////////////////////////////////PREPARATIONS////////////////////////////////////////////////

	@Before
	public void testInit() {
		tom = testRepo.save(tom);
		fahrrad = artRepo.save(fahrrad);
	}
	
	@After
	public void testDelete(){
		artRepo.deleteAll();
		testRepo.deleteAll();
	}

	/////////////////////////////TESTS FOR RETRIEVAL OF OBJECTS///////////////////////////////////////////////////

	@Test
	public void retrievesBenutzerCorrectly() {
		Optional<Benutzer> retrievedBenutzer = testRepo.findById(tom.getId());
		assertThat(retrievedBenutzer.isPresent());
		assertThat(retrievedBenutzer.get().getName()).isEqualTo(tom.getName());
		assertThat(retrievedBenutzer.get().getEmail()).isEqualTo(tom.getEmail());

	}

	@Test
	public void retrievesArtikelCorrectly(){

		Optional<Artikel> retrievedArtikel = artRepo.findById(fahrrad.getId());
		assertThat(retrievedArtikel.isPresent());
		assertThat(retrievedArtikel.get().getTitel()).isEqualTo(fahrrad.getTitel());
		assertThat(retrievedArtikel.get().isAktiv());

	}

	@Test
	public void findsArticlesByActivity(){
		hamster = artRepo.save(hamster);
		kochtopf = artRepo.save(kochtopf);
		List<Artikel> inaktiveArtikel = artRepo.findByAktiv(false);
		assertThat(inaktiveArtikel.get(0).getId()).isEqualTo(hamster.getId());

	}

	////////////////////////////////////TESTS FOR UPDATING OBJECTS////////////////////////////////////////////

	@Test
	public void updatesBenutzerCorrectly(){
		tom.setEmail("hello@hello.com");
		tom.setName("Thomas");
		testRepo.save(tom);
		Optional<Benutzer> retrievedBenutzer = testRepo.findById(tom.getId());
		assertThat(retrievedBenutzer.isPresent());
		assertThat(retrievedBenutzer.get().getName()).isEqualTo("Thomas");
		assertThat(retrievedBenutzer.get().getEmail()).isEqualTo("hello@hello.com");
		
	}

	@Test
	public void updatesArtikelCorrectly(){
		Adresse ad2 = Adresse.builder().ort("berlin").plz(5000).build();
		fahrrad.setTitel("megahammerfahrrad");
		fahrrad.setAdresse(ad2);
		artRepo.save(fahrrad);
		Optional<Artikel> retrievedArtikel = artRepo.findById(fahrrad.getId());
		assertThat(retrievedArtikel.isPresent());
		assertThat(retrievedArtikel.get().getTitel()).isEqualTo("megahammerfahrrad");
		assertThat(!(retrievedArtikel.get().getAdresse().getOrt()).equals("berlin"));
	}

	////////////////////////TESTS FOR RETRIEVEAL OF SEVERAL DIFFERENT OBJECTS////////////////////////////////////////////

	@Test
	public void savesAndRetrievesSeveralUsersWithDifferentIds(){
		herbert = testRepo.save(herbert);
		jimbo = testRepo.save(jimbo);
		Optional<Benutzer> retrievedBenutzer = testRepo.findById(tom.getId());
		Optional<Benutzer> retrievedBenutzer2 = testRepo.findById(herbert.getId());
		Optional<Benutzer> retrievedBenutzer3 = testRepo.findById(jimbo.getId());
		
		Long id1 = retrievedBenutzer.get().getId();
		Long id2 = retrievedBenutzer2.get().getId();
		Long id3 = retrievedBenutzer3.get().getId();
		
		assertThat(!(id1.equals(id2)));
		assertThat(!(id2.equals(id3)));
		assertThat(!(id3.equals(id1)));
	}

	@Test
	public void savesAndRetrievesSeveralArticlesWithDifferentIDs(){
		hamster = artRepo.save(hamster);
		kochtopf = artRepo.save(kochtopf);
		Optional<Artikel> retrievedArtikel = artRepo.findById(fahrrad.getId());
		Optional<Artikel> retrievedArtikel2 = artRepo.findById(hamster.getId());
		Optional<Artikel> retrievedArtikel3 = artRepo.findById(kochtopf.getId());

		Long id1 = retrievedArtikel.get().getId();
		Long id2 = retrievedArtikel2.get().getId();
		Long id3 = retrievedArtikel3.get().getId();

		assertThat(!(id1.equals(id2)));
		assertThat(!(id2.equals(id3)));
		assertThat(!(id3.equals(id1)));
	}

	////////////////////////////////////////TESTS FOR CONNECTION BETWEEN OBJECTS////////////////////////////////////////

	@Test
	public void connectsArtikelAndBenutzer(){
		fahrrad.setEigentuemer(testRepo.findById(tom.getId()).get());
		artRepo.save(fahrrad);
		Optional<Artikel> retrievedAtrikel = artRepo.findById(fahrrad.getId());
		assertThat(retrievedAtrikel.get().getEigentuemer().getId()).isEqualTo(tom.getId());
	}	
}
