package hhu.propra2.javageddon.teils.dataaccess;

        import java.nio.file.Files;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.nio.file.StandardCopyOption;
        import java.util.List;

        import hhu.propra2.javageddon.teils.model.Foto;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoStorage implements FotoStorageInterface{

    private final Path rootLocation = Paths.get("fotos");
    private int index = 0;

    @Autowired
    FotoRepository fotoRepository;

    @Override
    public void store(MultipartFile file){
        try {
            String newName = "" + index;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(newName), StandardCopyOption.REPLACE_EXISTING);
            //Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            index++;
        } catch (Exception e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }
}
