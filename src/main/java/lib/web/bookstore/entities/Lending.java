package lib.web.bookstore.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
public class Lending implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lending_id", nullable = false, unique = true)
    private int id;
    @Temporal(TemporalType.DATE)
    private Date dateLended;
    @Temporal(TemporalType.DATE)
    private Date dateReturned;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "lending_books", joinColumns = @JoinColumn(name = "lending_id"), inverseJoinColumns = @JoinColumn(name = "isbn"))
    private List<Book> books = new ArrayList<>();
    @JoinColumn(name = "client_id")
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Client client;
    @Column(name = "RELEASED")
    private boolean released;

    public Lending() {
    }

    public Lending(int id, Date dateLended, Date dateReturned, List<Book> books, Client client, boolean released) {
        this.id = id;
        this.dateLended = dateLended;
        this.dateReturned = dateReturned;
        this.books = books;
        this.client = client;
        this.released = released;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateLended() {
        return dateLended;
    }

    public void setDateLended(Date dateLended) {
        this.dateLended = dateLended;
    }

    public Date getDateReturned() {
        return dateReturned;
    }

    public void setDateReturned(Date dateReturned) {
        int day = dateLended.getDay();
        int month = dateLended.getMonth();
        int year = dateLended.getYear();
        if (dateReturned.after(new Date(year, month + 2, day))) {
            System.out.println("The return date cannot exceed 2 months ");
            dateReturned = new Date(year, month + 2, day);
            System.out.println("The new return date is:");
            System.out.println(dateReturned.toString());
        }
        this.dateReturned = dateReturned;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Lending other = (Lending) obj;
        return this.id == other.id;
    }

}
