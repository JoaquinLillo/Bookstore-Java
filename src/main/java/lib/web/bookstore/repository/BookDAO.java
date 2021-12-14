package lib.web.bookstore.repository;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import lib.web.bookstore.entities.Author;
import lib.web.bookstore.entities.Book;
import lib.web.bookstore.entities.Editorial;
import lib.web.bookstore.exceptions.ErrorService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BookDAO {

    @PersistenceContext
    EntityManager em = Persistence.createEntityManagerFactory("webPU").createEntityManager();

    @Transactional
    public void persistBook(Book book, int author_id, int editorial_id) throws ErrorService {
        if (book == null) {
            throw new ErrorService("Error. The book is empty");
        }                
        Editorial editorial= em.find(Editorial.class, editorial_id);
        Author author = em.find(Author.class, author_id);
        book.setAuthor(author);
        book.setEditorial(editorial);
        em.persist(book);        
    }

    @Transactional
    public void releaseBook(Book book) {       
        em.merge(book);        
    }

    @Transactional
    public void editBook(Book book) {        
        em.merge(book);        
    }

    @Transactional
    public Book findBookISBN(long ISBN) {
        Book book = new Book();
        System.out.println(ISBN);
        try {
            book = (Book) em.createQuery("select b from Book b WHERE b.isbn=:ISBN").setParameter("ISBN", ISBN).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("This ISBN wasn't found in the database");
        }
        return book;
    }

    @Transactional
    public Book findBookTitle(String title) {
        Book book = new Book();
        try {
            book = (Book) em.createQuery("select b from Book b WHERE b.title='" + title + "'").getSingleResult();
        } catch (NoResultException e) {
            System.out.println("The book wasn't found in the database");
        }
        return book;
    }

    @Transactional
    public Collection<Book> findBooksAuthor(int authorId) {
        Collection<Book> books = em.createQuery("select b from Book b where b.author.id=" + authorId).getResultList();
        return books;
    }

    
    @Transactional
    public Collection<Book> findBooksEditorial(int editorialId) {
        Collection<Book> books = em.createQuery("select b from Book b where b.editorial.id=" + editorialId).getResultList();
        return books;
    }

    @Transactional
    public ArrayList<Book> listBooks() {        
        Collection<Book> booksCollection = em.createQuery("select b from Book b where b.released=0").getResultList();
        ArrayList<Book> books = new ArrayList<>(booksCollection);
        return books;
    }
}
