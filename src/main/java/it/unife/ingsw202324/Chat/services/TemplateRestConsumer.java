
package it.unife.ingsw202324.Chat.services;

import it.unife.ingsw202324.Chat.models.entities.Event;
import it.unife.ingsw202324.Chat.models.entities.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class TemplateRestConsumer {

    static String uriBaseMock = "http://localhost:3000/api/";

    public List<User> findUsers(String resourceName) {
        /*
            Input:
                    resourceName:   indirizzo risorsa mockoon
         */
        RestClient restClient = RestClient.create();

        System.out.println(uriBaseMock + resourceName);
        // Effettua la richiesta GET e mappa la risposta in un array di User
        User[] users = restClient.get()
                .uri(uriBaseMock + resourceName)
                .retrieve()
                .body(User[].class);

        // Converti l'array in una lista e restituiscila
        if(users == null) return null;
        return Arrays.asList(users);
    }

    public static List<Event> findEvents(String resourceName){
        return null;
    }
}
