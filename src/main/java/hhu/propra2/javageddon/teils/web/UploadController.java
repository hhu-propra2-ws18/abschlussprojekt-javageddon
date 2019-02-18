package hhu.propra2.javageddon.teils.web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import hhu.propra2.javageddon.teils.dataaccess.FotoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class UploadController {

    @Autowired
    FotoStorage fotoStorage;

    @GetMapping("/upload")
    public String index() {
        return "fotos_upload";
    }

    @PostMapping("/upload")
    public String uploadMultipartFile(@RequestParam("files") MultipartFile[] files, Model model) {
        List<String> fileNames = null;

        try {
             if(files.length < 11) {        //TODO Make it Variable
                                            // TODO Check for JPG
                 fileNames = Arrays.asList(files)
                         .stream()
                         .map(file -> {
                             fotoStorage.store(file);
                             return file.getOriginalFilename();
                         })
                         .collect(Collectors.toList());
                 model.addAttribute("message", "Fotos uploaded successfully!");
                 model.addAttribute("files", fileNames);
             } else{
                 model.addAttribute("message", "Zu viele Fotos!");
                 model.addAttribute("files", fileNames);
                    }
        } catch (Exception e) {
            model.addAttribute("message", "Something went wrong!");
            model.addAttribute("files", fileNames);
        }

        return "fotos_upload";
    }

}
