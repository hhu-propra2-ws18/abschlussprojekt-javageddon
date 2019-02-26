package hhu.propra2.javageddon.teils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProPayUser {

    private String account;
    private double amount;

    private List<Reservations> reservations;

    public double getAmount() {
        return amount;
    }

    public String getAccount(){return account;}
}
