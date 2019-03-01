package hhu.propra2.javageddon.teils.dataaccess;

import org.springframework.web.multipart.MultipartFile;

public interface FotoStorageInterface {
     String store(MultipartFile file);
}
