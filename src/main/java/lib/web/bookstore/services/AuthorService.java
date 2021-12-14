package lib.web.bookstore.services;

import lib.web.bookstore.entities.Author;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import lib.web.bookstore.exceptions.ErrorService;
import lib.web.bookstore.repository.AuthorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

    Scanner leer = new Scanner(System.in).useDelimiter("\n");

    @Autowired
    AuthorDAO adao;

    @Transactional
    public void releaseAuthor(int id) throws ErrorService {
        Author author = findAuthorId(id);
        author.setReleased(true);
        adao.releaseAuthor(author);
    }

    @Transactional
    public void editAuthor(String authorName, int id) throws ErrorService {
        Author author = adao.findAuthorId(id);
        author.setId(id);
        author.setName(authorName);
        adao.editAuthor(author);
    }

    @Transactional
    public Collection<Author> listAuthors() {
        ArrayList<Author> authors = adao.listAuthors();
        return authors;
    }

    @Transactional
    public Author findAuthorName(String authorName) throws ErrorService {
        if (authorName == null) {
            throw new ErrorService("Required field: Enter the author's name");
        }
        Author author = adao.findAuthorName(authorName);
        return author;
    }

    @Transactional
    public Author registerAuthor(String authorName) throws ErrorService {
        Author author = new Author();
        try {
            author.setName(authorName);
            adao.persistAuthor(author);
        } catch (ErrorService ex) {
            throw ex;
        }
        return author;
    }

    public Author findAuthorId(int id) {
        Author author = adao.findAuthorId(id);
        return author;
    }

}
