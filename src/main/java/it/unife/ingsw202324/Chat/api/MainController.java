package it.unife.ingsw202324.Chat.api;

import it.unife.ingsw202324.Chat.models.DTOs.BasicChatDTO;
import it.unife.ingsw202324.Chat.models.DTOs.ChatDTO;
import it.unife.ingsw202324.Chat.models.DTOs.MemberDTO;
import it.unife.ingsw202324.Chat.models.DTOs.MessageDTO;
import it.unife.ingsw202324.Chat.models.entities.*;
import it.unife.ingsw202324.Chat.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public List<User> getAvailableUsers(){
        return templateRestConsumer.findUsers("available-users");
    }


    //--- RICERCA UTENTE ---
    private User getUser(String username){
        List<User> users = getAvailableUsers();
        return  users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }


    // --- LETTURA EVENTI ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @GetMapping("/available-events")
    public List<Event> getAvailableEvents(){
        List<Event> events = templateRestConsumer.findEvents("available-events");
        List<BasicChatDTO> chatDTOList = getChatList();

        /*
            se un evento corrisponde già ad una chat esistente
            l'evento non viene mostrato
         */
        List<Event> filteredEvents = new ArrayList<>();
        for(Event event: events){
            boolean isPresent = false;
            for (BasicChatDTO chat: chatDTOList){
                if (event.getName().equals(chat.getName())) {
                    isPresent = true;
                    break;
                }
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
    public void createChat(@RequestBody ChatDTO request) {
        /*
            invoca un servizio per creare una nuova chat

            Input:
                    request     :   contiene tutte le info della chat da creare
                    ChatDTO

            Output:
                    List<ChatDTO>:  tutte le chat
         */
        Chat chatToSave = chatService.convertFromDTO(request);
        List<Member> membersToSave = memberService.convertListFromDTO(request.getMembers(), chatToSave);

        chatService.create(chatToSave);
        memberService.createList(membersToSave);

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
    public ChatDTO getChat(@PathVariable String name){
        /*
            ritorna l'oggetto chat ricercato
            se l'oggetto non esiste -> ritorna null
         */
        try {
            //-- cerca la chat
            Chat foundChat = chatService.getChatByName(name);

            //-- in caso di chat non trovata
            if (foundChat == null){
                /*
                    assumiamo che se la chat non è trovata quella che si sta cercando
                    sia una chat privata non ancora esistente
                    creiamo qui i due oggetti MemberDTO e l'oggetto ChatDTO per poi chiamare
                    la funzione
                 */
                User foundUser = getUser(name);
                MemberDTO selfUser = new MemberDTO("selfuser", "Tu", "Tu", LocalDate.of(2002, 8, 29), false, false);
                MemberDTO member = new MemberDTO(foundUser.getUsername(), foundUser.getName(), foundUser.getSurname(), foundUser.getBirthDate(), false, false);
                List<MemberDTO> members = new ArrayList<MemberDTO>();
                members.add(member);
                members.add(selfUser);

                createChat(
                        new ChatDTO(name, "private", LocalDate.now(), members, null, null)
                );
            }

            //-- cerca la chat (stavolta sperando di trovarla)
            foundChat = chatService.getChatByName(name);
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
            if(convertedChat.getType().equals("group"))
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


    //### MEMBERS API ###################################################################################


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


    //--- RENDI UN UTENTE AMMINISTRATORE ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @PostMapping("chats/{chatName}/new-admin")
    public void newAdmin(@RequestBody MemberDTO request, @PathVariable String chatName){
        /*
            rende un utente amministratore
            il controllo è fatto da frontend

            Input:
                    request:    oggetto utente da salvare
                    MemberDTO
         */
        Chat chat = chatService.getChatByName(chatName);
        Member member = memberService.convertFromDTO(request, chat);

        member.setAdmin(true);
        memberService.create(member);
    }


    //--- ESCI DALLA CHAT (per l'utente selfuser)
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @PostMapping("chats/{chatName}/leave-chat")
    public void leaveChat(@PathVariable String chatName){
        /*
            setta selfuser come deleted=true

            Input:
                    chatName:   nome della chat
                    String
         */
        Chat chat = chatService.getChatByName(chatName);
        Member selfuser = memberService.getMemberByUsernameAndChat("selfuser", chat);
        selfuser.setDeleted(true);

        memberService.create(selfuser);
    }



    //### MESSAGE API ###########################################################################################

    //--- INVIO MESSAGGIO ---------------------------------------------------------------------------
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @PostMapping("/chats/{chatName}/new-message")
    public void createMessage(@RequestBody MessageDTO request, @PathVariable String chatName) {
        /*
            manda un nuovo messaggio
        */
        Chat chat = chatService.getChatByName(chatName);
        Member sender = memberService.getMemberByUsernameAndChat(request.getSender(), chat);
        Message messageToAdd = messageService.convertFromDTO(request, chat, sender);
        messageService.create(messageToAdd);
    }
}
