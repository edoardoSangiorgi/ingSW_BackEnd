package it.unife.ingsw202324.Chat.services;

import it.unife.ingsw202324.Chat.models.DTOs.EventDTO;
import it.unife.ingsw202324.Chat.models.entities.Event;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    //### CONVERSIONE ############################################################################à

    public Event convertFromDTO(EventDTO eventToConvert, String name){
        return new Event(
                name,
                eventToConvert.getDescription(),
                eventToConvert.getType(),
                eventToConvert.getMinAge(),
                eventToConvert.getLoc(),
                eventToConvert.getStart(),
                eventToConvert.getEnd()
        );
    }

    public EventDTO convertToDTO(Event eventToConvert){
        if(eventToConvert == null) return null;
        return new EventDTO(
                eventToConvert.getDescription(),
                eventToConvert.getType(),
                eventToConvert.getMinAge(),
                eventToConvert.getLoc(),
                eventToConvert.getStart(),
                eventToConvert.getEnd()
        );
    }


    //### METODI #############################################################################



}
