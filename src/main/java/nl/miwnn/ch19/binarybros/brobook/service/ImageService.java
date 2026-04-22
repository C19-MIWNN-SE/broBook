package nl.miwnn.ch19.binarybros.brobook.service;

/*
 * @author Mart Stukje
 * Handles business logic regarding images in the broBook application
 * */

import nl.miwnn.ch19.binarybros.brobook.model.Image;
import nl.miwnn.ch19.binarybros.brobook.repository.ImageRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image getImage(Long id) {
        return imageRepository.getReferenceById(id);
    }

    public Image saveImage(MultipartFile imageFile) {
        Image image = new Image();
        try {
            image.setData(imageFile.getBytes());
        } catch (IOException ioException) {
            throw new IllegalStateException("Dit bestand kon niet worden opgeslagen", ioException);
        }
        image.setContentType(imageFile.getContentType());
        return imageRepository.save(image);
    }

    public Image saveImageFromResource(String resourcePath) {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        try {
            Image image = new Image();
            image.setData(resource.getContentAsByteArray());
            image.setContentType(Files.probeContentType(resource.getFile().toPath()));
            imageRepository.save(image);
            return image;
        } catch (IOException e) {
            throw new IllegalStateException("Seed afbeelding kon niet worden geladen: " + resourcePath, e);
        }
    }

    public Image saveImageFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.connect();

            Image image = new Image();
            image.setData(connection.getInputStream().readAllBytes());
            image.setContentType(connection.getContentType());
            return imageRepository.save(image);
        } catch (IOException ioException) {
            throw new IllegalStateException("Seed afbeelding kon niet worden opgehaald: " + imageUrl, ioException);
        }
    }
}
