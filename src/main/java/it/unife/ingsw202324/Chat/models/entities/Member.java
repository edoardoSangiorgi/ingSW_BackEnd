package it.unife.ingsw202324.Chat.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    /*-- mappa il singolo membro della chat --*/

    @Id
    private String username;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private Boolean deleted;

    @ManyToOne
    private Chat chat;
    private Boolean admin;

}
