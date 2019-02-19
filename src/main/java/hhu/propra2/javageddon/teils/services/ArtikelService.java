package hhu.propra2.javageddon.teils.services;

import java.util.List;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.model.Artikel;
import org.springframework.stereotype.Service;

@Service
public class ArtikelService {

	private ArtikelRepository alleArtikel;
	
	public ArtikelService(ArtikelRepository artikel) {
		this.alleArtikel = artikel;
	}
	
	public List<Artikel> findAllAktivArtikel() {
		return alleArtikel.findByAktiv(true);
	}
	
	public Artikel findArtikelById(long id) {
		return alleArtikel.findById(id);
	}
}
