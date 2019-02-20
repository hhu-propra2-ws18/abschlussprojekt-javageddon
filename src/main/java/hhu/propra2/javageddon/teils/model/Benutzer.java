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
    @NotEmpty
    private String name;
    
    @NotNull
    @NotEmpty
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
