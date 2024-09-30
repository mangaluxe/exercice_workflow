package org.example.etudiant.controller;

import jakarta.validation.Valid;
import org.example.etudiant.dao.EtudiantRepository;
import org.example.etudiant.entity.Etudiant;
import org.example.etudiant.service.EtudiantService;
import org.example.etudiant.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@Controller
public class EtudiantController {

    // ========== Propriétés ==========

    private final EtudiantService etudiantService; // Service pour gérer les opérations sur les étudiants
    private final LoginService loginService;

    private String location = "src/main/resources/static/assets/img/up/";



    // ========== Constructeur ==========

    /**
     * Constructeur
     */
    public EtudiantController(EtudiantService etudiantService, LoginService loginService) {
        this.etudiantService = etudiantService;
        this.loginService = loginService;
    }


    // ========== Méthodes ==========

    /*
    @RequestMapping : Pour gérer plusieurs types de requêtes HTTP (GET et POST).

    @GetMapping : Uniquement requêtes GET.

    @PostMapping : Uniquement requêtes POST.
    */


    // ----- Read -----

    /**
     * Page d'Accueil
     *
     * @return Nom de la vue pour la page d'accueil
     */
//    @RequestMapping("/") // Marche aussi
    @GetMapping("/") // URL : http://localhost:8080/
    public String home(Model model) {
        model.addAttribute("title", "Page d'Accueil - Gestion des étudiants"); // Pour le title de la page
        return "index"; // Renvoie le nom de la vue "index" pour la page d'accueil
    }


    /**
     * Afficher tous les étudiants
     *
     * @param model : Modèle pour ajouter des attributs à la vue
     * @return Nom de la vue pour afficher tous les étudiants
     */
    @GetMapping("/liste") // URL : http://localhost:8080/liste
    public String liste(Model model) {
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("title", "Liste des étudiants"); // Pour le title de la page
        return "liste"; // Renvoie le nom de la vue "liste" pour afficher tous les étudiants
    }


    /**
     * Afficher les détails d'un étudiant
     *
     * @param id    : Id de l'étudiant
     * @param model : Modèle pour ajouter des attributs à la vue
     * @return Nom de la vue pour afficher les détails d'un étudiant
     */
    @GetMapping("/detail/{id}") // URL exemple : http://localhost:8080/detail/1
    public String detailEtudiant(@PathVariable("id") int id, Model model) { // @PathVariable : pour gérer les variables de modèle dans le mappage d’URI de la requête et les définir comme paramètres de méthode
        Etudiant etudiant = etudiantService.findById(id); // Obtient l'étudiant avec l'identifiant spécifié

        // System.out.println(etudiant);
        if (etudiant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Étudiant non trouvé");
        }

        model.addAttribute("etudiant", etudiant); // Ajoute l'étudiant au modèle

        model.addAttribute("title", "Détail de l'étudiant " + etudiant.getPrenom() + " " + etudiant.getNom()); // Title dynamique avec le prénom et nom de l'étudiant
        return "detail"; // Renvoie le nom de la vue "detail" pour afficher les détails de l'étudiant
    }


    // ----- Create -----

    /**
     * Formulaire d'ajout d'étudiant
     */
    @GetMapping("/formulaire") // URL : http://localhost:8080/formulaire
    public String formulaireAjout(Model model) {

        // ----- Protéger la page par login en session -----
        if (!loginService.isLogged()) {
            return "redirect:/login";
        }
        // ----- -----

        model.addAttribute("etudiant", new Etudiant());
        model.addAttribute("title", "Ajouter un étudiant"); // Pour le title de la page
        model.addAttribute("choixAction", "ajout"); // Pour différencier ajout et update
        return "formulaire";
    }



