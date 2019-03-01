package hhu.propra2.javageddon.teils.modelTest;

import hhu.propra2.javageddon.teils.model.ProPayUser;
import hhu.propra2.javageddon.teils.model.Reservations;
import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class ProPayUserTest {

    Reservations reservations1 = new Reservations().builder().amount(50).id(1).build();
    Reservations reservations2 = new Reservations().builder().amount(30).id(2).build();

    ProPayUser user = new ProPayUser().builder().amount(100).account("user").reservations(new ArrayList<>()).build();

    @Test
    public void fullAmountNoReservations(){
        assertThat(user.getVerfuegbaresGuthaben()).isEqualTo(100);
    }

    @Test
    public void correctAmountTwoReservations(){

        user.addReservation(reservations1);
        user.addReservation(reservations2);
        assertThat(user.getVerfuegbaresGuthaben()).isEqualTo(20);
    }
}
