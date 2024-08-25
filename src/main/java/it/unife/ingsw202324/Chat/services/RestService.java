package it.unife.ingsw202324.Chat.services;

import it.unife.ingsw202324.Chat.models.entities.Event;
import it.unife.ingsw202324.Chat.models.entities.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class RestService {

    static String uriBaseMock = "http://localhost:3000/";

    public List<User> findUsers(String resourceName) {
        /*
            legge tutti gli utenti da mockoon

            Input:
                    resourceName:   indirizzo risorsa mockoon
                    String

            Output:
                    List<Users>:    lista degli utenti
         */

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User[]> response = restTemplate.getForEntity(uriBaseMock + resourceName, User[].class);

        if(response.getBody() == null) return null;
        return Arrays.asList(response.getBody());


    }


    public List<Event> findEvents(String resourceName){
        /*
            legge tutti gli eventi da mockoon

            Input:
                    resourceName:   indirizzo risorsa mockoon
                    String

            Output:
                    List<Event>:    lista degli eventi
         */
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Event[]> response = restTemplate.getForEntity(uriBaseMock + resourceName, Event[].class);

        if(response.getBody() == null) return null;
        return Arrays.asList(response.getBody());
    }
}
