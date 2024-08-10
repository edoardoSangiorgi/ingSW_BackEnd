package it.unife.ingsw202324.Chat.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String username;
    private String name;
    private String surname;
    private boolean admin;

    // info meno utili
    private Date birthDate;


}
