package untitled7.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import untitled7.model.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
}
