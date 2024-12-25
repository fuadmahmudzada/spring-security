package untitled7.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import untitled7.model.AccountTransactions;

import java.util.List;

@Repository
public interface AccountTransactionsRepository extends CrudRepository<AccountTransactions, String> {

    List<AccountTransactions> findByCustomerIdOrderByTransactionDtDesc(long customerId);

}