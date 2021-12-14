package lib.web.bookstore.repository;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import lib.web.bookstore.entities.Editorial;
import lib.web.bookstore.exceptions.ErrorService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class EditorialDAO {

    @PersistenceContext
    EntityManager em = Persistence.createEntityManagerFactory("webPU").createEntityManager();

    @Transactional
    public void persistEditorial(Editorial editorial) throws ErrorService {
        try {
            if (editorial == null) {
                throw new ErrorService("Error. The editorial is empty");
            }
            em.persist(editorial);
        } catch (ErrorService e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    public Editorial findEditorialId(int editorialId) throws ErrorService {
        Editorial editorial = new Editorial();
        try {
            if (editorialId == 0) {
                throw new ErrorService("Required field: Enter an editorial's name");
            }
            editorial = (Editorial) em.find(Editorial.class, editorialId);
        } catch (NoResultException e) {
            System.out.println("The editorial wasn't found on the database");
            editorial = null;
        } catch (NonUniqueResultException ex) {
            throw new ErrorService("The editorial was already registered on the database");
        }
        return editorial;
    }

    @Transactional
    public void editEditorial(Editorial editorial) {
        em.merge(editorial);
    }

    @Transactional
    public void releaseEditorial(Editorial editorial) {
        em.merge(editorial);
    }

    @Transactional
    public ArrayList<Editorial> listEditorials() {
        Collection<Editorial> editorialsCollection = em.createQuery("select e from Editorial e where e.released=0").getResultList();
        ArrayList<Editorial> editorials = new ArrayList<>(editorialsCollection);
        return editorials;
    }

    @Transactional
    public Editorial findEditorialName(String editorialName) throws ErrorService {
        Editorial editorial = new Editorial();
        try {
            if (editorialName == null) {
                throw new ErrorService("Required field: Enter an editorial's name");
            }
            editorial = (Editorial) em.createQuery("select e from Editorial e WHERE e.name='" + editorialName + "'").getSingleResult();
        } catch (NoResultException e) {
            System.out.println("The editorial wasn't found on the database");
            editorial = null;
        } catch (NonUniqueResultException ex) {
            throw new ErrorService("The editorial was already registered on the database");
        }
        return editorial;
    }
}
