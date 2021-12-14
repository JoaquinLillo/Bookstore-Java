package lib.web.bookstore.repository;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import lib.web.bookstore.entities.Author;
import lib.web.bookstore.exceptions.ErrorService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AuthorDAO {

    @PersistenceContext
    EntityManager em = Persistence.createEntityManagerFactory("webPU").createEntityManager();

    @Transactional
    public void persistAuthor(Author author) throws ErrorService {
        if (author == null) {
            throw new ErrorService("Error: Author is empty");
        }
        em.persist(author);
    }

    @Transactional
    public void releaseAuthor(Author author) {
        em.merge(author);
    }

    @Transactional
    public Author findAuthorName(String authorName) throws ErrorService {
        Author author = new Author();
        try {
            if (authorName == null) {
                throw new ErrorService("Required field: Enter an author's name");
            }
            author = (Author) em.createQuery("select a from Author a WHERE a.name='" + authorName + "'").getSingleResult();
        } catch (NoResultException e) {
            System.out.println("The author wasn't found on the database");
            author = null;
        } catch (NonUniqueResultException ex) {
            throw new ErrorService("The author was already registered on the database");
        }
        return author;
    }

    @Transactional
    public void editAuthor(Author author) {
        em.merge(author);
    }

    @Transactional
    public ArrayList<Author> listAuthors() {
        Collection<Author> authorsCollection = em.createQuery("select a from Author a where a.released=0").getResultList();
        ArrayList<Author> authors = new ArrayList<>(authorsCollection);
        return authors;
    }

    public Author findAuthorId(int id) {
        Author author = em.find(Author.class, id);
        return author;
    }

}
