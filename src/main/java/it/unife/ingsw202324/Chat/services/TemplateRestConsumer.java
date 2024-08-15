
package it.unife.ingsw202324.Chat.services;

import it.unife.ingsw202324.Chat.models.entities.Event;
import it.unife.ingsw202324.Chat.models.entities.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class TemplateRestConsumer {

    private final String uriBaseMock = "http://localhost:3000/api/";

    public List<User> findUsers(String resourceName) {
        /*
            Input:
                    resourceName:   indirizzo risorsa mockoon
         */
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> response = restTemplate.getForEntity(uriBaseMock + "/users", User[].class);

        return Arrays.asList(response.getBody());
    }

    public List<Event> findEvents(String resourceName){
          /*
            Input:
                    resourceName:   indirizzo risorsa mockoon
         */
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Event[]> response = restTemplate.getForEntity(uriBaseMock + "/Events", Event[].class);

        return Arrays.asList(response.getBody());
    }
}
