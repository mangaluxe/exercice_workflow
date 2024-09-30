package org.example.etudiant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Controller
public class UploadController {

    // ========== Propriétés ==========

    private String location = "src/main/resources/static/assets/img/up/";


    // ========== Constructeur ==========


    // ========== Méthodes ==========

    @RequestMapping("/upload")
    public String upload() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String uploadPost(@RequestParam("image") MultipartFile image) throws IOException {
        Path destinationFile = Paths.get(location).resolve(image.getOriginalFilename()).toAbsolutePath();

        InputStream inputStream = image.getInputStream();

        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);

        return "redirect:/upload";
    }

}
