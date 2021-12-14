package lib.web.bookstore.services;

import lib.web.bookstore.entities.Editorial;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import lib.web.bookstore.exceptions.ErrorService;
import lib.web.bookstore.repository.EditorialDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialService {

    Scanner leer = new Scanner(System.in).useDelimiter("\n");
    @Autowired
    EditorialDAO edao;

    @Transactional
    public void releaseEditorial(int editorialId) throws ErrorService {
        Editorial editorial = edao.findEditorialId(editorialId);
        editorial.setReleased(true);
        edao.releaseEditorial(editorial);
    }

    @Transactional
    public void editEditorial(String editorialName, int editorialId) throws ErrorService {

        Editorial editorial = edao.findEditorialId(editorialId);
        editorial.setId(editorialId);
        editorial.setName(editorialName);
        edao.editEditorial(editorial);
    }

    @Transactional
    public List<Editorial> listEditorials() {
        ArrayList<Editorial> editorials = edao.listEditorials();
        return editorials;
    }

    @Transactional
    public Editorial registerEditorial(String editorialName) throws ErrorService {
        Editorial editorial = new Editorial();
        try {
            editorial.setName(editorialName);
            edao.persistEditorial(editorial);
        } catch (ErrorService ex) {
            throw ex;
        }
        return editorial;
    }

    @Transactional
    public Editorial findEditorialName(String editorialName) throws ErrorService {
        if (editorialName == null) {
            throw new ErrorService("Required field: Enter the editorial's name");
        }
        Editorial editorial = new Editorial();
        editorial = edao.findEditorialName(editorialName);
        return editorial;
    }

    @Transactional
    public Editorial findEditorialId(int editorialId) throws ErrorService {
        if (editorialId == 0) {
            throw new ErrorService("Required field: Enter the editorial's name");
        }
        Editorial editorial = edao.findEditorialId(editorialId);
        return editorial;
    }

}
