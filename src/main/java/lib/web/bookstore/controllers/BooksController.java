package lib.web.bookstore.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import lib.web.bookstore.entities.Author;
import lib.web.bookstore.entities.Book;
import lib.web.bookstore.entities.Client;
import lib.web.bookstore.entities.Editorial;
import lib.web.bookstore.exceptions.ErrorService;
import lib.web.bookstore.services.AuthorService;
import lib.web.bookstore.services.BookService;
import lib.web.bookstore.services.EditorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAuthority('USER')")
@RequestMapping("/books")
public class BooksController {
    
    @Autowired
    private BookService bs;
    
    @Autowired
    private AuthorService as;
    
    @Autowired
    private EditorialService es;
    
    @GetMapping("")
    public String books(HttpSession session, ModelMap model) {
        
        class EditorialWBooks {
            
            Editorial editorial;
            String books;
            
            public EditorialWBooks(Editorial editorial, String books) {
                this.editorial = editorial;
                this.books = books;
            }
            
            public EditorialWBooks() {
            }
            
            public Editorial getEditorial() {
                return editorial;
            }
            
            public void setEditorial(Editorial editorial) {
                this.editorial = editorial;
            }
            
            public String getBooks() {
                return books;
            }
            
            public void setBooks(String books) {
                this.books = books;
            }
            
        }
        
        class AuthorWBooks {

            Author author;
            String books;
            
            public AuthorWBooks() {
            }
            
            public AuthorWBooks(Author author, String books) {
                this.author = author;
                this.books = books;
            }
            
            public Author getAuthor() {
                return author;
            }
            
            public void setAuthor(Author author) {
                this.author = author;
            }
            
            public String getBooks() {
                return books;
            }
            
            public void setBooks(String books) {
                this.books = books;
            }
            
        }
        
        List<Book> books = bs.listBooks();
        List<Author> authors = new ArrayList<>(as.listAuthors());
        List<Editorial> editorials = new ArrayList<>(es.listEditorials());
        
        List<AuthorWBooks> booksAuthors = new ArrayList<>();
        
        
        // Authors list
        for (Author author : authors) {
            
            AuthorWBooks awb = new AuthorWBooks();
            String aux = "";
            
            List<Book> booksAuth = new ArrayList<>(bs.findBooksAuthor(author.getId()));
            for (Book book : booksAuth) {
                if ("".equals(aux)) {
                    aux = book.getTitle();
                } else {
                    aux = aux + ", " + book.getTitle();
                }
            }
            awb.setAuthor(author);
            awb.setBooks(aux);
            booksAuthors.add(awb);
        }

        // Editorials list
        List<EditorialWBooks> editorialBookses = new ArrayList<>();
        
        for (Editorial editorial : editorials) {
            EditorialWBooks ewb = new EditorialWBooks();
            String aux = "";
            List<Book> booksEdit = new ArrayList<>(bs.findBooksEditorial(editorial.getId()));
            for (Book book : booksEdit) {
                if ("".equals(aux)) {
                    aux = book.getTitle();
                } else {
                    aux = aux + ", " + book.getTitle();
                }
                
            }
            ewb.setBooks(aux);
            ewb.setEditorial(editorial);
            editorialBookses.add(ewb);
        }
        
        model.put("booksByEditorial", editorialBookses);
        model.put("booksByAuthor", booksAuthors);
        model.put("books", books);
        model.addAttribute("authors", authors);
        model.addAttribute("editorials", editorials);
        
        return "books.html";
    }
    
