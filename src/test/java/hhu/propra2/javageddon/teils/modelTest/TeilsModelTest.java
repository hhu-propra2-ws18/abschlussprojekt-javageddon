package hhu.propra2.javageddon.teils.modelTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import hhu.propra2.javageddon.teils.model.Artikel;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.model.Benutzer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeilsModelTest {

	@Autowired
	BenutzerRepository testRepo;
	
	Benutzer tom = Benutzer.builder().name("Tom").email("tom@tomtom.com").build();
	Benutzer herbert = Benutzer.builder().name("Herbert").email("her@bert.com").build();
	Benutzer jimbo = Benutzer.builder().name("Jimbo").email("jjj@j.org").build();

	Artikel fahrrad = Artikel.builder().titel("fahrrad").aktiv(true).eigentuemer(tom).build();
	
	@Before
	public void testInit() {
		tom = testRepo.save(tom);		
	}
	
	@After
	public void testDelete(){
		
		testRepo.deleteAll();
	}
	
	@Test
	public void retrievesBenutzerCorrectly() {
		Optional<Benutzer> retrievedBenutzer = testRepo.findById(tom.getId());
		assertThat(retrievedBenutzer.isPresent());
		assertThat(retrievedBenutzer.get().getName()).isEqualTo(tom.getName());
		assertThat(retrievedBenutzer.get().getEmail()).isEqualTo(tom.getEmail());

	}

	@Test
	public void retrievesArtikelCorrectly(){

	}
	
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
	public void savesAndRetrievesSeveralPersonsWithDifferentIds(){
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


	
}
