package it.unife.ingsw202324.Chat.services;


import it.unife.ingsw202324.Chat.models.DTOs.ChatDTO;
import it.unife.ingsw202324.Chat.models.DTOs.EventDTO;
import it.unife.ingsw202324.Chat.models.DTOs.MessageDTO;
import it.unife.ingsw202324.Chat.models.DTOs.MemberDTO;
import it.unife.ingsw202324.Chat.models.entities.Chat;
import it.unife.ingsw202324.Chat.models.entities.Message;
import it.unife.ingsw202324.Chat.models.entities.Member;
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



    //### CONVERSIONE ###########################################################################################À
    public Chat convertFromDTO(ChatDTO chatDTO){
        /*
            converte:
            - chat
            - membri
            - messaggi
         */

        Chat chat = new Chat(
                null,
                chatDTO.getChatName(),
                chatDTO.getType(),
                false,
                chatDTO.getCreationDate(),
                null,
                null
        );

        List<Member> members = new ArrayList<>();
        List<MemberDTO> memberDTOList = chatDTO.getMembers();
        for(MemberDTO memberDTO: memberDTOList) {
            members.add(memberService.convertFromDTO(memberDTO, chat));
        }

        List<Message> messages = new ArrayList<>();
        List<MessageDTO> messageDTOList = chatDTO.getMessages();
        for(MessageDTO messageDTO: messageDTOList){
            messages.add(messageService.convertFromDTO(messageDTO, chat));
        }

        chat.setMembers(members);
        chat.setMessages(messages);

        return chat;
    }

    public ChatDTO convertToDTO(Chat chat){

        //-- conversione messaggi
        List<Message> chatMessages = chat.getMessages();
        List<MessageDTO> chatMessagesDTO = new ArrayList<>();
        for(Message message: chatMessages){
            chatMessagesDTO.add(messageService.convertToDTO(message));
        }

        //-- conversione utenti membri
        List<Member> chatMembers = chat.getMembers();
        List<MemberDTO> chatMembersDTO = new ArrayList<>();
        for(Member member: chatMembers){
            chatMembersDTO.add(memberService.convertToDTO(member));
        }

        EventDTO event = new EventDTO();
        //-- TODO: API per recuperare l'evento

        //-- conversione
        return new ChatDTO(
                chat.getName(),
                chat.getType(),
                chat.getCreationDate(),
                chatMembersDTO,
                chatMessagesDTO,
                event
        );


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
        return chatRepository.findByName(chatName)
                .orElseThrow(() -> new RuntimeException("Chat not found"));


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
    public void addUser(Member memberToAdd, Chat chat){

        chat.addMember(memberToAdd);
        memberService.create(memberToAdd);
    }


    //--- RIMUOVI UTENTE -----------------------------------------------------------------------------------
    public void removeMember(Member member, Chat chat){
        chat.removeMember(member);
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
