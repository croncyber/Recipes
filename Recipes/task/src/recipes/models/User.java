package recipes.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
@Entity
@NoArgsConstructor
@Table(name = "USER")
public final class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    @NotNull
    @Email
    @Size(min = 8)
    @Column(name = "EMAIL")
    private String email;
    @NotBlank
    @NotNull
    @Size(min = 8)
    @Column(name = "PASSWORD")
    private String password;

    private String role;

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;

    }
}
