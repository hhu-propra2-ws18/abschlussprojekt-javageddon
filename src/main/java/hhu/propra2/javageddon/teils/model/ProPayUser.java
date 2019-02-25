package hhu.propra2.javageddon.teils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ProPayUser {

    private String account;
    private int ammount;

    private Reservations reservations;

    public int getAmmount() {
        return ammount;
    }
}
