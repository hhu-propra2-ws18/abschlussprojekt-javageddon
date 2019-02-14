package hhu.propra2.javageddon.teils.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
public class Foto {
    @Id
    @GeneratedValue
    private long id;

    private String link;

    @OneToOne
    @JoinColumn(name = "ARTIKEL_FOTO_ID")
    private Artikel artikel;
}
