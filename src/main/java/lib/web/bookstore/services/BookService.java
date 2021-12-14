package lib.web.bookstore.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import lib.web.bookstore.entities.Author;
import lib.web.bookstore.entities.Book;
import lib.web.bookstore.entities.Editorial;
import lib.web.bookstore.exceptions.ErrorService;
import lib.web.bookstore.repository.BookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    Scanner leer = new Scanner(System.in).useDelimiter("\n");
    
    @Autowired
    BookDAO bdao;

    @Transactional
    public Book registerBook(long isbn, String title, int year, int copies, int copiesLended, Author author, Editorial editorial) throws ErrorService {
        Book book = new Book();
        BookService bs = new BookService();
        try {
            if (bs.findBookISBN(isbn) != null) {
                throw new ErrorService("The book was already registered");
            }
            book.setIsbn(isbn);
            book.setTitle(title);
            book.setYear(year);
            book.setCopies(copies);
            book.setCopiesRemaining();
            book.setCopiesLended(copiesLended);            
            bdao.persistBook(book, author.getId(), editorial.getId());
        } catch (ErrorService ex) {
            throw ex;
        }
        return book;
    }

    @Transactional
    public void releaseBook(long isbn) {
        Book book = bdao.findBookISBN(isbn);
        book.setReleased(true);
        bdao.releaseBook(book);
    }

    @Transactional
    public void editBook(long isbn, String title, int year, int copies, int copiesLended, Author author, Editorial editorial) throws ErrorService {
        Book book = bdao.findBookISBN(isbn);
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setYear(year);
        book.setCopies(copies);        
        book.setCopiesLended(copiesLended);
        book.setCopiesRemaining();
        book.setAuthor(author);
        book.setEditorial(editorial);
        bdao.editBook(book);
    }

    @Transactional
    public Book findBookISBN(long ISBN) throws ErrorService {
        if (ISBN == 0) {
            throw new ErrorService("Required field: Enter the book's ISBN");
        }
        Book book = new Book();
        book = bdao.findBookISBN(ISBN);
        return book;
    }

    @Transactional
    public Book findBookTitle(String title) throws ErrorService {
        Book book = new Book();
        if (title == null) {
            throw new ErrorService("Required field: Enter the book's title");
        }
        book = bdao.findBookTitle(title);
        return book;
    }

    @Transactional
    public Collection<Book> findBooksAuthor(int authorId) {
        Collection<Book> books = bdao.findBooksAuthor(authorId);
        return books;
    }

    @Transactional
    public Collection<Book> findBooksEditorial(int id) {
        Collection<Book> books = bdao.findBooksEditorial(id);
        return books;
    }

    @Transactional
    public ArrayList<Book> listBooks() {
        ArrayList<Book> books = bdao.listBooks();
        return books;

    }

}
