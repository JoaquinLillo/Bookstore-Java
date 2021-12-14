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
import lib.web.bookstore.services.ClientService;
import lib.web.bookstore.services.EditorialService;
import lib.web.bookstore.services.WordnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
public class PortalController {

    @Autowired
    private ClientService cs;

    @Autowired
    private BookService bs;

    @Autowired
    private AuthorService as;

    @Autowired
    private EditorialService es;

    @Autowired
    private WordnikService ws;

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(HttpSession session, @RequestParam(required = false) String logout, @RequestParam(required = false) String error, ModelMap model, String document, String password) {

        List<Book> books = bs.listBooks();
        List<Author> authors = new ArrayList<>(as.listAuthors());
        List<Editorial> editorials = new ArrayList<>(es.listEditorials());

        document = SecurityContextHolder.getContext().getAuthentication().getName();

        if (error != null) {
            model.put("error", "You've inserted wrong credentials");
            model.put("books", books);
            model.addAttribute("authors", authors);
            model.addAttribute("editorials", editorials);
            return "index.html";
        }
        if (logout != null) {
            model.put("logout", "You have logged out succesfully");
            model.put("books", books);
            model.addAttribute("authors", authors);
            model.addAttribute("editorials", editorials);
            return "index.html";
        }

        if (document == null && password == null) {
            return "index.html";
        }

        Client client = null;
        try {
            client = cs.findClientDocument(document);
        } catch (UsernameNotFoundException | ErrorService e) {

            String wordOfDay = ws.wordnikApi();
            model.put("wordOfDay", wordOfDay);

//            model.put("error", e.getMessage());
            model.put("books", books);
            model.addAttribute("authors", authors);
            model.addAttribute("editorials", editorials);
            return "index.html";
        }

        String wordOfDay = ws.wordnikApi();
        System.out.println("Esta es la palabra del día");
        System.out.println(wordOfDay);

        model.put("wordOfDay", wordOfDay);

        session.setAttribute("usersession", client);
        model.put("books", books);
        model.addAttribute("authors", authors);
        model.addAttribute("editorials", editorials);
        model.put("user", client);
        return "index.html";
    }

    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    @PostMapping("/registerUser")
    public String registerUser(ModelMap model, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String document, @RequestParam String telephone, @RequestParam String password, @RequestParam String passwordValidation) throws ErrorService {

        List<Book> books = bs.listBooks();
        List<Author> authors = new ArrayList<>(as.listAuthors());
        List<Editorial> editorials = new ArrayList<>(es.listEditorials());

        try {
            if (document.equals(cs.findClientDocument(document).getDocument())) {
                model.put("error", "The user is already registered");
                return "register.html";
            }
        } catch (ErrorService e) {
            Client user = cs.registerClient(firstName, lastName, document, telephone, password, passwordValidation);
            model.put("user", user);
            model.put("books", books);
            model.addAttribute("authors", authors);
            model.addAttribute("editorials", editorials);
            return "index.html";
        }
        return "index.html";
    }

}
