package hhu.propra2.javageddon.teils.dataaccess;

import org.springframework.web.multipart.MultipartFile;

public interface FotoStorageInterface {
    public String store(MultipartFile file);
}
