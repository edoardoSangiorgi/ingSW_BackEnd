package it.unife.ingsw202324.Chat.services;


import it.unife.ingsw202324.Chat.models.DTOs.BasicChatDTO;
import it.unife.ingsw202324.Chat.models.DTOs.ChatDTO;
import it.unife.ingsw202324.Chat.models.entities.Chat;
import it.unife.ingsw202324.Chat.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {


    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private EventService eventService;



    //### CONVERSIONE ###########################################################################################À
    public Chat convertFromDTO(ChatDTO chatDTO){
        return new Chat(
                null,
                chatDTO.getChatName(),
                chatDTO.getType(),
                false,
                chatDTO.getCreationDate(),
                null,
                null
        );


    }

    public ChatDTO convertToDTO(Chat chat){
        return new ChatDTO(
                chat.getName(),
                chat.getType(),
                chat.getCreationDate(),
                null,
                null,
                null
        );


    }

    public List<BasicChatDTO> convertListToBasicDTO(List<Chat> listToConvert){

        List<BasicChatDTO> convertedList = new ArrayList<>();
        for(Chat chat: listToConvert){

            String content = messageService.getLastMessageByChat(chat);

            //-- creazione oggetto da inviare nella view
            convertedList.add(new BasicChatDTO(chat.getName(), chat.getType(), content));
        }

        return convertedList;
    }




    //### METODI ###################################################################################À

    // --- LETTURA SINGOLA CHAT -----------------------------------------------------------------------------
    public Chat getChatByName(String chatName){
        /*
            recupera la singola chat in base al nome

            Input:
                    chatName:       nome della chat da trovare

            Output:
                    Chat:           L'oggetto chat da restituire al controller
                                    contiene anche tutte le info della chat
         */

        // -- cerca la chat dal DB
        return chatRepository.findByName(chatName).orElseThrow(() -> new RuntimeException("Chat not found"));
    }


    // -- LETTURA LISTA CHAT --------------------------------------------------------------------------------
    public List<Chat> getAll(){
        return chatRepository.findAll();
    }



    // -- CREAZIONE NUOVA CHAT ------------------------------------------------------------------------------
    public void create(Chat chatToSave){
        /*
            INPUT:
                    request:    l'oggetto che coniene le info della chat dal client
                    CreateRequestChat

            COSA FA:
                    - converte request in un'oggetto chat
                    - salva l'oggetto chat sul DB
         */


        chatRepository.save(chatToSave);
    }



    // -- UPDATE CHAT ---------------------------------------------------------------------------------------
    public void update(Chat chatToUpdate){
        create(chatToUpdate);
    }


    // -- ELIMINAZIONE CHAT ---------------------------------------------------------------------------------
    public void deleteChat(Long chatId){
        chatRepository.deleteById(chatId);
    }



}
