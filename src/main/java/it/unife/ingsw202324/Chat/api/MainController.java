package it.unife.ingsw202324.Chat.api;

import it.unife.ingsw202324.Chat.models.DTOs.*;
import it.unife.ingsw202324.Chat.models.entities.*;
import it.unife.ingsw202324.Chat.services.*;
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
    private MemberService memberService;
    @Autowired
    private EventService eventService;
    @Autowired
    private TemplateRestConsumer templateRestConsumer;


    //### MOCKOON API (REST) ####################################################################################

    // --- LETTURA UTENTI ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @GetMapping("/available-users")
    public List<User> getUsers(){
        return templateRestConsumer.findUsers("available-users");
    }


    // --- LETTURA EVENTI ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @GetMapping("/available-events")
    public List<Event> getAvailableEvents(){
        List<Event> events = templateRestConsumer.findEvents("available-events");
        List<BasicChatDTO> chatDTOList = getChatList();

        List<Event> filteredEvents = new ArrayList<>();
        for(Event event: events){
            Boolean isPresent = false;
            for (BasicChatDTO chat: chatDTOList){
                if (event.getName().equals(chat.getName())) isPresent = true;
            }
            if (!isPresent) filteredEvents.add(event);
        }
        return filteredEvents;
    }

    // --- RICERCA EVENTO CHAT ---
    private Event getChatEvent(String eventName){
        List<Event> events = templateRestConsumer.findEvents("available-events");
        return  events.stream()
                .filter(event -> event.getName().equals(eventName))
                .findFirst()
                .orElse(null);
    }


    //### CHAT API #############################################################################################

    //--- CREAZIONE NUOVA CHAT ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
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
        chatService.create(chatToSave);
        return getChatList();
    }


    //--- LETTURA LISTA CHAT ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @GetMapping("/chats")
    public List<BasicChatDTO> getChatList(){

        /*
            Legge tutte le chat presenti

            Output:
                    List<BasicChatDTO>:     una lista di oggetti ridotti per rappresentare la chat
                                            nella lista principale
         */

        //-- recupero le chat dal db
        List<Chat> allChats = chatService.getAll();

        //-- conversione
        return chatService.convertListToBasicDTO(allChats);
    }


    //--- LETTURA SINGOLA CHAT ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @GetMapping("/chats/{name}")
    public ChatDTO getChat(@PathVariable String name) {
        /*
            ritorna l'oggetto chat ricercato
            se l'oggetto non esiste -> ritorna null
         */
        try {
            //-- cerca la chat
            Chat foundChat = chatService.getChatByNameOrId(name);
            //-- cerca i membri
            List<Member> foundMembers = memberService.getMembersByChat(foundChat);
            List<MemberDTO> members = memberService.convertListToDTO(foundMembers);
            //-- cerca i messaggi
            List<Message> foundMessages = messageService.getMessagesByChat(foundChat);
            List<MessageDTO> messages = messageService.convertListToDTO(foundMessages);

            ChatDTO convertedChat = chatService.convertToDTO(foundChat);
            convertedChat.setMembers(members);
            convertedChat.setMessages(messages);

            //-- cerca l'evento
            if(members.size() > 1)
                convertedChat.setEvent(eventService.convertToDTO(
                        getChatEvent(foundChat.getName())
                ));
            else
                convertedChat.setEvent(null);


            return convertedChat;

        } catch (RuntimeException e){
            return null;
        }



    }


    //--- AGGIUNGI UN NUOVO UTENTE ALLA CHAT ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @PostMapping("/chats/{chatName}/add-member")
    public ChatDTO addUser(@RequestBody MemberDTO memberToAdd, @PathVariable String chatName){

        //-- Conversione in model object
        Chat chat = chatService.getChatByNameOrId(chatName);
        Member member = memberService.convertFromDTO(memberToAdd, chat);

        //-- Aggiungi l'utente alla chat
        memberService.add(member);

        //-- restiiusci la chat aggiornata
        return chatService.convertToDTO(chatService.getChatByNameOrId(chat.getName()));
    }


    //--- RIMUOVI UTENTE DALLA CHAT ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @PostMapping("chats/{chatName}/remove-member")
    public ChatDTO removeUser(@RequestBody MemberDTO memberToRemove, @PathVariable String chatName){

        //-- conversione in model obj
        Chat chat = chatService.getChatByNameOrId(chatName);
        Member member = memberService.convertFromDTO(memberToRemove, chat);

        memberService.remove(member);

        return chatService.convertToDTO(chatService.getChatByNameOrId(chat.getName()));
    }




    //### MESSAGE API ###########################################################################################

    //--- INVIO MESSAGGIO ---------------------------------------------------------------------------
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @PostMapping("/chats/{chatName}/new-message")
    public void createMessage(@RequestBody MessageDTO request, @PathVariable String chatName) {
        /*
            manda un nuovo messaggio
        */
        Chat chat = chatService.getChatByNameOrId(chatName);
        Member sender = memberService.getMemberByUsernameAndChat(request.getSender(), chat);
        Message messageToAdd = messageService.convertFromDTO(request, chat, sender);

        messageService.create(messageToAdd);
    }
}
