package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Benutzer {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @NotEmpty(message = "Sie müssen einen Benutzernamen angeben")
    private String name;
    
    @NotNull
    @NotEmpty(message = "Sie müssen eine Email-Adresse angeben")
    private String email;
    private String password;

    @NotNull
    @NotEmpty
    @Builder.Default
    private String rolle = "ROLE_USER";

    public String getPassword() {
        return password;
    }
}
