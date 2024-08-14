package it.unife.ingsw202324.Chat.models.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private String username;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private Boolean deleted;
    private Boolean admin;


}
