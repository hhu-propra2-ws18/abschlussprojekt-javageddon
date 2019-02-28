package hhu.propra2.javageddon.teils.services;

import hhu.propra2.javageddon.teils.dataaccess.VerkaufArtikelRepository;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.VerkaufArtikel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VerkaufArtikelService {
    private VerkaufArtikelRepository alleVerkaufArtikel;

    public VerkaufArtikelService(VerkaufArtikelRepository artikel) {
        this.alleVerkaufArtikel = artikel;
    }

    public List<VerkaufArtikel> findAllArtikel() {
        return alleVerkaufArtikel.findAll();
    }

    public VerkaufArtikel findArtikelById(long id) {
        return alleVerkaufArtikel.findById(id);
    }

    public List<VerkaufArtikel> findArtikelByEigentuemer(Benutzer eigentuemer) {
        return alleVerkaufArtikel.findByEigentuemer(eigentuemer);
    }

    public VerkaufArtikel addArtikel(VerkaufArtikel a) {
        return alleVerkaufArtikel.save(a);
    }

    public void deleteArtikel(VerkaufArtikel a){
        alleVerkaufArtikel.delete(a);
    }

    public VerkaufArtikel updateFotosArtikel(VerkaufArtikel artikel, ArrayList<String> fotos) {
        artikel.setFotos(fotos);
        return alleVerkaufArtikel.save(artikel);
    }
}