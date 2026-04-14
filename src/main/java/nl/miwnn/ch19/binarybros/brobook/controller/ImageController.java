package nl.miwnn.ch19.binarybros.brobook.controller;

/*
 * @author Mart Stukje
 * !! Doel van programma
 * */

import nl.miwnn.ch19.binarybros.brobook.model.Image;
import nl.miwnn.ch19.binarybros.brobook.repository.ImageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/image")
public class ImageController {

    private final ImageRepository imageRepository;

    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> showImage(@PathVariable Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Afbeelding niet gevonden"));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(image.getData());
    }
}
