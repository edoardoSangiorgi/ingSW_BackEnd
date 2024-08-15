
package it.unife.ingsw202324.Chat.services;

import it.unife.ingsw202324.Chat.models.entities.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class TemplateRestConsumer {

    static String uriBaseMock = "http://localhost:3000/api/";

    public static List<User> callREST(String resourceName, String uriBase, boolean useMock) {
        // Usa WebClient invece di RestClient
        WebClient webClient = WebClient.create();

        // Se useMock è vero, usa l'URI base di Mockoon
        if(useMock)
            uriBase = uriBaseMock;

        System.out.println(uriBase + resourceName);
        // Effettua la richiesta GET e mappa la risposta in un array di User
        User[] users = webClient.get()
                .uri(uriBase + resourceName)
                .retrieve()
                .bodyToMono(User[].class)
                .block(); // Usa block per ottenere il risultato sincronicamente

        // Converti l'array in una lista e restituiscila
        return Arrays.asList(users);
    }
}
