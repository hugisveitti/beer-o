package project.controller;



import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.persistence.entities.Beer;
import project.service.BeerService;

import javax.persistence.EntityManagerFactory;
import java.util.List;


@RestController
public class BeerController {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private BeerService beerService;

    @Autowired
    public BeerController(BeerService beerService){
        this.beerService = beerService;
    }

    @RequestMapping("/beers")
    public List<Beer> getBeers(){
        return beerService.findAll();
    }
}
