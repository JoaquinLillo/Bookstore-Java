package lib.web.bookstore.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lib.web.bookstore.entities.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {

    private String document;
    private String name;
    private String lastName;
    private String telephoneNumber;
    private boolean released;
    private String password;
    private String passwordValidation;
    private List<GrantedAuthority> authorities = new ArrayList<>();

    public MyUserDetails(Client client) {
        this.document = client.getDocument();
        this.name = client.getName();
        this.lastName = client.getLastName();
        this.telephoneNumber = client.getTelephoneNumber();
        this.released = client.isReleased();
        this.password = client.getPassword();
        this.passwordValidation = client.getPasswordValidation();
        this.authorities = Arrays.stream(client.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public MyUserDetails() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return document;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return released;
    }

}
