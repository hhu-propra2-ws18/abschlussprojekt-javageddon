package hhu.propra2.javageddon.teils.services;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.BeschwerdeRepository;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Beschwerde;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BeschwerdeService {

        private BeschwerdeRepository alleBeschwerden;

        public BeschwerdeService(BeschwerdeRepository beschwerden) {
            this.alleBeschwerden = beschwerden;
        }

        public List<Beschwerde> findAllUnbearbeitetBeschwerden() {
            return alleBeschwerden.findAllByBearbeitet(false);
        }

        public Beschwerde findBeschwerdeById(long id) {
            return alleBeschwerden.findById(id);
        }

        public Beschwerde addBeschwerde(Beschwerde beschwerde) {
            return alleBeschwerden.save(beschwerde);
        }
}
