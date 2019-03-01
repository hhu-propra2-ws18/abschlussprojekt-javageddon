package hhu.propra2.javageddon.teils.modelTest;

import hhu.propra2.javageddon.teils.model.Verkauf;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VerkaufTest {

    Verkauf v1 = new Verkauf();

    @Test
    public void expectsStatusOne(){
        v1.setBearbeitet(false);
        assertThat(v1.ermittleStatus()).isEqualTo(1);
    }

    @Test
    public void expectsStatusTwo(){
        v1.setBearbeitet(true);
        v1.setAkzeptiert(false);
        assertThat(v1.ermittleStatus()).isEqualTo(2);
    }

    @Test
    public void expectsStatusThree(){
        v1.setBearbeitet(true);
        v1.setAkzeptiert(true);
        assertThat(v1.ermittleStatus()).isEqualTo(3);
    }
}
