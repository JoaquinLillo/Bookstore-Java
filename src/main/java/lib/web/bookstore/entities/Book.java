package lib.web.bookstore.entities;

import java.io.Serializable;
import java.util.HashSet;
import javax.persistence.*;
import java.util.Set;
import lib.web.bookstore.exceptions.ErrorService;

@Entity
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long isbn;
    private String title;
    private Integer year;
    private Integer copies;
    private Integer copiesLended;
    private Integer copiesRemaining;
    private boolean released;
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "books")
    private Set<Lending> lendings = new HashSet();
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Author author;
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Editorial editorial;

    public Book() {
    }

    public Book(long isbn, String title, Integer year, Integer copies, Integer copiesLended, Integer copiesRemaining, boolean released, Author author, Editorial editorial) {
        this.isbn = isbn;
        this.title = title;
        this.year = year;
        this.copies = copies;
        this.copiesLended = copiesLended;
        this.copiesRemaining = copiesRemaining;
        this.released = released;
        this.author = author;
        this.editorial = editorial;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public Integer getCopiesLended() {
        return copiesLended;
    }

    public void setCopiesLended(Integer copiesLended) {
        if (copiesLended > copies) {
            System.out.println("Not enough books in stock");
            copiesLended = copies;
        }
        this.copiesLended = copiesLended;

    }

    public Integer getCopiesRemaining() {
        return copiesRemaining;
    }

    public void setCopiesRemaining() throws ErrorService {
        this.copiesRemaining = this.copies - this.copiesLended;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public Set<Lending> getLendings() {
        return lendings;
    }

    public void setLendings(Set<Lending> lendings) {
        this.lendings = lendings;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    @Override
    public String toString() {
        return "Book{" + "isbn=" + isbn + ", title=" + title + ", year=" + year + ", copies=" + copies + ", copiesLended=" + copiesLended + ", copiesRemaining=" + copiesRemaining + ", released=" + released + ", author=" + author + ", editorial=" + editorial + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.isbn ^ (this.isbn >>> 32));
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
        final Book other = (Book) obj;
        return this.isbn == other.isbn;
    }

}
