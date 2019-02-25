package hhu.propra2.javageddon.teils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Reservations {
    private int amount;
    private int id;
}
