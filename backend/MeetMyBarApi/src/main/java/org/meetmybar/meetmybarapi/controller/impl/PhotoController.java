package org.meetmybar.meetmybarapi.controller.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import jakarta.validation.Valid;
import org.meetmybar.meetmybarapi.business.impl.PhotoBusiness;
import org.meetmybar.meetmybarapi.models.dto.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/photos")
@Validated
@Tag(name = "Photos", description = "API de gestion des photos")
@Component
public class PhotoController {
    private final PhotoBusiness photoBusiness;

    @Autowired
    public PhotoController(PhotoBusiness business) {

        this.photoBusiness = business;
    }
    @PostMapping
    public ResponseEntity<?> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "main_photo", required = false) Boolean mainPhoto) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(photoBusiness.savePhoto(file, description, mainPhoto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une photo par son ID")
    public ResponseEntity<Optional<Photo>> getPhoto(@PathVariable int id) {
        return ResponseEntity.ok(photoBusiness.getPhotoById(id));
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "Télécharger une photo par son ID")
    public ResponseEntity<ByteArrayResource> downloadPhoto(@PathVariable int id) {
        return photoBusiness.downloadPhotoById(id);
    }


    @PatchMapping("/{id}")
    @Operation(summary = "Mettre à jour une photo")
    public ResponseEntity<Photo> updatePhoto(
           @PathVariable int id,
            @Valid @RequestBody Photo updateDto) {
       return ResponseEntity.ok(photoBusiness.updatePhoto(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une photo")
    public ResponseEntity<Optional<Photo>> deletePhoto(@PathVariable int id) {
       return ResponseEntity.ok(photoBusiness.deletePhoto(id));
    }
}