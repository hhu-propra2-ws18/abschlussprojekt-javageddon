package hhu.propra2.javageddon.teils.services;

import hhu.propra2.javageddon.teils.dataaccess.TransaktionRepository;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Transaktion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransaktionService {
    private TransaktionRepository alleTransaktionen;

    public TransaktionService(TransaktionRepository tRepo){
        this.alleTransaktionen = tRepo;
    }

    public List<Transaktion> findTransaktionenByKontoinhaber(Benutzer b){
        return alleTransaktionen.findByKontoinhaber(b)
                .stream()
                .sorted((t1, t2) -> t2.getDatum().compareTo(t1.getDatum()))
                .collect(Collectors.toList());
    }

    public Transaktion addTransaktion(Transaktion t) {
        return alleTransaktionen.save(t);
    }
}
