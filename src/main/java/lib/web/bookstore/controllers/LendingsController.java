package lib.web.bookstore.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import lib.web.bookstore.entities.Author;
import lib.web.bookstore.entities.Book;
import lib.web.bookstore.entities.Client;
import lib.web.bookstore.entities.Editorial;
import lib.web.bookstore.entities.Lending;
import lib.web.bookstore.exceptions.ErrorService;
import lib.web.bookstore.services.AuthorService;
import lib.web.bookstore.services.BookService;
import lib.web.bookstore.services.ClientService;
import lib.web.bookstore.services.EditorialService;
import lib.web.bookstore.services.LendingService;
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
@RequestMapping("/lendings")
public class LendingsController {

    @Autowired
    private LendingService ls;

    @Autowired
    private AuthorService as;

    @Autowired
    private EditorialService es;

    @Autowired
    private BookService bs;

    @Autowired
    private ClientService cs;

    @GetMapping("")
    public String lendings(HttpSession session, ModelMap model) throws ErrorService {

        class LendingWBooks {

            Lending lending;
            String books;

            public LendingWBooks(Lending lending, String books) {
                this.lending = lending;
                this.books = books;
            }

            public LendingWBooks() {
            }

            public Lending getLending() {
                return lending;
            }

            public void setLending(Lending lending) {
                this.lending = lending;
            }

            public String getBooks() {
                return books;
            }

            public void setBooks(String books) {
                this.books = books;
            }

        }

        Client login = (Client) session.getAttribute("usersession");
        
        List<Lending> lendingsClient = ls.findClientLendings(login.getDocument());

        List<Lending> lendings = ls.listLendings();
        List<Book> books = bs.listBooks();
        List<String> booksAuthors = new ArrayList<>();

        List<LendingWBooks> lendingBClient = new ArrayList<>();
        List<LendingWBooks> lendingBookses = new ArrayList<>();

        for (Lending lending : lendingsClient) {
            LendingWBooks lwb = new LendingWBooks();
            String aux = "";

            List<Book> booksLended = new ArrayList<>();
            booksLended = lending.getBooks();
            for (Book book : booksLended) {
                if ("".equals(aux)) {
                    aux = book.getTitle();
                } else {
                    aux = aux + ", " + book.getTitle();
                }
            }
            lwb.setBooks(aux);
            lwb.setLending(lending);
            lendingBClient.add(lwb);
        }
        
        // General lendings

        for (Lending lending : lendings) {
            LendingWBooks lwb = new LendingWBooks();
            String aux = "";

            List<Book> booksLended = new ArrayList<>();
            booksLended = lending.getBooks();
            for (Book book : booksLended) {
                if ("".equals(aux)) {
                    aux = book.getTitle();
                } else {
                    aux = aux + ", " + book.getTitle();
                }
            }
            lwb.setBooks(aux);
            lwb.setLending(lending);
            lendingBookses.add(lwb);
        }

        
        model.put("lendingByClient", lendingBClient);
        model.put("booksByLending", lendingBookses);
        model.put("lendings", lendings);
        model.put("booksByAuthor", booksAuthors);
        model.put("books", books);

        return "lendings.html";
    }
 
    @GetMapping("/editlending")
    public String editLending(HttpSession session, @RequestParam(required = false) String lendingId, @RequestParam(required = false) String action, ModelMap model) throws ErrorService {

        if (action == null) {
            action = "Create";
        }

        Lending lending = new Lending();

        List<Lending> lendings = new ArrayList<>(ls.listLendings());

        model.put("action", action);
        model.put("lending", lending);
        List<Book> books = new ArrayList<>(bs.listBooks());
        model.put("books", books);
        model.put("clients", cs.listClients());

        if (lendingId != null) {
            int LendingID = Integer.parseInt(lendingId);
            lending = ls.findLendingId(LendingID);
            model.put("action", action);
            model.put("lending", lending);
            return "editlending.html";
        } else {

            return "editlending.html";
        }
    }

    @PostMapping("/update-lending")
    public String updateLending(ModelMap model, HttpSession session, @RequestParam int lendingId, @RequestParam String dateLended, @RequestParam String dateReturned, @RequestParam String[] books, @RequestParam String client) throws ErrorService {
        Lending lending = null;

        lending = ls.findLendingId(lendingId);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Client clientLending = cs.findClientName(client);

        List<Book> bookses = new ArrayList<>();

        for (String book : books) {

            Book book2 = bs.findBookTitle(book);
            if (lending != null && !lending.getBooks().contains(book2) ) {
                book2.setCopiesLended(book2.getCopiesLended() + 1);
                book2.setCopiesRemaining();
            }
            bookses.add(book2);
        }

        Client login = (Client) session.getAttribute("usersession");
        if (login
                == null) {
            return "redirect:/";
        }

        try {

            List<Lending> lendings = ls.listLendings();
            Date lendingDate = format.parse(dateLended);
            Date returningDate = format.parse(dateReturned);

            if (lendingId == 0) {
                ls.registerLending(lendingId, lendingDate, returningDate, bookses, clientLending);
                model.put("lendings", lendings);
                return "redirect:/lendings";
            } else {
                ls.editLending(lendingId, bookses, clientLending, lendingDate, returningDate);
                model.put("lendings", lendings);
                return "redirect:/lendings";
            }
        } catch (ErrorService e) {
            model.put("action", "Update");
            model.put("error", e.getMessage());
            return "editlending.html";
        } catch (ParseException ex) {
            throw new ErrorService("");
        }
    }

    @PostMapping("/delete-lending")
    public String deleteLending(HttpSession session, @RequestParam int lendingId, ModelMap model) throws ErrorService {
        Client login = (Client) session.getAttribute("usersession");

        List<Book> books = bs.listBooks();
        List<Author> authors = new ArrayList<>(as.listAuthors());
        List<Editorial> editorials = new ArrayList<>(es.listEditorials());

        if (login == null) {
            model.put("books", books);
            model.addAttribute("authors", authors);
            model.addAttribute("editorials", editorials);
            return "redirect:/";
        } else {
            ls.registerRestore(lendingId);
            return "redirect:/lendings";
        }
    }

}
