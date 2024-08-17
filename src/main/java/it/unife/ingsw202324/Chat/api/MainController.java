package it.unife.ingsw202324.Chat.api;

import it.unife.ingsw202324.Chat.models.DTOs.*;
import it.unife.ingsw202324.Chat.models.entities.*;
import it.unife.ingsw202324.Chat.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    // -- RICERCA UTENTI DISPONIBILI ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @GetMapping("/available-users/{name}")
    public List<User> getUsers(@PathVariable String name){

        List<User> usersList = templateRestConsumer.findUsers("available-users");
        if(usersList == null) return null;
        //-- filtraggio secondo il nome (case insensitive)
        return usersList.stream()
                .filter(user -> user.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());

    }


    // -- RICERCA EVENTO A CUI COLLEGARE LA CHAT ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @GetMapping("/available-events/{eventName}")
    public List<Event> getEvents(@PathVariable String eventName){

        List<Event> eventsList = templateRestConsumer.findEvents("available-events");
        if(eventsList == null) return null;
        //-- filtraggio secondo il nome (case insensitive)
        return eventsList.stream()
                .filter(event -> event.getName().toLowerCase().contains(eventName.toLowerCase()))
                .collect(Collectors.toList());
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
            Chat foundChat = chatService.getChatByName(name);
            //-- cerca i membri
            List<Member> foundMembers = memberService.getMembersByChat(foundChat);
            List<MemberDTO> members = memberService.convertListToDTO(foundMembers);
            //-- cerca i messaggi
            List<Message> foundMessages = messageService.getMessagesByChat(foundChat);
            List<MessageDTO> messages = messageService.convertListToDTO(foundMessages);
            //-- cerca l'evento
            List<Event> foundEvent = getEvents(foundChat.getName());

            ChatDTO convertedChat = chatService.convertToDTO(foundChat);
            convertedChat.setMembers(members);
            convertedChat.setMessages(messages);

            if(foundEvent.size() == 1) {
                EventDTO event = eventService.convertToDTO(foundEvent.get(0));
                convertedChat.setEvent(event);
            } else {
                convertedChat.setEvent(null);
            }

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
        Chat chat = chatService.getChatByName(chatName);
        Member member = memberService.convertFromDTO(memberToAdd, chat);

        //-- Aggiungi l'utente alla chat
        memberService.add(member);

        //-- restiiusci la chat aggiornata
        return chatService.convertToDTO(chatService.getChatByName(chat.getName()));
    }


    //--- RIMUOVI UTENTE DALLA CHAT ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @PostMapping("chats/{chatName}/remove-member")
    public ChatDTO removeUser(@RequestBody MemberDTO memberToRemove, @PathVariable String chatName){

        //-- conversione in model obj
        Chat chat = chatService.getChatByName(chatName);
        Member member = memberService.convertFromDTO(memberToRemove, chat);

        memberService.remove(member);

        return chatService.convertToDTO(chatService.getChatByName(chat.getName()));
    }




    //### MESSAGE API ###########################################################################################

    //--- INVIO MESSAGGIO ---------------------------------------------------------------------------
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @PostMapping("/chats/{chatName}/message")
    public void createMessage(@RequestBody MessageDTO request, @PathVariable String chatName) {
        /*
            manda un nuovo messaggio
        */
        Chat chat = chatService.getChatByName(chatName);
        Member sender = memberService.getMemberByName(request.getSender());
        Message messageToAdd = messageService.convertFromDTO(request, chat, sender);

        messageService.create(messageToAdd);
    }
}
