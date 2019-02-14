package hhu.propra2.javageddon.teils.dataaccess;

import com.github.javafaker.Faker;
import hhu.propra2.javageddon.teils.model.Adresse;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    BenutzerRepository benutzer;

    @Autowired
    ArtikelRepository artikel;

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        final Faker faker = new Faker(Locale.GERMAN);

        IntStream.range(0, 10).mapToObj(value -> {
            final Benutzer b = new Benutzer();

            b.setName(faker.funnyName().name());
            b.setEmail(faker.funnyName().name());

            return b;
        }).collect(Collectors.collectingAndThen(
                Collectors.toList(),
                this.benutzer::saveAll));


        IntStream.range(0,10).mapToObj(value -> {
            final Artikel a = new Artikel();

            a.setTitel(faker.gameOfThrones().character());
            a.setBeschreibung(faker.gameOfThrones().quote());
            a.setKostenTag(faker.number().numberBetween(1,100));
            a.setKaution(faker.number().numberBetween(100,300));
            if(Math.random() < 0.5) {
                a.setAktiv(true);
            }else {
                a.setAktiv(false);
            }
            if(Math.random() < 0.5) {
                a.setVerfuegbar(true);
            }else {
                a.setVerfuegbar(false);
            }
            ArrayList<String> ftemp = new ArrayList();
            ftemp.add("Bild1");
            ftemp.add("Bild2");
            ftemp.add("Bild3");

            Adresse adtemp = new Adresse();
            adtemp.setHausnummer(faker.number().digit());
            adtemp.setOrt(faker.gameOfThrones().city());
            adtemp.setPlz(faker.number().randomDigit());
            adtemp.setStrasse(faker.gameOfThrones().house());
            a.setAdresse(adtemp);

            return a;
        }).collect(Collectors.collectingAndThen(
                Collectors.toList(),
                this.artikel::saveAll));
    }
}

