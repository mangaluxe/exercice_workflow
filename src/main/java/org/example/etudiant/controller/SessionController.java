package org.example.etudiant.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@Controller
@ResponseBody
public class SessionController {

    // Test de session :

    @Autowired
    private HttpSession httpSession;

    @RequestMapping("/ecrire")
    public void ecrire() {
        httpSession.setAttribute("user", "Haiou");
        httpSession.setAttribute("list", Arrays.asList("Bibi", "Bobo"));
    }

    @RequestMapping("/lire")
    public void lire() {
        httpSession.getAttribute("user").toString();
        System.out.println("user : " + httpSession.getAttribute("user")); // Afficher : Haiou
    }

    @RequestMapping("/effacer")
    public void supprimer() {
//        httpSession.removeAttribute("user");
        httpSession.invalidate(); // Supprimer toutes les sessions

        System.out.println("user : " + httpSession.getAttribute("user")); // Affiche : null
    }

}
