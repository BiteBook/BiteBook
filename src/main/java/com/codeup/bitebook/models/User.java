package com.codeup.bitebook.models;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @Column
    private String otherAllergies;


    @OneToMany(mappedBy = "user")
    private List<MealPlanner> mealPlanners;

    @ManyToMany
    @JoinTable(
            name = "user_diet_styles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "diet_style_id")
    )
    private List<DietStyle> dietaryPreferences;

    @ManyToMany
    @JoinTable(
            name = "user_allergens",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    private List<Allergen> allergyList;



    public User(User copy) {
        id = copy.id; // This line is SUPER important! Many things won't work if it's absent
        email = copy.email;
        username = copy.username;
        password = copy.password;
        dietaryPreferences = copy.dietaryPreferences;
        allergyList = copy.allergyList;
    }


    @Override
    public String toString() {
        return "User id " + id + " username: " + username;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean matchesDietaryNeeds(Recipe recipe) {
        // Check if the user's dietary preferences match the recipe's diet styles
        if (dietaryPreferences != null && !recipe.getDietStyles().containsAll(dietaryPreferences)) {
            return false;
        }

        // Check if the user's allergy list matches the recipe's allergens
        if (allergyList != null && !recipe.getAllergens().containsAll(allergyList)) {
            return false;
        }

        // If the recipe passed all checks, it matches the user's dietary needs
        return true;
    }

}