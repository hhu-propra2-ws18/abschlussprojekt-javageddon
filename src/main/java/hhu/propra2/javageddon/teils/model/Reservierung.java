package hhu.propra2.javageddon.teils.model;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Reservierung {
    @Id
    @GeneratedValue
    private long id;

    private Date start;

    private Date ende;

    @OneToOne
    @JoinColumn(name = "RESERVIERUNG_BENUTZER_ID")
    private Benutzer leihender;

    @OneToOne
    @JoinColumn(name = "RESERVIERUNG_ARTIKEL_ID")
    private Artikel artikel;

    private Boolean bearbeitet;

    private Boolean akzeptiert;
}
