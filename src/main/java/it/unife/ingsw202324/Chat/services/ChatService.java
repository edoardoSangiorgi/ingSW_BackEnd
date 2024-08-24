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



    //### CONVERSIONE ###########################################################################################À
    public Chat convertFromDTO(ChatDTO chatDTO){
        // ChatDTO ---> Chat
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
        // Chat ---> ChatDTO
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
        /*
            Lista di Chat ---> Lista di BasicChatDTO
         */

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
                    String

            Output:
                    Chat:           L'oggetto chat da restituire al controller
                                    contiene anche tutte le info della chat
         */

        // -- cerca la chat dal DB
        return chatRepository.findByName(chatName).orElse(null);
    }



    // -- LETTURA LISTA CHAT --------------------------------------------------------------------------------
    public List<Chat> getAll(){
        /*
            trova tutte le chat in cui il membro selfuser è settato come
            deleted = false
         */
        return chatRepository.findAllChatsWithNonDeletedSelfUser();
    }



    // -- CREAZIONE NUOVA CHAT ------------------------------------------------------------------------------
    public void create(Chat chatToSave){
        /*
            salva una nuova chat sul db

            Input:
                    chatToSave  :   nuova chat da salvare
                    Chat
         */
        chatRepository.save(chatToSave);
    }



    // -- UPDATE CHAT ---------------------------------------------------------------------------------------
    public void update(Chat chatToUpdate){
        /*
            esegue l'update du una chat sul db

            Input:
                    chatToUpdate  :   chat da aggiornate
                    Chat
         */
        create(chatToUpdate);
    }


    // -- ELIMINAZIONE CHAT ---------------------------------------------------------------------------------
    public void deleteChat(Chat chatToDelete){
        /*
            cancella una chat livello logico
            setta deleted = true

            Input:
                    chatToDelete    :   chat da rimuovere
                    Chat
         */
        chatToDelete.setDeleted(true);
        update(chatToDelete);
    }



}
