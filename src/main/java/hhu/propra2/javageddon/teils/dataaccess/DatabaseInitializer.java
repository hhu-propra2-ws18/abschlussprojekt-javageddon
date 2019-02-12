package hhu.propra2.javageddon.teils.dataaccess;

import com.github.javafaker.Faker;
import hhu.propra2.javageddon.teils.model.Benutzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    BenutzerRepository benutzer;

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        final Faker faker = new Faker(Locale.GERMAN);

        IntStream.range(0,10).mapToObj(value -> {
            final Benutzer b = new Benutzer();

            b.setName(faker.funnyName().name());
            b.setEmail(faker.funnyName().name());

            return b;
        }).collect(Collectors.collectingAndThen(
                Collectors.toList(),
                this.benutzer::saveAll));
    }

}
