package it.unife.ingsw202324.Chat.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "chat")
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    private Long id;
    private String name;
    private String type;
    private Boolean deleted;
    private Date creationDate;

    @OneToMany(mappedBy = "chat")
    private List<Member> members;

    @OneToMany
    private List<Message> messages;


    public void addMember(Member memberToAdd) {
        members.add(memberToAdd);
    }

    public void removeMember(Member memberToRemove) {
        members.remove(memberToRemove);
    }

    public void addMessage(Message messageToAdd) {
        messages.add(messageToAdd);
        messageToAdd.setChat(this);
    }
}
