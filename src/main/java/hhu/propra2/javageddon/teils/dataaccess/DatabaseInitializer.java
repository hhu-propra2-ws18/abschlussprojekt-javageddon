package hhu.propra2.javageddon.teils.dataaccess;

import com.github.javafaker.Faker;
import hhu.propra2.javageddon.teils.model.Adresse;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Reservierung;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    BenutzerRepository benutzer;

    @Autowired
    ArtikelRepository artikel;
    
    @Autowired
    ReservierungRepository reservierung;

    @Autowired
    BeschwerdeRepository beschwerden;

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        final Faker faker = new Faker(Locale.GERMAN);


        IntStream.range(0, 10).mapToObj(value -> {
            final Benutzer b = new Benutzer();

            b.setName(faker.funnyName().name());
            b.setEmail(faker.funnyName().name());
            b.setRolle("ROLE_USER");

            return b;
        }).collect(Collectors.collectingAndThen(
                Collectors.toList(),
                this.benutzer::saveAll));

        final Benutzer testnutzer = new Benutzer();
        testnutzer.setName("marvin");
        testnutzer.setPassword("istDerBeste");
        testnutzer.setEmail("test@dunervst.com");
        testnutzer.setRolle("ROLE_USER");
        benutzer.save(testnutzer);

        final Benutzer testnutzerAD = new Benutzer();
        testnutzerAD.setName("flo");
        testnutzerAD.setPassword("istAdmin");
        testnutzerAD.setEmail("test@marvinnervt.com");
        testnutzerAD.setRolle("ROLE_ADMIN");
        benutzer.save(testnutzerAD);


        IntStream.range(0,10).mapToObj(value -> {
            final Artikel a = new Artikel();

            List<Benutzer> alleBenutzer = benutzer.getAllByIdIsNotNull();

            int anzahlBenutzer = alleBenutzer.size();
            int zufaelligerBenutzer = (int) (Math.random()*anzahlBenutzer);

            a.setEigentuemer(alleBenutzer.get(zufaelligerBenutzer));

            ArrayList<String> fotos = new ArrayList<String>();
            a.setFotos(fotos);

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

            Adresse adtemp = new Adresse();
            adtemp.setHausnummer("" + ((int)(Math.random() * 100)));
            adtemp.setOrt(faker.gameOfThrones().city());
            adtemp.setPlz((int)(Math.random() * 10000));
            adtemp.setStrasse(faker.gameOfThrones().house());
            a.setStandort(adtemp);

            return a;
        }).collect(Collectors.collectingAndThen(
                Collectors.toList(),
                this.artikel::saveAll));
    
	    IntStream.range(0,30).mapToObj(value -> {
	        final Reservierung r = new Reservierung();
	
	        List<Benutzer> alleBenutzer = benutzer.getAllByIdIsNotNull();
	        List<Artikel> alleArtikel = (List<Artikel>) artikel.findAll();
	
	        int anzahlBenutzer = alleBenutzer.size();
	        int zufaelligerBenutzer = (int) (Math.random()*anzahlBenutzer);
	        
	        int anzahlArtikel = alleArtikel.size();
	        int zufaelligerArtikel = (int) (Math.random()*anzahlArtikel);
	
	        r.setLeihender(alleBenutzer.get(zufaelligerBenutzer));
	        r.setArtikel(alleArtikel.get(zufaelligerArtikel));

            LocalDate startDay = LocalDate.now().plusDays(faker.number().numberBetween(-30,30));
            r.setStart(startDay);
            r.setEnde(startDay.plusDays(faker.number().numberBetween(2, 22)));
            if(Math.random() < 0.5) {
                r.setBearbeitet(true);
                if(Math.random() < 0.5) {
                    r.setAkzeptiert(true);
                }else {
                    r.setAkzeptiert(false);
                }
            }else {
                r.setBearbeitet(false);
            }
	
	
	        return r;
	    }).collect(Collectors.collectingAndThen(
	            Collectors.toList(),
	            this.reservierung::saveAll));
    }
}

