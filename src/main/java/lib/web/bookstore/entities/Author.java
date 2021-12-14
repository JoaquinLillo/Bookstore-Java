package lib.web.bookstore.entities;

import javax.persistence.*;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;    
    private String name;
    private boolean released;

    public Author(Integer id, String name, boolean released) {
        this.id = id;
        this.name = name;
        this.released = released;
    }

    public Author() {        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", name=" + name + ", released=" + released + '}';
    }
    
    

}
