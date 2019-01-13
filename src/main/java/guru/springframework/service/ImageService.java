package guru.springframework.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service for manipulate with images.
 */
public interface ImageService {

    /**
     * Save image file to storage.
     * @param id identifier of file.
     * @param file image file.
     */
    void saveImage(Long id, MultipartFile file);
}
