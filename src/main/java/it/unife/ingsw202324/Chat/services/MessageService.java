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
        // MessageDTO ---> Message
        return new Message(
                null,
                messageDTO.getContent(),
                messageDTO.getTimestamp(),
                chat,
                sender
        );
    }

    public MessageDTO convertToDTO(Message message){
        // Message ---> MessageDTO
        return new MessageDTO(
                message.getContent(),
                message.getSender().getUsername(),
                message.getTimestamp()
        );
    }

    public List<MessageDTO> convertListToDTO(List<Message> listToConvert){
        // Lista di MessageDTO ---> Lista di Message
        List<MessageDTO> convertedList = new ArrayList<>();
        for(Message message: listToConvert){
            convertedList.add(convertToDTO(message));
        }
        return convertedList;
    }


    //### METODI ###########################################################################################


    // -- CREA UN NUOVO MESSAGGIO -------------------------------------------------------------------------
    public void create(Message messageToSave){
        /*
            salva un nuovo messaggio sul sb

            Input:
                    messageToSave   :   messaggio da salvare
                    Message
         */
        messageRepository.save(messageToSave);
    }


    // -- CERCA MESSAGGI DI UNA CHAT -----------------------------------------------------------------------
    public List<Message> getMessagesByChat(Chat chat){
        /*
            cerca tutti i messaggi inviati in una chat

            Input:
                    chat            :    chat della quale si vogliono i messaggi
                    Chat

            Output:
                    List<Message>   :   lista di messaggi inviato

         */
        return messageRepository.findByChat(chat);
    }


    //--- CERCA L'ULTIMO MEASSAGGIO -------------------------------------------------------------------
    public String getLastMessageByChat(Chat chat){
        /*
            cerca l'ultimo messaggio di una chat

            Input:
                    chat    :   chat della quale si vuole l'ultimo messaggio
                    Chat

            Output:
                    content :   ultimo messaggio formattato in una stringa
                    String      <username>: <testo>
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
