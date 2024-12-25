package untitled7.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import untitled7.model.Cards;

import java.util.List;

@Repository
public interface CardsRepository extends CrudRepository<Cards, Long> {

    List<Cards> findByCustomerId(long customerId);

}