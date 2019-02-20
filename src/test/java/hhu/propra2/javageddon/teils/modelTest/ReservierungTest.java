package hhu.propra2.javageddon.teils.modelTest;

import hhu.propra2.javageddon.teils.model.Reservierung;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservierungTest {

    LocalDate start = LocalDate.of(2018, 11, 13);
    LocalDate ende = LocalDate.of(2018, 12, 24);

    Reservierung res = Reservierung.builder().start(start).ende(ende).build();
    Reservierung res2 = Reservierung.builder().start(start).ende(start).build();

    @Test
    public void printsReservierungDateStringCorrectly(){

        String correctString = "13.11.2018 - 24.12.2018";
        assertThat(res.printReservierungsDauer()).isEqualTo(correctString);
    }

    @Test
    public void printsSameReservierungDateStringCorrectly(){

        String correctString = "13.11.2018";
        assertThat(res2.printReservierungsDauer()).isEqualTo(correctString);
    }
}