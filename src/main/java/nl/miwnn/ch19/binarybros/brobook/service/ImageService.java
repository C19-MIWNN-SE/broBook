package nl.miwnn.ch19.binarybros.brobook.service;

/*
 * @author Mart Stukje
 * Handles business logic regarding images in the broBook application
 * */

import nl.miwnn.ch19.binarybros.brobook.model.Image;
import nl.miwnn.ch19.binarybros.brobook.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
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
}
