package hhu.propra2.javageddon.teils.serviceTest;

import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.dataaccess.TransaktionRepository;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Transaktion;
import hhu.propra2.javageddon.teils.services.BenutzerService;
import hhu.propra2.javageddon.teils.services.TransaktionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
@SpringBootTest
public class TransaktionServiceTest {

    @Autowired
    BenutzerRepository benRepo;

    @Autowired
    TransaktionRepository transRepo;

    @Autowired
    BenutzerService bService = new BenutzerService(benRepo);

    TransaktionService tService = new TransaktionService(transRepo);

    Benutzer otto = Benutzer.builder().name("Otto").email("otto@otto.de").build();

    Transaktion gestern = new Transaktion().builder().kontoinhaber(otto).betrag(50).datum(LocalDate.now().minusDays(1)).verwendungszweck("Test").build();
    Transaktion heute = new Transaktion().builder().kontoinhaber(otto).betrag(50).datum(LocalDate.now()).verwendungszweck("Test").build();
    Transaktion morgen = new Transaktion().builder().kontoinhaber(otto).betrag(50).datum(LocalDate.now().plusDays(1)).verwendungszweck("Test").build();


    @Before
    public void testInit() {
        otto = benRepo.save(otto);
        gestern = transRepo.save(gestern);
        heute = transRepo.save(heute);
        morgen = transRepo.save(morgen);
    }

    @After
    public void testDelete() {
        transRepo.deleteAll();
        benRepo.deleteAll();
    }


    @Test
    public void checkOrder() {
        assertThat(tService.findTransaktionenByKontoinhaber(otto)).containsExactly(morgen, heute, gestern);
    }
}
