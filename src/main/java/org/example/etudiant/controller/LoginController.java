package org.example.etudiant.controller;

import org.example.etudiant.entity.Etudiant;
import org.example.etudiant.entity.Utilisateur;
import org.example.etudiant.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginController {

    // ========== Propriétés ==========

    private final LoginService loginService;


    // ========== Constructeur ==========

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    // ========== Méthodes ==========

    // ----- Connexion -----

    @GetMapping("/login")
    public String loginForm(Model model) {

//        model.addAttribute("utilisateur", new Utilisateur());

        return "login-form";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
//    public String login(@RequestParam("username") String username, @RequestParam("password") String password, BindingResult bindingResult, Model model) {

//        // --- Validation ---
//        if (bindingResult.hasErrors()) { // Vérification des erreurs de validation
//            return "login-form"; // Si erreurs de validation, retour au formulaire
//        }
//        // --- ---

        if (loginService.login(username, password)) {
            return "redirect:/admin"; // Redirection vers une page protégée
        }
        return "redirect:/login"; // Redirection en cas d’échec
    }


    // ----- Connecté -----

    @GetMapping("/admin")
    public String protectedPage(Model model) {
        System.out.println("Vous êtes connecté !");

        // ----- Protéger la page par login en session -----
        if (!loginService.isLogged()) {
            return "redirect:/";
        }
        // ----- -----

        return "admin";
    }


    // ----- Déconnexion -----

    @GetMapping("/logout")
    public String logout() {
        loginService.logout();
        return "redirect:/";
    }

}



