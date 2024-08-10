package it.unife.ingsw202324.Chat.services;

import it.unife.ingsw202324.Chat.DTOs.MessageDTO;
import it.unife.ingsw202324.Chat.models.Chat;
import it.unife.ingsw202324.Chat.models.Message;
import it.unife.ingsw202324.Chat.models.User;
import it.unife.ingsw202324.Chat.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    UserService userService;


    //### CONVERSIONE ###############################################################################À
    public Message convertFromDTO(MessageDTO messageDTO){

        User sender = new User();
        sender.setUsername(messageDTO.getSenderUsername());

        return new Message(
                null,
                messageDTO.getContent(),
                messageDTO.getTimestamp(),
                null,
                sender
        );
    }

    public MessageDTO convertToDTO(Message message){

        MessageDTO newDto = new MessageDTO();


        newDto.setContent(message.getContent());
        newDto.setSenderUsername(message.getSender().getUsername());
        newDto.setTimestamp(message.getTimestamp());

        return newDto;
    }



    // -- CREA UN NUOVO MESSAGGIO -------------------------------------------------------------------------
    public void create(Message message){

        messageRepository.save(message);
    }


    // -- CERCA MESSAGGI DI UNA CHAT -----------------------------------------------------------------------
    public List<MessageDTO> getMessagesByChat(Chat chat){
        /*
            Input:
                    chatDTO:    oggetto DTO con le info sulla chat
                    ChatDTO

            Output:
                    dtoList:    lista di messaggi da ritornare al Client
                    List<MessageDTO>

         */
        List<Message> messageList = messageRepository.findByChat(chat);
        List<MessageDTO> dtoList = new ArrayList<>();
        for(Message message: messageList){
            dtoList.add(convertToDTO(message));
        }
        return dtoList;
    }


    public Message getLastMessageByChatName(Long chatId){
        return messageRepository.findFirstByChatIdOrderByTimestampDesc(chatId);
    }


}
