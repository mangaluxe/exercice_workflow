package org.example.etudiant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.etudiant.validation.MyValid;


@Data // Annotation Lombok qui génère automatiquement les getters/setters, toString...
@AllArgsConstructor // Annotation Lombok qui génère un constructeur avec tous les arguments.
@NoArgsConstructor // Annotation Lombok qui génère un constructeur sans arguments.
@Entity // Indique que cette classe est une entité JPA (correspond à une table dans la base de données)
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indiquent que le champ id est la clé primaire et qu'il sera auto-généré par la base de données (génération automatique de l'ID)
    private int id;

    // @NotNull
    @NotBlank(message = "Champ obligatoire")
    private String nom;

    @NotBlank(message = "Champ obligatoire")
    @Size(min = 3, max = 100, message = "Entre 3 et 100 caractères")
    private String prenom;

    @Min(value = 16, message = "Mini 16 ans")
    @Max(value = 99, message = "Max 99 ans")
    private int age;

    @MyValid(message = "Gros mots interdits") // Validation personnalisée
    @NotBlank(message = "Champ obligatoire")
    @Email(message = "Format d’email : exemple@email.com")
    private String email;

    private String image = null; // Mettre null par défaut, sinon conflit

}