package it.unife.ingsw202324.Chat.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    private String chatName;
    private String type;
    private LocalDate creationDate;

    private List<MemberDTO> members;
    private List<MessageDTO> messages;
    private EventDTO event;

}