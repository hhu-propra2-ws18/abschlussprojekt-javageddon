package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservierung {
    @Id
    @GeneratedValue
    private long id;

    private Date start;

    private Date ende;

    @ManyToOne
    @JoinColumn(name = "RESERVIERUNG_BENUTZER_ID")
    private Benutzer leihender;

    @ManyToOne
    @JoinColumn(name = "RESERVIERUNG_ARTIKEL_ID")
    private Artikel artikel;

    private Boolean bearbeitet;

    private Boolean akzeptiert;
}
