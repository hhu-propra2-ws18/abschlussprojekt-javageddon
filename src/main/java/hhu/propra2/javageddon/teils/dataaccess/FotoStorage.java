package hhu.propra2.javageddon.teils.dataaccess;

        import java.nio.file.Files;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.nio.file.StandardCopyOption;
        import org.springframework.stereotype.Service;
        import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoStorage implements FotoStorageInterface{

    private final Path rootLocation = Paths.get("fotos");

    @Override
    public void store(MultipartFile file){
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }
}
