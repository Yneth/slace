package ua.yware.slace.service.storage;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init();

    String getContentType(Resource resource);

    void store(String fileName, MultipartFile file);

    default void store(MultipartFile file) {
        this.store(file.getOriginalFilename(), file);
    }

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
