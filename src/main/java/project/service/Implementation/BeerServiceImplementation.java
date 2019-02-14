package project.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.Beer;
import project.persistence.repositories.BeerRepository;
import project.service.BeerService;

import java.util.List;

@Service
public class BeerServiceImplementation implements BeerService {

    private BeerRepository beerRepository;

    @Autowired
    public BeerServiceImplementation(BeerRepository beerRepository){
        this.beerRepository = beerRepository;
    }


    public List<Beer> findAll(){
        return beerRepository.findAll();
    }

    public Beer findByName(String name){
        return beerRepository.findByName(name);
    }
}
