package hhu.propra2.javageddon.teils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProPayUser {

    private String account;
    private double amount;

    private List<Reservations> reservations;

    public double getAmount() {
        return amount;
    }

    public void addReservation(Reservations res){

        reservations.add(res);
    }

    public double getVerfuegbaresGuthaben(){
        double verfuegbaresGeld = amount;
        for (Reservations res: reservations) {
            verfuegbaresGeld -= res.getAmount();
        }
        return verfuegbaresGeld;
    }

    public String getAccount(){return account;}
}
