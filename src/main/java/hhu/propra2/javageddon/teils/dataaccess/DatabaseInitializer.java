package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.User;
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
    UserRepository users;

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        final Faker faker = new Faker(Locale.GERMAN);

        IntStream.range(0,10).mapToObj(value -> {
            final User u = new User();

            u.setName(faker.funnyName().name);
            u.setEmail(faker.funnyName().name);

            return u;
        }).collect(Collectors.collectingAndThen(
                Collectors.toList(),
                this.users::saveAll));
    }

}
