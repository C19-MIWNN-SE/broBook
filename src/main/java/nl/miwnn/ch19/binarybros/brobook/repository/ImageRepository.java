package nl.miwnn.ch19.binarybros.brobook.repository;

/*
 * @author Mart Stukje
 * */

import nl.miwnn.ch19.binarybros.brobook.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
