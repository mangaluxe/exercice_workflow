package org.example.etudiant.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class MyValidConstraintValidator implements ConstraintValidator<MyValid, String> {

    private static final List<String> BAD_WORDS = Arrays.asList("connar", "pute", "fuck");

//    @Override
//    public void initialize(MyValid constraintAnnotation) {
//        // Pas besoin d'initialiser
//    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) {
            return true; // Vous pouvez retourner false si vous voulez que null soit invalidé
        }

        // Vérifier si le texte contient l'un des mots interdits
        for (String badWord : BAD_WORDS) {
            if (s.toLowerCase().contains(badWord)) {
                return false; // Mots interdits trouvés
            }
        }

        return true; // Pas de mots interdits trouvés
    }
}
