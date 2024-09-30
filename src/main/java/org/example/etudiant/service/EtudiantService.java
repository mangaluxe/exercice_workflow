package org.example.etudiant.service;

import org.example.etudiant.dao.EtudiantRepository;
import org.example.etudiant.entity.Etudiant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service // Annotation de Spring qui marque cette classe comme un service
public class EtudiantService {

    // ========== Propriétés ==========

    private final EtudiantRepository etudiantRepository;


    // ========== Constructeur ==========

    @Autowired
    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }


    // ========== Méthodes ==========

    // ----- Read -----

    /**
     * Obtenir la liste de tous les étudiants
     * @return List<Etudiant>
     */
    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }


    /**
     * Obtenir un étudiant par son identifiant
     * @param id Identifiant de l'étudiant
     * @return Etudiant
     */
    public Etudiant findById(int id) {
        return etudiantRepository.findById(id).orElse(null);
    }


    // ----- Create / Update -----

    /**
     * Ajouter ou modifier un nouvel étudiant
     *  @param etudiant : L'étudiant à ajouter ou modifier
     */
    public Etudiant save(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }


    // ----- Delete -----

    /**
     * Supprimer un étudiant
     * @param etudiant : L'étudiant à supprimer
     */
    public void delete(Etudiant etudiant) {
        etudiantRepository.delete(etudiant);
    }


    // ----- Recherche -----

    /**
     * Recherche des étudiants par leur nom ou prenom
     */
    public List<Etudiant> search(String searchTerm) {
        return etudiantRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(searchTerm, searchTerm);
    }




}
