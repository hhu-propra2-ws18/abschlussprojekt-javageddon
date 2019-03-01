package hhu.propra2.javageddon.teils.modelTest;

import hhu.propra2.javageddon.teils.model.ProPayUser;
import hhu.propra2.javageddon.teils.model.Reservations;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProPayUserTest {

    Reservations reservations1 = new Reservations().builder().amount(50).id(1).build();
    Reservations reservations2 = new Reservations().builder().amount(30).id(2).build();

    ProPayUser user1 = new ProPayUser().builder().amount(100).build();

    @Test
    public void fullAmountNoReservations(){
        assertThat(user1.getVerfuegbaresGuthaben()).isEqualTo(100);
    }

    @Test
    public void correctAmountTwoReservations(){

        user1.addReservation(reservations1);
        user1.addReservation(reservations2);
        assertThat(user1.getVerfuegbaresGuthaben()).isEqualTo(20);
    }
}
