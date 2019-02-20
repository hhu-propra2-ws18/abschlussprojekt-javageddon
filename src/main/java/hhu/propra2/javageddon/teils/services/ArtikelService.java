package hhu.propra2.javageddon.teils.services;

import java.util.ArrayList;
import java.util.List;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.model.Adresse;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;

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
	
	public Artikel addArtikel(Artikel a) {
		return alleArtikel.save(a);
	}
	
	public Artikel updateFotosArtikel(Artikel artikel, ArrayList<String> fotos) {
		artikel.setFotos(fotos);
		return alleArtikel.save(artikel);
	}
}
