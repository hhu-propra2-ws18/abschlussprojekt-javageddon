package hhu.propra2.javageddon.teils.web;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.FotoStorage;
import hhu.propra2.javageddon.teils.dataaccess.VerkaufArtikelRepository;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.VerkaufArtikel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class UploadController {

    @Autowired
    FotoStorage fotoStorage;

    @Autowired
    private ArtikelRepository alleArtikel;

    @Autowired
    private VerkaufArtikelRepository alleVerkaufArtikel;

    @GetMapping("/fotoupload/{id}")
    public String index() {
        return "fotos_upload";
    }

    @PostMapping("/fotoupload/{id}")
    public String uploadMultipartFile(@RequestParam("files") MultipartFile[] files, Model model, @PathVariable long id) {
        ArrayList<String> renamedFiles = fotoSpeichern(files, model);
        Artikel artikel = alleArtikel.findById(id);
        artikel.setFotos(renamedFiles);
        alleArtikel.save(artikel);

        return "redirect:/details/?id=" + id;
    }

    @GetMapping("/verkauf/fotoupload/{id}")
    public String verkauf_index() {
        return "fotos_upload";
    }

    @PostMapping("/verkauf/fotoupload/{id}")
    public String uploadMultipartFileVerkauf(@RequestParam("files") MultipartFile[] files, Model model, @PathVariable long id) {
        ArrayList<String> renamedFiles = fotoSpeichern(files, model);
        VerkaufArtikel artikel = alleVerkaufArtikel.findById(id);
        artikel.setFotos(renamedFiles);
        alleVerkaufArtikel.save(artikel);

        return "redirect:/verkauf/details/?id=" + id;
    }

    private ArrayList<String> fotoSpeichern(@RequestParam("files") MultipartFile[] files, Model model) {
        List<String> fileNames = null;
        ArrayList<String> renamedFiles = new ArrayList<String>();

        try {
            if(files.length < 11) {        //TODO Make it Variable
                // TODO Check for JPG
                fileNames = Arrays.asList(files)
                        .stream()
                        .map(file -> {
                            String newName = fotoStorage.store(file);
                            if(newName.equals("nichts") == false) {
                                renamedFiles.add(newName);
                            }
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
        return renamedFiles;
    }


}
