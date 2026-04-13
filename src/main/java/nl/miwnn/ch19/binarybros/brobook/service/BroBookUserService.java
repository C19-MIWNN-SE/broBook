package nl.miwnn.ch19.binarybros.brobook.service;

/*
 * @author Mart Stukje
 * !! Doel van programma
 * */

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BroBookUserService {

    public List<BroBookUser> getAllUsers(){
        List<BroBookUser> users = new ArrayList<>();
        users.add(new BroBookUser("Mart", "Stukje", "user"));
        users.add(new BroBookUser("Paul", "Rademaker", "user"));

        return users;
    }

}
