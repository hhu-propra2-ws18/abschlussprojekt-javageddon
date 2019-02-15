package hhu.propra2.javageddon.teils.dataaccess;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FotoStorageInterface {
    public void store(MultipartFile file);
//    public Resource loadFile(String filename);
//    public void deleteAll();
//    public void init();
//    public Stream<Path> loadFiles();
}
