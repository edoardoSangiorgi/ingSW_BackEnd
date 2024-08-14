package it.unife.ingsw202324.Chat.services;


import it.unife.ingsw202324.Chat.models.entities.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class TemplateRestConsumer {

    static String uriBaseMock = "http://localhost:3000/api/";

    public static List<User> callREST(String resourceName, String uriBase, boolean useMock) {
        RestClient restClient = RestClient.create();
        /*
        Creo uriBase per chiamare Mockoon se l'impostazione è useMock
         */
        if(useMock)
            uriBase = uriBaseMock;

        System.out.println(uriBase+resourceName);

        User[] users=  restClient.get()
                .uri(uriBase + resourceName)
                .retrieve()
                .body(User[].class);

        // filtraggio

        return Arrays.stream(users).toList();
    }
}