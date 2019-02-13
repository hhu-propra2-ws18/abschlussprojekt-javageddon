package hhu.propra2.javageddon.teils.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Benutzer {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String email;



}