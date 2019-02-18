package hhu.propra2.javageddon.teils.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
public class Foto {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Artikel artikel;
}
