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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private ClientService cs;

    @Autowired
    private BookService bs;

    @Autowired
    private AuthorService as;

    @Autowired
    private EditorialService es;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/editprofile")
    public String editProfile(HttpSession session, @RequestParam String document, ModelMap model) {

        Client login = (Client) session.getAttribute("usersession");

        List<Book> books = bs.listBooks();
        List<Author> authors = new ArrayList<>(as.listAuthors());
        List<Editorial> editorials = new ArrayList<>(es.listEditorials());

        if (login == null || !login.getDocument().equals(document)) {
            model.put("books", books);
            model.addAttribute("authors", authors);
            model.addAttribute("editorials", editorials);
            return "redirect:/";
        }

        try {

            Client client = cs.findClientDocument(document);
            model.addAttribute("profile", client);
            return "editprofile.html";
        } catch (ErrorService e) {
            model.addAttribute("error", e.getMessage());
        }
        return "editprofile.html";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/update-profile")
    public String updateProfile(ModelMap model, HttpSession session, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String document, @RequestParam String telephone, @RequestParam String password, @RequestParam String passwordValidation) {

        Client login = (Client) session.getAttribute("usersession");

        List<Book> books = bs.listBooks();
        List<Author> authors = new ArrayList<>(as.listAuthors());
        List<Editorial> editorials = new ArrayList<>(es.listEditorials());

        if (login == null || !login.getDocument().equals(document)) {
            model.put("books", books);
            model.addAttribute("authors", authors);
            model.addAttribute("editorials", editorials);
            return "redirect:/";
        }

        Client client = null;
        try {
            client = cs.findClientDocument(document);
            cs.editClient(client.getId(), firstName, lastName, document, telephone, password, passwordValidation, client.getLendings());
            session.setAttribute("usersession", client);
            model.put("books", books);
            model.addAttribute("authors", authors);
            model.addAttribute("editorials", editorials);
            return "redirect:/";
        } catch (ErrorService e) {
            model.put("error", e.getMessage());
            model.put("profile", client);
            return "editprofile.html";
        }

    }

}
