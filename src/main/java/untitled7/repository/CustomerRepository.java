package untitled7.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import untitled7.model.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
}
