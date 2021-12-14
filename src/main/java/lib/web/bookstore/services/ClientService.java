package lib.web.bookstore.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.Set;
import lib.web.bookstore.entities.Client;
import lib.web.bookstore.entities.Lending;
import lib.web.bookstore.exceptions.ErrorService;
import lib.web.bookstore.repository.ClientDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientService implements UserDetailsService {

    Scanner leer = new Scanner(System.in).useDelimiter("\n");

    @Autowired
    ClientDAO cdao = new ClientDAO();

    public Client findClientName(String clientName) throws ErrorService {
        if (clientName == null) {
            throw new ErrorService("Required field: Enter the client's name");
        }
        Client client = cdao.findClientName(clientName);
        return client;
    }

    public Client findClientDocument(String document) throws ErrorService {
        if (document == null) {
            throw new ErrorService("Required field: Enter the client's document");
        }
        Client client = new Client();
        try {
            client = cdao.findClientDocument(document);
        } catch (UsernameNotFoundException e) {
            throw new ErrorService(e.getMessage());
        }

        return client;
    }

    public Client registerClient(String firstName, String lastName, String document, String telephone, String password, String passwordValidation) {
        Client client = new Client();
        client.setDocument(document);
        client.setName(firstName);
        client.setLastName(lastName);
        client.setTelephoneNumber(telephone);
        String crypted = new BCryptPasswordEncoder().encode(password);
        client.setPassword(crypted);
        client.setPasswordValidation(crypted);
        cdao.registerClient(client);
        return client;
    }

    public Collection<Client> listClients() {
        ArrayList<Client> clients = cdao.listClients();
        return clients;
    }

    public void editClient(int client_id, String firstName, String lastName, String document, String telephone, String password, String passwordValidation, Set<Lending> lendings) {
        Client client = cdao.findClientId(client_id);
        client.setDocument(document);
        client.setName(firstName);
        client.setLastName(lastName);
        client.setTelephoneNumber(telephone);
        cdao.editClient(client, lendings);
    }

    public void releaseClient(int id_client) {
        Client client = cdao.findClientId(id_client);
        cdao.releaseClient(client);
    }

    @Override
    public UserDetails loadUserByUsername(String document) throws UsernameNotFoundException {

        Client client = cdao.findClientDocument(document);

        if (client != null) {
            return new User(client.getDocument(), client.getPassword(), Arrays.asList(new SimpleGrantedAuthority("USER")));

        } else {
            throw new UsernameNotFoundException("The client wasn't found on the database");
        }
    }
}
