package recipes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "RECIPES")
public class Recipe implements Serializable {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "category")
    private String category;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;
    @ElementCollection
    @Column(name = "ingredients")
    private List<String> ingredients = new ArrayList<>();
    @ElementCollection
    @Column(name = "directions")
    private List<String> directions = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "recipes_users_in_recipe",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    private Set<User> usersInRecipe = new LinkedHashSet<>();

    public Recipe(String name, String description, String category, Date date, List<String> ingredients, List<String> directions) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.date = date;
        this.ingredients = ingredients;
        this.directions = directions;
    }
}
