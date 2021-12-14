package lib.web.bookstore.services;

import java.util.Date;
import lib.web.bookstore.entities.Book;
import lib.web.bookstore.entities.Client;
import lib.web.bookstore.entities.Lending;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import lib.web.bookstore.repository.LendingDAO;
import lib.web.bookstore.exceptions.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LendingService {

    Scanner leer = new Scanner(System.in).useDelimiter("\n");
    @Autowired
    LendingDAO ldao;
    @Autowired
    ClientService cs;

    @Transactional
    public void registerLending(int lendingId, Date dateLended, Date dateReturned, List<Book> books, Client client) throws ErrorService {
        try {
            Lending lending = new Lending();
            lending.setDateLended(dateLended);
            lending.setDateReturned(dateReturned);
            List<Long> booksISBN = new ArrayList<>();
            for (Book book : books) {
                booksISBN.add(book.getIsbn());
            }
            ldao.registerLending(lending, client.getId(), booksISBN);
        } catch (ErrorService ex) {
            throw ex;
        }
    }

    @Transactional
    public List<Lending> findClientLendings(String document) throws ErrorService {
        Client client = new Client();        
        client = cs.findClientDocument(document);
        if (client == null) {
            throw new ErrorService("Error: Client doesn't exist");
        }
        List<Lending> lendings = new ArrayList<>(ldao.findClientLendings(document));
        return lendings;
    }

    @Transactional
    public Lending findLendingId(int lending_id) {
        Lending lending = ldao.findLendingId(lending_id);
        return lending;
    }

    @Transactional
    public void editLending(int lending_id, List<Book> books, Client client, Date dateLended, Date dateReturned) {
        Lending lending = ldao.findLendingId(lending_id);
        lending.setDateLended(dateLended);
        lending.setDateReturned(dateReturned);
        lending.setClient(client);
        lending.setBooks(books);
        ldao.editLending(lending);
    }

    @Transactional
    public void registerRestore(int lending_id) throws ErrorService {
        if (lending_id == 0) {
            throw new ErrorService("Required field: enter the lending id");
        }
        try {
            Lending lending = ldao.findLendingId(lending_id);
            ldao.registerRestore(lending);
        } catch (ErrorService ex) {
            throw ex;
        }

    }

    @Transactional
    public void deleteLending(int lending_id) throws ErrorService {
        if (lending_id == 0) {
            throw new ErrorService("Required field: enter the lending id");
        }
        Lending lending = ldao.findLendingId(lending_id);
        ldao.deleteLending(lending);
    }

    @Transactional
    public List<Lending> listLendings() {
        List<Lending> lendings = ldao.listLendings();
        return lendings;
    }

}

// @Transactional
//    public Set<Book> changeBooks(Set<Book> booksLended, Lending lending) throws ErrorService {
//        BookService bs = new BookService();
//        ArrayList<Book> booksAvaible = bs.listBooks();
//        System.out.println("How many books do you want to change?");
//        int amountToChange = leer.nextInt();
//        amountToChange = (amountToChange > booksLended.size()) ? booksLended.size() : amountToChange;
//        System.out.println("Wich books do you want to change?");
//        for (Book book : booksLended) {
//            System.out.println(book.getTitle() + ": " + book.getIsbn());
//        }
//        for (int i = 0; i < amountToChange; i++) {
//            try {
//                System.out.println("Select the book's isbn");
//                Book book = bs.findBookISBN(leer.nextLong());
//                book.getLendings().remove(lending);
//                book.setCopiesLended(book.getCopiesLended() - 1);
//                book.setCopiesRemaining();
//                bs.bdao.editBook(book);
//                booksLended.remove(book);
//                System.out.println("Now select one of the books on stock");
//                for (Book book1 : booksAvaible) {
//                    System.out.println(book1.getTitle() + ": " + book1.getIsbn());
//                }
//                book = bs.findBookISBN(leer.nextLong());
//                book.getLendings().add(lending);
//                book.setCopiesLended(book.getCopiesLended() + 1);
//                book.setCopiesRemaining();
//                bs.bdao.editBook(book);
//                booksLended.add(book);
//            } catch (ErrorService ex) {
//                throw ex;
//            }
//        }
//        return booksLended;
//    }
//
//    @Transactional
//    public Set<Book> removeBooks(Set<Book> booksLended, Lending lending) throws ErrorService {
//        BookService bs = new BookService();
//        System.out.println("How many books do you want to remove from the lending?");
//        int amountToChange = leer.nextInt();
//        amountToChange = (amountToChange > booksLended.size()) ? booksLended.size() : amountToChange;
//        System.out.println("Wich books do you want to remove from the lending?");
//        for (Book book : booksLended) {
//            System.out.println(book.getTitle() + ": " + book.getIsbn());
//        }
//        for (int i = 0; i < amountToChange; i++) {
//            try {
//                System.out.println("Select the book's isbn");
//                Book book = bs.findBookISBN(leer.nextLong());
//                book.getLendings().remove(lending);
//                book.setCopiesLended(book.getCopiesLended() - 1);
//                book.setCopiesRemaining();
//                bs.bdao.editBook(book);
//                booksLended.remove(book);
//            } catch (ErrorService ex) {
//                throw ex;
//            }
//        }
//        return booksLended;
//    }
//
//    @Transactional
//    public Set<Book> addBooks(Set<Book> booksLended, Lending lending) throws ErrorService {
//        BookService bs = new BookService();
//        ArrayList<Book> booksAvaible = bs.listBooks();
//        System.out.println("How many books do you want to add?");
//        int amountToChange = leer.nextInt();
//        amountToChange = (amountToChange > booksAvaible.size()) ? booksAvaible.size() : amountToChange;
//        try {
//            for (int i = 0; i < amountToChange; i++) {
//                System.out.println("Now select one of the books on stock");
//                for (Book book : booksAvaible) {
//                    System.out.println(book.getTitle() + ": " + book.getIsbn());
//                }
//                System.out.println("Select the books ISBN");
//                Book book = bs.findBookISBN(leer.nextLong());
//                book.getLendings().add(lending);
//                book.setCopiesLended(book.getCopiesLended() + 1);
//                book.setCopiesRemaining();
//                bs.bdao.editBook(book);
//                booksLended.add(book);
//            }
//        } catch (ErrorService e) {
//            throw e;
//        }
//        return booksLended;
//    }
