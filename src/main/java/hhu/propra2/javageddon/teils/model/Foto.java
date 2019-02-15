package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
