package lib.web.bookstore.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String document;
    private String name;
    private String lastName;
    private String telephoneNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private Set<Lending> lendings = new HashSet();
    private boolean released;
    private String password;
    private String passwordValidation;
    private String roles;
  

    public Client(String document, String name, String lastName, String telephoneNumber, boolean released, String password, String passwordValidation) {
        this.document = document;
        this.name = name;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.released = released;
        this.password = password;
        this.passwordValidation = passwordValidation;
    }

    public Client(int id, String document, String name, String lastName, String telephoneNumber, boolean released, String password, String passwordValidation) {
        this.id = id;
        this.document = document;
        this.name = name;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.released = released;
        this.password = password;
        this.passwordValidation = passwordValidation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordValidation() {
        return passwordValidation;
    }

    public void setPasswordValidation(String passwordValidation) {
        this.passwordValidation = passwordValidation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public Set<Lending> getLendings() {
        return lendings;
    }

    public void setLendings(Set<Lending> lendings) {
        this.lendings = lendings;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
    
    


    public Client() {
    }

    
    
    

}
