package hhu.propra2.javageddon.teils.model;

import com.sun.javafx.beans.IDProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Foto {
    @Id
    @GeneratedValue
    private long id;

    private String link;
}
