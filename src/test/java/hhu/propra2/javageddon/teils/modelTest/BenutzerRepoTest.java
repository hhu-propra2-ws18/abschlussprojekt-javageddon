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
		Benutzer retrievedBenutzer = benRepo.findById(tom.getId());
		assertThat(retrievedBenutzer).isEqualTo(tom);

	}

	////////////////////////////////////TESTS FOR UPDATING OBJECTS////////////////////////////////////////////

	@Test
	public void updatesBenutzerCorrectly(){
		tom.setEmail("hello@hello.com");
		tom.setName("Thomas");
		benRepo.save(tom);
		Benutzer retrievedBenutzer = benRepo.findById(tom.getId());
		assertThat(retrievedBenutzer).isEqualTo(tom);
		
	}

	/////////////////////////TESTS FOR REPOSITORY METHODS///////////////////////////////////////////////////////////////

	@Test
	public void existsByName(){
		boolean checkTom = benRepo.existsByName(tom.getName());
		assertThat(checkTom).isEqualTo(true);
	}

	@Test
	public void doesNotExistByName(){
		boolean checkHerbert = benRepo.existsByName(herbert.getName());
		assertThat(checkHerbert).isEqualTo(false);
	}

	@Test
	public void existsByEmail(){
		boolean checkTom = benRepo.existsByEmail(tom.getEmail());
		assertThat(checkTom).isEqualTo(true);
	}

	@Test
	public void doesNotExistByEmail(){
		boolean checkHerbert = benRepo.existsByEmail(herbert.getEmail());
		assertThat(checkHerbert).isEqualTo(false);
	}


	////////////////////////TESTS FOR RETRIEVEAL OF SEVERAL DIFFERENT OBJECTS////////////////////////////////////////////

	@Test
	public void savesAndRetrievesSeveralUsersWithDifferentIds(){
		herbert = benRepo.save(herbert);
		jimbo = benRepo.save(jimbo);
		Benutzer retrievedBenutzer = benRepo.findById(tom.getId());
		Benutzer retrievedBenutzer2 = benRepo.findById(herbert.getId());
		Benutzer retrievedBenutzer3 = benRepo.findById(jimbo.getId());
		
		Long id1 = retrievedBenutzer.getId();
		Long id2 = retrievedBenutzer2.getId();
		Long id3 = retrievedBenutzer3.getId();
		
		assertThat(!(id1.equals(id2)));
		assertThat(!(id2.equals(id3)));
		assertThat(!(id3.equals(id1)));
	}

}