    /**
     * Ajout d'étudiant
     */
    @PostMapping("/ajout") // Marche aussi: @RequestMapping(value = "/ajout", method = RequestMethod.POST)
    public String ajout(@Valid @ModelAttribute("etudiant") Etudiant etudiant, BindingResult bindingResult, @RequestParam(value = "imageupload", required = false) MultipartFile image) throws IOException {

        System.out.println("Image: " + image.getOriginalFilename());

        System.out.println(etudiant.getNom());

        // --- Validation ---
        if (bindingResult.hasErrors()) { // Vérification des erreurs de validation
            return "formulaire"; // Si erreurs de validation, retour au formulaire
        }
        // --- ---

        // --- Upload image ---

        if (image != null && !image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            Path destinationFile = Paths.get(location).resolve(Paths.get(imageName)).normalize().toAbsolutePath();
            Files.createDirectories(destinationFile.getParent());

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            etudiant.setImage(imageName); // Stocke uniquement le nom du fichier dans l'entité
        }
        else {
            etudiant.setImage(null);
        }
        // --- ---

        System.out.println(etudiant.getImage());

        etudiantService.save(etudiant); // Sauvegarder l'étudiant dans BDD

        return "redirect:/liste"; // Redirection vers la liste après ajout
    }



    // ----- Update -----

    /**
     * Formulaire pour modifier un étudiant
     */
    @GetMapping("/update")
    public String formulaireUpdate(@RequestParam("id") int id, Model model) { // @RequestParam : pour lier les paramètres de requête ou les données de formulaire à un argument de méthode dans un contrôleur.
        Etudiant etudiant = etudiantService.findById(id);
        model.addAttribute("etudiant", etudiant);
        model.addAttribute("title", "Modifier un étudiant"); // Pour le title de la page
        model.addAttribute("choixAction", "update"); // Pour différencier ajout et update

        return "formulaire"; // Même formulaire que pour ajout
    }

    /**
     * Modifie un étudiant
     */
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("etudiant") Etudiant etudiant, BindingResult bindingResult, @RequestParam("imageupload") MultipartFile image) throws IOException {

        // ----- Protéger la page par login en session -----
        if (!loginService.isLogged()) {
            return "redirect:/login";
        }
        // ----- -----

        // --- Validation ---
        if (bindingResult.hasErrors()) { // Vérification des erreurs de validation
            return "formulaire"; // Si erreurs de validation, retour au formulaire
        }
        // --- ---


        // ----- Gestion du changement d'image (si une nouvelle image a été uploadée) -----
        if (image != null && !image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            Path destinationFile = Paths.get(location).resolve(Paths.get(imageName)).normalize().toAbsolutePath();
            Files.createDirectories(destinationFile.getParent());
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            etudiant.setImage(imageName); // Enregistre le nom du fichier dans l'objet Etudiant
        }
        else {
            System.out.println("Aucune image");
        }
        // ----- -----

        etudiantService.save(etudiant); // Modifie l'étudiant existant

        return "redirect:/liste"; // Redirection vers la liste des étudiants après ajout
    }


    // ----- Delete -----

    /**
     * Supprimer un étudiant
     */
    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id) {

        Etudiant etudiant = etudiantService.findById(id);
        if (etudiant != null) {
            etudiantService.delete(etudiant);
        }

        return "redirect:/liste";
    }


    // ----- Recherche -----

    /**
     * Rechercher étudiant par son nom
     */
    @GetMapping("/recherche") // URL : http://localhost:8080/recherche?nom=tom&prenom=nana
    public String recherche(@RequestParam("searchTerm") String searchTerm, Model model) { // @RequestParam : pour lier les paramètres de requête ou les données de formulaire à un argument de méthode dans un contrôleur.

        List<Etudiant> etudiants = etudiantService.search(searchTerm);
        model.addAttribute("etudiants", etudiants);

//        model.addAttribute("etudiants", etudiantService.search(searchTerm)); // Marche aussi

        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("title", "Recherche d'étudiants"); // Pour le title de la page

        return "recherche"; // Renvoie vers la page de résultat
    }


}