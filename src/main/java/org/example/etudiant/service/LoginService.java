package org.example.etudiant.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    // ========== Propriétés ==========

    private String login_db = "admin"; // Simulation
    private String password_db = "1234"; // Simulation

    @Autowired
    private HttpSession httpSession;


    // ========== Méthodes ==========

    // ----- Connexion -----

    public boolean login(String username, String password) {
        if (username.equals(login_db) && password.equals(password_db)) {
            httpSession.setAttribute("login", "ok");
            return true;
        }
        System.out.println("Erreur Login");
        return false;
    }


    // ----- Connecté -----

    public boolean isLogged() {
        Object isLogged = httpSession.getAttribute("login");
        return isLogged.equals("ok");
    }


    // ----- Déconnexion -----

    public void logout() {
//        httpSession.removeAttribute("login");
        httpSession.invalidate(); // Détruire toutes les sessions
    }

}