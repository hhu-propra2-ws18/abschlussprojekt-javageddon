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
    private int index = 0;


    @Override
    public String store(MultipartFile file){
       if (file.isEmpty()){
            return "nichts";
        }
        try {
            String newName = "" + index;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(newName + ".jpg"), StandardCopyOption.REPLACE_EXISTING);
            index++;
            return newName;
        } catch (Exception e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }
}
