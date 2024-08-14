package it.unife.ingsw202324.Chat.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicChatDTO {

    private String name;
    private String type;
    private String lastMessage;

}
