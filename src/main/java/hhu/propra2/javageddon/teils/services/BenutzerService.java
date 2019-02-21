package hhu.propra2.javageddon.teils.services;

import org.springframework.stereotype.Service;

import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.model.Benutzer;

@Service
public class BenutzerService {
	
	private BenutzerRepository alleBenutzer;
	
	public BenutzerService(BenutzerRepository benutzer) {
		this.alleBenutzer = benutzer;
	}
	
	public void addBenutzer(Benutzer b) {
		b.setRolle("ROLE_USER");
		alleBenutzer.save(b);
	}
	
	public Benutzer findBenutzerById(long id) {
		return alleBenutzer.findById(id);
	}
	
	public boolean isDuplicateName(Benutzer b) {
		return alleBenutzer.existsByName(b.getName());
	}
	
	public boolean isDuplicateEmail(Benutzer b) {
		return alleBenutzer.existsByEmail(b.getEmail());
	}
	
	public boolean isEmptyName(Benutzer b) {
		return b.getName().equals("");
	}

	public boolean isEmptyPassword(Benutzer b) { return b.getPassword().equals("");	}

	public boolean isEmptyEmail(Benutzer b) {
		return b.getEmail().equals("");
	}
	
	public boolean hasIncorrectInput(Benutzer b) {
        return isDuplicateEmail(b) || isDuplicateName(b) ||
                isEmptyEmail(b) || isEmptyName(b);
    }

    public Long getIdByName(String username) {
	    return alleBenutzer.findByName(username).getId();
    }
}
