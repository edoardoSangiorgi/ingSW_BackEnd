package it.unife.ingsw202324.Chat.api;

import it.unife.ingsw202324.Chat.models.DTOs.*;
import it.unife.ingsw202324.Chat.models.entities.Chat;
import it.unife.ingsw202324.Chat.models.entities.Member;
import it.unife.ingsw202324.Chat.models.entities.Message;
import it.unife.ingsw202324.Chat.services.ChatService;
import it.unife.ingsw202324.Chat.services.MessageService;
import it.unife.ingsw202324.Chat.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageService messageService;
    @Autowired
    MemberService memberService;


    //### MOCKOON API (REST) ############À#######################################################################

    // -- RICERCA UTENTI DISPONIBILI ---
    @GetMapping("/available-users")
    public List<MemberDTO> getUsers(@RequestBody String username){
        // TODO: mockoon api --> TemplateRestConsumer

        List<MemberDTO> memberDTOList = new ArrayList<>();

        return memberDTOList;
    }


    // -- RICERCA EVENTO A CUI COLLEGARE LA CHAT ---
    @GetMapping("/available-events")
    public List<EventDTO> getEvents(@RequestBody EventDTO eventDTO){
        // TODO: mockoon api --> TemplateRestConsumer
        return null;
    }




    //### CHAT API #############################################################################################

    //--- CREAZIONE NUOVA CHAT ---
    @PostMapping("/chats/create")
    public List<BasicChatDTO> createChat(@RequestBody ChatDTO request) {
        /*
            invoca un servizio per creare una nuova chat

            Input:
                    request     :   contiene tutte le info della chat da creare
                    ChatDTO

            Output:
                    List<ChatDTO>:  tutte le chat
         */
        Chat chatToSave = chatService.convertFromDTO(request);
        chatService.createChat(chatToSave);
        return getChatList();
    }


    //--- LETTURA LISTA CHAT ---
    @GetMapping("/chats")
    public List<BasicChatDTO> getChatList(){

        /*
            Legge tutte le chat presenti

            Output:
                    List<BasicChatDTO>:     una lista di oggetti ridotti per rappresentare la chat
                                            nella lista principale
         */

        //-- risultato query
        List<Chat> allChats = chatService.getAll();

        //-- conversione
        List<BasicChatDTO> allChatDTO = new ArrayList<>();
        for(Chat chat: allChats){
            Message lastMessage = messageService.getLastMessageByChatName(chat.getId());
            String content = lastMessage.getContent();

            //-- creazione oggetto da inviare nella view
            BasicChatDTO basicChatDTO = new BasicChatDTO(
                    chat.getName(),
                    chat.getType(),
                    content
            );

            allChatDTO.add(basicChatDTO);
        }

        return allChatDTO;
    }


    //--- LETTURA SINGOLA CHAT ---
    @GetMapping("/chats/{name}")
    public ChatDTO getChat(@PathVariable String name) {
        /*
            ritorna l'oggetto chat ricercato
            se l'oggetto non esiste -> ritorna null
         */
        try {
            Chat foundChat = chatService.getChatByName(name);
            return chatService.convertToDTO(foundChat);
        } catch (RuntimeException e){
            return null;
        }



    }


    //--- AGGIUNGI UN NUOVO UTENTE ALLA CHAT ---
    @GetMapping("/chats/{name}/add-member")
    public ChatDTO addUser(@RequestBody MemberDTO memberToAdd, @RequestBody ChatDTO chatDTO){

        //-- Conversione in model object
        Chat chat = chatService.convertFromDTO(chatDTO);
        Member member = memberService.convertFromDTO(memberToAdd, chat);


        //-- Aggiungi l'utente alla chat
        chatService.addUser(member, chat);


        //-- restiiusci la chat aggiornata
        return chatService.convertToDTO(chatService.getChatByName(chat.getName()));
    }

    //--- RIMUOVI UTENTE DALLA CHAT ---
    @GetMapping("/chats/{name}/removeUser")
    public ChatDTO removeUser(@RequestBody MemberDTO memberToRemove, @RequestBody ChatDTO chatDTO){

        //-- conversione in model obj
        Chat chat = chatService.convertFromDTO(chatDTO);
        Member member = memberService.convertFromDTO(memberToRemove, chat);

        chatService.removeMember(member, chat);

        return chatService.convertToDTO(chatService.getChatByName(chat.getName()));
    }




    //### MESSAGE API ###########################################################################################

    //--- INVIO MESSAGGIO ---------------------------------------------------------------------------
    @PostMapping("/chats/{chatName}/message")
    public void createMessage(@RequestBody MessageDTO request, @RequestBody ChatDTO chatDTO) {
        /*
            manda un nuovo messaggio
        */

        Chat chat = chatService.convertFromDTO(chatDTO);
        Message messageToAdd = messageService.convertFromDTO(request, chat);

        chatService.addMessage(messageToAdd, chat);

    }
}
