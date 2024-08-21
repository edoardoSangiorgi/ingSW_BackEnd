package it.unife.ingsw202324.Chat.services;

import it.unife.ingsw202324.Chat.models.DTOs.MessageDTO;
import it.unife.ingsw202324.Chat.models.entities.Chat;
import it.unife.ingsw202324.Chat.models.entities.Member;
import it.unife.ingsw202324.Chat.models.entities.Message;
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
    MemberService memberService;


    //### CONVERSIONE ###############################################################################À
    public Message convertFromDTO(MessageDTO messageDTO, Chat chat, Member sender){


        return new Message(
                null,
                messageDTO.getContent(),
                messageDTO.getTimestamp(),
                chat,
                sender
        );
    }

    public MessageDTO convertToDTO(Message message){

        MessageDTO newDto = new MessageDTO();


        newDto.setContent(message.getContent());
        newDto.setSender(message.getSender().getUsername());
        newDto.setTimestamp(message.getTimestamp());

        return newDto;
    }

    public List<MessageDTO> convertListToDTO(List<Message> listToConvert){
        List<MessageDTO> convertedList = new ArrayList<>();
        for(Message message: listToConvert){
            convertedList.add(convertToDTO(message));
        }
        return convertedList;
    }



    // -- CREA UN NUOVO MESSAGGIO -------------------------------------------------------------------------
    public void create(Message message){
        messageRepository.save(message);
    }


    // -- CERCA MESSAGGI DI UNA CHAT -----------------------------------------------------------------------
    public List<Message> getMessagesByChat(Chat chat){
        /*
            Input:
                    chatDTO:    oggetto DTO con le info sulla chat
                    ChatDTO

            Output:
                    dtoList:    lista di messaggi da ritornare al Client
                    List<MessageDTO>

         */
        return messageRepository.findByChat(chat);
    }


    //--- CERCA L'ULTIMO MEASSAGGIO -------------------------------------------------------------------
    public String getLastMessageByChat(Chat chat){
        /*
            cerca l'ultimo messaggio di una chat
            formatta l'ultimo messaggio in una stringa
         */
        List<Message> lastMessageList =  messageRepository.findLastMessageByChat(chat);
        Message lastMessage;

        if(lastMessageList.isEmpty()) return "";
        if(lastMessageList.size() > 1){
            lastMessage = lastMessageList.get(0);
        } else {
            lastMessage = lastMessageList.get(0);
        }
        String content = "";
        if(lastMessage != null){
            String text = lastMessage.getContent();
            String username = lastMessage.getSender().getUsername();
            content = username + ": " + text;
        }

        return content;
    }


}
