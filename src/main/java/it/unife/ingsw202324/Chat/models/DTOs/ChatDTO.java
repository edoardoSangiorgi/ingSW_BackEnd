package it.unife.ingsw202324.Chat.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    private String chatName;
    private String type;
    private Date creationDate;

    private List<MemberDTO> members;
    private List<MessageDTO> messages;
    private EventDTO event; // sono contenute le info dell'evento

}