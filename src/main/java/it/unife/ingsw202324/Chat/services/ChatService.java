package it.unife.ingsw202324.Chat.services;


import it.unife.ingsw202324.Chat.DTOs.ChatDTO;
import it.unife.ingsw202324.Chat.DTOs.MessageDTO;
import it.unife.ingsw202324.Chat.DTOs.UserDTO;
import it.unife.ingsw202324.Chat.models.Chat;
import it.unife.ingsw202324.Chat.models.Message;
import it.unife.ingsw202324.Chat.models.User;
import it.unife.ingsw202324.Chat.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {


    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;



    //### CONVERSIONE ###########################################################################################À
    public Chat convertFromDTO(ChatDTO chatDTO){

        return new Chat(
                null,
                chatDTO.getChatName(),
                chatDTO.getType(),
                false,
                chatDTO.getCreationDate(),
                null,
                null,
                null
        );
    }

    public ChatDTO convertToDTO(Chat chat){

        if(chat == null) return null;

        //-- conversione messaggi
        List<Message> chatMessages = chat.getMessages();
        List<MessageDTO> chatMessagesDTO = new ArrayList<>();
        for(Message message: chatMessages){
            chatMessagesDTO.add(messageService.convertToDTO(message));
        }

        //-- conversione utenti membri
        List<User> chatMembers = chat.getMembers();
        List<UserDTO> chatMembersDTO = new ArrayList<>();
        for(User member: chatMembers){
            chatMembersDTO.add(userService.convertToDTO(member));
        }

        //-- TODO: API per recuperare l'evento

        //-- conversione
        return new ChatDTO(
                chat.getName(),
                chat.getType(),
                chat.getCreationDate(),
                chatMembersDTO,
                chatMessagesDTO,
                null
        );


    }




    //### METODI ###################################################################################À

    // --- LETTURA SINGOLA CHAT -----------------------------------------------------------------------------
    public Chat getChatByName(String chatName){
        /*
            recupera la singola chat in base al nome



            Input:
                    chat:       la chat da trovare

            Output:
                    ChatDTO:    L'oggetto chatDTO da restituire al client
                                che contiene anche tutti i messaggi da stampare
         */

        // -- cerca la chat dal DB
        Optional<Chat> requestedChat = chatRepository.findByName(chatName);

        //-- ritorna la chat trovata altrimenti ritorna null
        return requestedChat.orElse(null);

    }


    // -- LETTURA LISTA CHAT --------------------------------------------------------------------------------
    public List<Chat> getAll(){
        return chatRepository.findAll();
    }



    // -- CREAZIONE NUOVA CHAT ------------------------------------------------------------------------------
    public void createChat(Chat chatToSave){
        /*
            INPUT:
                    request:    l'oggetto che coniene le info della chat dal client
                    CreateRequestChat

            COSA FA:
                    - converte request in un'oggetto chat
                    - salva l'oggetto chat sul DB
         */
        ;

        chatRepository.save(chatToSave);
    }



    // -- UPDATE CHAT ---------------------------------------------------------------------------------------
    public Chat updateChat(Chat chatToUpdate){
        return chatRepository.save(chatToUpdate);
    }


    //--- AGGIUNGI UTENTE ----------------------------------------------------------------------------------
    public void addUser(User user, Chat chat){

        chat.addMember(user);
        userService.create(user);
    }


    //--- RIMUOVI UTENTE -----------------------------------------------------------------------------------
    public void removeUser(User user, Chat chat){
        chat.removeMember(user);
    }


    //--- AGGIUNGI MESSAGGIO -------------------------------------------------------------------------------
    public void addMessage(Message messageToAdd, Chat chat){
        chat.addMessage(messageToAdd);
        messageService.create(messageToAdd);
    }


    // -- ELIMINAZIONE CHAT ---------------------------------------------------------------------------------
    public void deleteChat(Long chatId){
        chatRepository.deleteById(chatId);
    }



}
