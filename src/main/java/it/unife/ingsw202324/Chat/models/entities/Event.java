package it.unife.ingsw202324.Chat.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    /*
        ### CLASSE DI SERVIZIO #############################################################À

        È associata alla chat dell'evento specifico
     */
    private String name;
    private String description;
    private String type;
    private Integer minAge;
    private String loc;
    private LocalDateTime start;
    private LocalDateTime end;


}
