package it.unife.ingsw202324.Chat.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "chat")
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String type;
    private Boolean deleted;
    private LocalDate creationDate;

    @OneToMany(mappedBy = "id.chat")
    private List<Member> members;

    @OneToMany
    private List<Message> messages;

}
