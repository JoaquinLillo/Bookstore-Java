package lib.web.bookstore.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import lib.web.bookstore.entities.Client;
import lib.web.bookstore.entities.Lending;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ClientDAO {

    @PersistenceContext
    EntityManager em = Persistence.createEntityManagerFactory("webPU").createEntityManager();

    @Transactional
    public Client findClientName(String clientName) {
        Client client = new Client();
        try {
            client = (Client) em.createQuery("select c from Client c where CONCAT(c.name,' ',c.lastName) LIKE '%" + clientName + "%'").getSingleResult();
        } catch (NoResultException e) {
            System.out.println("The client wasn't found on the database");
            client = null;
        }
        return client;
    }

    @Transactional
    public Client registerClient(Client client) {
        em.persist(client);
        return client;
    }

    @Transactional
    public ArrayList<Client> listClients() {
        Collection<Client> clientsCollection = em.createQuery("select a from Client a").getResultList();
        ArrayList<Client> clients = new ArrayList<>(clientsCollection);
        return clients;
    }

    @Transactional
    public void editClient(Client client, Set<Lending> lendings) {
        client.setLendings(lendings);
        em.merge(client);
    }

    @Transactional
    public Client findClientId(int client_id) {
        Client client = em.find(Client.class, client_id);
        return client;
    }

    @Transactional
    public void releaseClient(Client client) {
        client.setReleased(false);
        em.merge(client);
    }

    @Transactional
    public Client findClientDocument(String document) throws UsernameNotFoundException {
        Client client = new Client();
        try {
            client = (Client) em.createQuery("select c from Client c where c.document='" + document + "'").getSingleResult();
        } catch (NoResultException | UsernameNotFoundException e) {
            throw new UsernameNotFoundException("The client wasn't found on the database");
        } catch (NonUniqueResultException ex) {
            throw new UsernameNotFoundException("The client is registered twice or more on the system");
        }

        return client;

    }

}
