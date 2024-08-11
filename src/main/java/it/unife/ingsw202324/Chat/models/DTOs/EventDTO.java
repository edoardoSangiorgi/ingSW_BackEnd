package it.unife.ingsw202324.Chat.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {


    private String name;
    private String description;
    private String type;
    private Integer minAge;
    private String loc;
    private LocalDateTime start;
    private LocalDateTime end;

}