    @GetMapping("/editbook")
    public String editBook(HttpSession session, @RequestParam(required = false) String ISBN, @RequestParam(required = false) String action, ModelMap model) throws ErrorService {
        
        if (action == null) {
            action = "Create";
        }
        
        Book book = new Book();
        
        List<Book> books = bs.listBooks();
        List<Author> authors = new ArrayList<>(as.listAuthors());
        List<Editorial> editorials = new ArrayList<>(es.listEditorials());
        
        model.put("action", action);
        model.put("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("editorials", editorials);
        
        if (ISBN != null) {
            try {
                long BookISBN = Long.parseLong(ISBN);
                book = bs.findBookISBN(BookISBN);
                model.put("action", action);
                model.put("book", book);
            } catch (ErrorService ex) {
                throw ex;
            } finally {
                return "editbook.html";
            }
        } else {
            
            return "editbook.html";
        }
    }
    
    @PostMapping("/update-book")
    public String updateBook(ModelMap model, HttpSession session, @RequestParam long ISBN, @RequestParam String title, @RequestParam int year, @RequestParam int copies, @RequestParam int copiesLended, @RequestParam String author, @RequestParam String editorial) {
        Book book = null;
        
        Client login = (Client) session.getAttribute("usersession");
        if (login == null) {
            return "redirect:/";
        }
        
        try {
            if (ISBN == 0) {
                bs.registerBook(ISBN, title, year, copies, copiesLended, as.findAuthorName(author), es.findEditorialName(editorial));
                return "redirect:/books";
            } else {
                bs.editBook(ISBN, title, year, copies, copiesLended, as.findAuthorName(author), es.findEditorialName(editorial));
                return "redirect:/books";
            }
        } catch (ErrorService e) {
            model.put("action", "Update");
            model.put("error", e.getMessage());
            return "editbook.html";
        }
    }
    
    @PostMapping("/delete-book")
    public String deleteBook(HttpSession session, @RequestParam long ISBN, ModelMap model) {
        Client login = (Client) session.getAttribute("usersession");
        
        List<Book> books = bs.listBooks();
        List<Author> authors = new ArrayList<>(as.listAuthors());
        List<Editorial> editorials = new ArrayList<>(es.listEditorials());
        
        if (login == null) {
            model.put("books", books);
            model.addAttribute("authors", authors);
            model.addAttribute("editorials", editorials);
            return "redirect:/";
        }
        bs.releaseBook(ISBN);
        return "redirect:/books";
    }

    // AUTHORS CONTROLLER 
    @GetMapping("/editauthor")
    public String editAuthor(HttpSession session, @RequestParam(required = false) String authorId, @RequestParam(required = false) String action, ModelMap model) throws ErrorService {
        
        if (action == null) {
            action = "Create";
        }
        
        Author author = new Author();
        
        List<Author> authors = new ArrayList<>(as.listAuthors());
        model.put("action", action);
        model.addAttribute("authors", authors);
        
        if (authorId != null) {
            int id = Integer.parseInt(authorId);
            author = as.findAuthorId(id);
            model.put("action", action);
            model.put("author", author);
            return "editauthor.html";
        } else {
            author.setId(0);
            model.put("author", author);
            return "editauthor.html";
        }
    }
    
    @PostMapping("/update-author")
    public String updateAuthor(ModelMap model, HttpSession session, @RequestParam int authorId, @RequestParam String authorName) {
        
        Client login = (Client) session.getAttribute("usersession");
        if (login == null) {
            return "redirect:/";
        }
        
        try {
            if (authorId == 0) {
                as.registerAuthor(authorName);
                return "redirect:/books";
            } else {
                as.editAuthor(authorName, authorId);
                return "redirect:/books";
            }
        } catch (ErrorService e) {
            model.put("action", "Update");
            model.put("error", e.getMessage());
            return "editauthor.html";
        }
    }
    
    @PostMapping("/delete-author")
    public String deleteAuthor(HttpSession session, @RequestParam int authorId) throws ErrorService {
        Client login = (Client) session.getAttribute("usersession");
        if (login == null) {
            return "redirect:/";
        }
        try {
            as.releaseAuthor(authorId);
        } catch (ErrorService ex) {
            throw ex;
        }
        return "redirect:/books";
    }

    // EDITORIAL CONTROLLER
    @GetMapping("/editeditorial")
    public String editEditorial(HttpSession session, @RequestParam(required = false) String id, @RequestParam(required = false) String action, ModelMap model) throws ErrorService {
        
        if (action == null) {
            action = "Create";
        }
        
        Editorial editorial = new Editorial();
        List<Editorial> editorials = new ArrayList<>(es.listEditorials());
        model.put("action", action);
        model.put("editorial", editorial);
        model.addAttribute("editorials", editorials);
        
        if (id != null) {
            int editorialId = Integer.parseInt(id);
            editorial = es.findEditorialId(editorialId);
            model.put("action", action);
            model.put("editorial", editorial);
            return "editeditorial.html";
        } else {
            editorial.setId(0);
            model.put("editorial", editorial);
            return "editeditorial.html";
        }
    }
    
    @PostMapping("/update-editorial")
    public String updateEditorial(ModelMap model, HttpSession session, @RequestParam int id, @RequestParam String editorialName) {
        
        Client login = (Client) session.getAttribute("usersession");
        if (login == null) {
            return "redirect:/";
        }
        try {
            if (id == 0) {
                es.registerEditorial(editorialName);
                return "redirect:/books";
            } else {
                es.editEditorial(editorialName, id);
                return "redirect:/books";
            }
        } catch (ErrorService e) {
            model.put("action", "Update");
            model.put("error", e.getMessage());
            return "editeditorial.html";
        }
    }
    
    @PostMapping("/delete-editorial")
    public String deleteEditorial(HttpSession session, @RequestParam int id) throws ErrorService {
        Client login = (Client) session.getAttribute("usersession");
        if (login == null) {
            return "redirect:/";
        }
        try {
            es.releaseEditorial(id);
        } catch (ErrorService ex) {
            throw ex;
        }
        return "redirect:/books";
    }
    
}
