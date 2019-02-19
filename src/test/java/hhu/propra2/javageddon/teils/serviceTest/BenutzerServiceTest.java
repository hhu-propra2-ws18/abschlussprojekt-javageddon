package hhu.propra2.javageddon.teils.serviceTest;

import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.services.BenutzerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
@SpringBootTest
public class BenutzerServiceTest {

    BenutzerRepository benRepo;

    @Autowired
    BenutzerService bService = new BenutzerService(benRepo);

    Benutzer richtigeEingabe = Benutzer.builder().name("Otto").email("otto@otto.de").build();
    Benutzer duplikatName = Benutzer.builder().name("Otto").email("otto1@otto.de").build();
    Benutzer duplikatEmail = Benutzer.builder().name("Heido").email("otto@otto.de").build();
    Benutzer leererName = Benutzer.builder().name("").email("1@1.de").build();
    Benutzer leereEmail = Benutzer.builder().name("Anna").email("").build();

    @Test
    public void checkEmptyName() {
        assertThat(bService.isEmptyName(leererName)).isEqualTo(true);
    }

    @Test
    public void checkEmptyEmail() {
        assertThat(bService.isEmptyEmail(leereEmail)).isEqualTo(true);
    }

    @Test
    public void checkDuplikatNameButNotEmail() {
        bService.addBenutzer(richtigeEingabe);
        assertThat(bService.isDuplicateName(duplikatName)).isEqualTo(true);
        assertThat(bService.isDuplicateEmail(duplikatName)).isEqualTo(false);
    }

    @Test
    public void checkDuplikatEmailButNotName() {
        bService.addBenutzer(richtigeEingabe);
        assertThat(bService.isDuplicateName(duplikatEmail)).isEqualTo(false);
        assertThat(bService.isDuplicateEmail(duplikatEmail)).isEqualTo(true);
    }

    @Test
    public void checkDuplikatEmailAndName() {
        bService.addBenutzer(richtigeEingabe);
        assertThat(bService.isDuplicateName(richtigeEingabe)).isEqualTo(true);
        assertThat(bService.isDuplicateEmail(richtigeEingabe)).isEqualTo(true);
    }

    @Test
    public void hasCorrectInput() {
        bService.addBenutzer(richtigeEingabe);
        Benutzer zweiterBenutzer = Benutzer.builder().name("tim").email("tim@tim.com").build();
        assertThat(bService.hasIncorrectInput(zweiterBenutzer)).isEqualTo(false);
    }

    @Test
    public void hasIncorrectInput() {
        bService.addBenutzer(richtigeEingabe);
        assertThat(bService.hasIncorrectInput(duplikatName)).isEqualTo(true);
        assertThat(bService.hasIncorrectInput(duplikatEmail)).isEqualTo(true);
        assertThat(bService.hasIncorrectInput(leererName)).isEqualTo(true);
        assertThat(bService.hasIncorrectInput(leereEmail)).isEqualTo(true);
    }
}
