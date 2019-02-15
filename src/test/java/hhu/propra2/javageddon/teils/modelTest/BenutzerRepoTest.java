package hhu.propra2.javageddon.teils.modelTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

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
public class BenutzerRepoTest {

	////////////////REQUIRED OBJECTS/////////////////////////////////////////////////////

	@Autowired
	BenutzerRepository benRepo;
	
	Benutzer tom = Benutzer.builder().name("Tom").email("tom@tomtom.com").build();
	Benutzer herbert = Benutzer.builder().name("Herbert").email("her@bert.com").build();
	Benutzer jimbo = Benutzer.builder().name("Jimbo").email("jjj@j.org").build();
	
	////////////////////////////////////PREPARATIONS////////////////////////////////////////////////

	@Before
	public void testInit() {
		tom = benRepo.save(tom);
	}
	
	@After
	public void testDelete(){
		benRepo.deleteAll();
	}

	/////////////////////////////TESTS FOR RETRIEVAL OF OBJECTS///////////////////////////////////////////////////

	@Test
	public void retrievesBenutzerCorrectly() {
		Optional<Benutzer> retrievedBenutzer = benRepo.findById(tom.getId());
		assertThat(retrievedBenutzer.isPresent());
		assertThat(retrievedBenutzer.get().getName()).isEqualTo(tom.getName());
		assertThat(retrievedBenutzer.get().getEmail()).isEqualTo(tom.getEmail());

	}

	////////////////////////////////////TESTS FOR UPDATING OBJECTS////////////////////////////////////////////

	@Test
	public void updatesBenutzerCorrectly(){
		tom.setEmail("hello@hello.com");
		tom.setName("Thomas");
		benRepo.save(tom);
		Optional<Benutzer> retrievedBenutzer = benRepo.findById(tom.getId());
		assertThat(retrievedBenutzer.isPresent());
		assertThat(retrievedBenutzer.get().getName()).isEqualTo("Thomas");
		assertThat(retrievedBenutzer.get().getEmail()).isEqualTo("hello@hello.com");
		
	}


	////////////////////////TESTS FOR RETRIEVEAL OF SEVERAL DIFFERENT OBJECTS////////////////////////////////////////////

	@Test
	public void savesAndRetrievesSeveralUsersWithDifferentIds(){
		herbert = benRepo.save(herbert);
		jimbo = benRepo.save(jimbo);
		Optional<Benutzer> retrievedBenutzer = benRepo.findById(tom.getId());
		Optional<Benutzer> retrievedBenutzer2 = benRepo.findById(herbert.getId());
		Optional<Benutzer> retrievedBenutzer3 = benRepo.findById(jimbo.getId());
		
		Long id1 = retrievedBenutzer.get().getId();
		Long id2 = retrievedBenutzer2.get().getId();
		Long id3 = retrievedBenutzer3.get().getId();
		
		assertThat(!(id1.equals(id2)));
		assertThat(!(id2.equals(id3)));
		assertThat(!(id3.equals(id1)));
	}

}
