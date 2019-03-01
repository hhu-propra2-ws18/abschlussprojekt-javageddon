package hhu.propra2.javageddon.teils.services;

import hhu.propra2.javageddon.teils.dataaccess.VerkaufRepository;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Verkauf;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerkaufService {
    private VerkaufRepository alleVerkaeufe;

    public VerkaufService(VerkaufRepository verkaeufe){
        this.alleVerkaeufe = verkaeufe;
    }

    public Verkauf addVerkauf(Verkauf v) {
        return alleVerkaeufe.save(v);
    }

    public void deleteVerkauf(Verkauf v){
        alleVerkaeufe.delete(v);
    }

    public List<Verkauf> findVerkaufByKaeufer(Benutzer b){
        return alleVerkaeufe.findByKaeufer(b);
    }

    public Verkauf findVerkaufById(long id) {
        return  alleVerkaeufe.findById(id);
    }

    public List<Verkauf> findVerkaufByArtikelEigentuemerAndNichtBearbeitet(Benutzer b) {
        return alleVerkaeufe.findByArtikelEigentuemerAndBearbeitet(b, false);
    }

    public boolean hasEnoughMoney(Verkauf v, int guthaben){
        int gesamtKosten = v.getArtikel().getVerkaufsPreis();

        if(gesamtKosten <= guthaben){
            return true;
        } else {
            return false;
        }
    }

}
