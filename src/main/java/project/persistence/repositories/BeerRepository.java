package project.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.persistence.entities.Beer;

import java.util.List;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Long> {

    Beer findByName(String name);

    List<Beer> findAll();
}
