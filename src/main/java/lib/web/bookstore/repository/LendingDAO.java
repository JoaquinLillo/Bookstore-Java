package lib.web.bookstore.repository;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import lib.web.bookstore.entities.Book;
import lib.web.bookstore.entities.Client;
import lib.web.bookstore.entities.Lending;
import lib.web.bookstore.exceptions.ErrorService;
import lib.web.bookstore.services.ClientService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LendingDAO {

    @PersistenceContext
    EntityManager em = Persistence.createEntityManagerFactory("webPU").createEntityManager();
    ClientService cs = new ClientService();

    @Transactional
    public void registerRestore(Lending lending) throws ErrorService {
        if (lending == null) {
            throw new ErrorService("Error. Lending is empty");
        }
        
        for (Book book : lending.getBooks()) {
            book.getLendings().remove(lending);
            book.setCopiesLended(book.getCopiesLended()-1);
            book.setCopiesRemaining();      
        }
        
        
        lending.setReleased(true);
        em.merge(lending);
    }

    @Transactional
    public List<Lending> findClientLendings(String document) {
        List<Lending> lendings = new ArrayList();
        lendings = em.createQuery("select l from Lending l where l.client.document='" + document+"'").getResultList();
        return lendings;
    }

    @Transactional
    public void registerLending(Lending lending, int client_id, List<Long> booksISBN) throws ErrorService {
        Client client = em.find(Client.class, client_id);
        lending.setClient(client);
        client.getLendings().add(lending);
        for (Long long1 : booksISBN) {
            Book book = em.find(Book.class, long1);
            book.getLendings().add(lending);
            book.setCopiesLended(book.getCopiesLended() + 1);
            book.setCopiesRemaining();
            lending.getBooks().add(book);
        }
        em.persist(lending);
    }

    @Transactional
    public Lending findLendingId(int lending_id) {
        Lending lending = em.find(Lending.class, lending_id);
        return lending;
    }

    @Transactional
    public void deleteLending(Lending lending) {
        em.remove(lending);
    }

    @Transactional
    public List<Lending> listLendings() {
        System.out.println(em.isOpen());
        List<Lending> lendings = em.createQuery("select l from Lending l where l.released=0").getResultList();
        return lendings;
    }

    @Transactional
    public void editLending(Lending lending) {
        em.merge(lending);
    }

}
