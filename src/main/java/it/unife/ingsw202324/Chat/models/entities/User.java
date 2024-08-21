package it.unife.ingsw202324.Chat.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /*-- mappa gli utenti disponibili (non membri di nessun gruppo)--*/

    private String username;
    private String name;
    private String surname;
    private LocalDate birthDate;
}
