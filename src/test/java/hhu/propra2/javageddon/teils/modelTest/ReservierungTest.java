package hhu.propra2.javageddon.teils.modelTest;

import hhu.propra2.javageddon.teils.model.Reservierung;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservierungTest {

    LocalDate start = LocalDate.of(2018, 11, 13);
    LocalDate ende = LocalDate.of(2018, 12, 24);
    LocalDate zukunftStart = LocalDate.of(2019, 03, 11);
    LocalDate zukunftEnde = LocalDate.of(2019, 04, 11);

    Reservierung res = Reservierung.builder().start(start).ende(ende).bearbeitet(false)
            .akzeptiert(false).zurueckerhalten(false).zurueckgegeben(false).build();
    Reservierung res2 = Reservierung.builder().start(start).ende(start).build();
    Reservierung res3 = Reservierung.builder().start(start).ende(zukunftEnde).bearbeitet(false)
            .akzeptiert(false).zurueckerhalten(false).zurueckgegeben(false).build();

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
    
    @Test
    public void printsStartCorrectly() {
        String correctString = "13.11.2018";
        assertThat(res.printFormattedStart()).isEqualTo(correctString);
    }
    
    @Test
    public void printsEndeCorrectly() {
        String correctString = "24.12.2018";
        assertThat(res.printFormattedEnde()).isEqualTo(correctString);
    }

    @Test
    public void dayisAtStart() {
        assertThat(res.containsDate(start)).isEqualTo(true);
    }

    @Test
    public void dayisAtEnd() {
        assertThat(res.containsDate(ende)).isEqualTo(true);
    }

    @Test
    public void dayisBetween() {
        LocalDate checkDay = LocalDate.of(2018, 11,20);
        assertThat(res.containsDate(checkDay)).isEqualTo(true);
    }

    @Test
    public void dayisNotBetween() {
        LocalDate checkDay = LocalDate.of(2019, 11,20);
        assertThat(res.containsDate(checkDay)).isEqualTo(false);
    }

    @Test
    public void durationOneDay() {
        assertThat(res2.calculateReservierungsLength()).isEqualTo(1);
    }

    @Test
    public void duration42Day() {
        assertThat(res.calculateReservierungsLength()).isEqualTo(42);
    }

    @Test
    public void expectsStatusOne(){

        res3.setBearbeitet(false);
        res3.setStart(zukunftStart);
        assertThat(res3.ermittleStatus()).isEqualTo(1);
    }

    @Test
    public void expectsStatusTwo(){

        res3.setBearbeitet(true);
        res3.setAkzeptiert(false);
        assertThat(res3.ermittleStatus()).isEqualTo(2);
    }

    @Test
    public void expectsStatusThree(){

        res3.setZurueckerhalten(false);
        res3.setBearbeitet(true);
        res3.setAkzeptiert(true);
        res3.setStart(zukunftStart);
        assertThat(res3.ermittleStatus()).isEqualTo(3);
    }

    @Test
    public void expectsStatusFour(){

        res3.setZurueckerhalten(false);
        res3.setBearbeitet(true);
        res3.setAkzeptiert(true);
        res3.setStart(start);
        assertThat(res3.ermittleStatus()).isEqualTo(4);
    }

    @Test
    public void expectsStatusFive(){

        res3.setZurueckerhalten(true);
        res3.setBearbeitet(true);
        res3.setAkzeptiert(true);
        assertThat(res3.ermittleStatus()).isEqualTo(5);
    }

    @Test
    public void expectsStatusSix(){

        res3.setZurueckgegeben(false);
        res3.setBearbeitet(true);
        res3.setAkzeptiert(true);
        res3.setEnde(ende);
        assertThat(res3.ermittleStatus()).isEqualTo(6);
    }

    @Test
    public void expectsStatusSeven(){

        res3.setBearbeitet(false);
        res3.setEnde(ende);
        res3.setStart(start);
        assertThat(res3.ermittleStatus()).isEqualTo(7);
    }








}
