package it.unife.ingsw202324.Chat.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private String content;
    private String senderUsername;
    private LocalDateTime timestamp;

}
