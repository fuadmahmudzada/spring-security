package untitled7.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import untitled7.model.Loans;

import java.util.List;

@Repository
public interface LoanRepository extends CrudRepository<Loans, Long> {

    List<Loans> findByCustomerIdOrderByStartDtDesc(long customerId);

}