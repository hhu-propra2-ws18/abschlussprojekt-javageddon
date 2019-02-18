package hhu.propra2.javageddon.teils.dataaccess;

        import java.nio.file.Files;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.nio.file.StandardCopyOption;

        import hhu.propra2.javageddon.teils.model.Foto;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoStorage implements FotoStorageInterface{

    private final Path rootLocation = Paths.get("fotos");

    @Autowired
    FotoRepository fotoRepository;

    @Override
    public void store(MultipartFile file){
        try {
            long nextId = 0;
            String newName = "";
            if(fotoRepository.findTopById() != null) {
                Foto last = fotoRepository.findTopById();
                nextId = last.getId() + 1;
                newName += nextId;
            }else {
                newName += "0";
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(newName), StandardCopyOption.REPLACE_EXISTING);
            //Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }
}
