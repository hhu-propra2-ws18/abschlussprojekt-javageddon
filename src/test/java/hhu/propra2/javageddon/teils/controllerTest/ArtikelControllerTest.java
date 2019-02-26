package hhu.propra2.javageddon.teils.controllerTest;

import hhu.propra2.javageddon.teils.model.Adresse;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.services.ArtikelService;
import hhu.propra2.javageddon.teils.web.ArtikelController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@RunWith(SpringRunner.class)
@WebMvcTest(ArtikelController.class)
public class ArtikelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArtikelService alleArtikel;

    @Test
    public void erstelleArtikelCorrectly() throws Exception{

        Benutzer tom = Benutzer.builder().name("Tom").email("tom@tomtom.com").build();
        Adresse ad1 = Adresse.builder().hausnummer("5e").strasse("Hauptstrasse").ort("koeln").plz(4000).build();
        Artikel hamster =  Artikel.builder().titel("gwendolin").aktiv(false)
                .eigentuemer(tom).standort(ad1).beschreibung("?").kaution(1).kostenTag(1).build();

        mockMvc.perform(post("/artikel_erstellen"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/fotoupload/" + hamster.getId()));
    }


}
