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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    //--- LETTURA TUTTI GLI UTENTI DISPONIBILI ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @GetMapping("/available-users")
    public List<User> getAvailableUsers(){
        return templateRestConsumer.findUsers("available-users");
    }


    // --- LETTURA UTENTI DISPONIBILI PER UNA CHAT ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @GetMapping("/{chatName}/available-users")
    public List<User> getAvailableUsers(@PathVariable String chatName){
        /*
            Input:
                    chatName:       nome della chat
                    String

            Output:
                    List<Users>:    lista di tutti gli utenti disponibli
                                    (quelli già presenti non vengono inclusi)
        */
        List<User> users = templateRestConsumer.findUsers("available-users");

        /*
            recupero gli utenti dal db che sono collegati a quella chat
            e filtro gli utenti disponibili in base a quelli che non sono membri
        */
        Chat chat = chatService.getChatByName(chatName);
        List<Member> membersFromDB = memberService.getMembersByChat(chat);
        List<User> usersFromDB = memberService.convertToUserList(membersFromDB);

        //-- lascio solamente gli elementi unici
        Set<User> set = new HashSet<>(users);
        set.addAll(usersFromDB);

        return new ArrayList<>(set);

    }


    //--- RICERCA UTENTE ---
    private User getUser(String username){
        /*
            cerca un utente specifico

            Input:
                    username    :   username dell'utente (parametro di ricerca)
                    String

            Output:
                    User        :   utente trovato
                    null        :   utente non trovato
         */
        List<User> users = templateRestConsumer.findUsers("available-users");
        return  users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }


    // --- LETTURA EVENTI ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @GetMapping("/available-events")
    public List<Event> getAvailableEvents(){
        /*
            legge tutti gli eventi disponibili
            ovvero quelli che non sono collegati a nessuna chat

            Output:
                    List<Event>:    lista degli eventi
                                    (in questo caso l'attributo nome è incluso)
         */
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
        /*
            ricerca un evento specifico

            Input:
                    eventName   :   nome dell'evento (parametro di ricerca)
                    String

            Output:
                    Event       :   evento trovato
                    null        :   evento non trovato
         */
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

        //-- recupero tutti gli eventi
        List<Event> events = templateRestConsumer.findEvents("available-events");

        // Estraggo tutti i nomi degli eventi in un Set per una ricerca più efficiente
        Set<String> eventNames = events.stream()
                .map(Event::getName) // Estrae il campo nome di ciascun evento
                .collect(Collectors.toSet());


        // Filtra la lista allChats:
        // - Se la chat è di tipo "group", verifica se il nome è presente negli eventi
        // - Altrimenti, aggiungila direttamente alla lista filtrata
        List<Chat> filteredChats = allChats.stream()
                .filter(chat ->
                        // Filtra solo le chat di tipo "group"
                        !"group".equals(chat.getType()) || eventNames.contains(chat.getName()) // Mantiene tutte le altre chat
                )
                .toList();


        //-- conversione
        return chatService.convertListToBasicDTO(filteredChats);
    }


    //--- LETTURA SINGOLA CHAT ---
    @CrossOrigin(origins = "http://localhost:5173") //indirizzo del frontend
    @GetMapping("/chats/{name}")
    public ChatDTO getChat(@PathVariable String name){
        /*
            ritorna l'oggetto chat ricercato

            Input:
                    name    :   nome della chat
                    String

            Output:
                    ChatDTO :   chat trovata
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
                List<MemberDTO> members = new ArrayList<>();
                members.add(selfUser);
                members.add(member);

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
                convertedChat.setEvent(
                        eventService.convertToDTO(
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
        /*
            aggiunge un nuovo utente alla chat

            Input:
                    memberToAdd     :   utente da aggiungere
                    MemberDTO

                    chatName        :   nome della chat a cui aggiungere l'utente
                    String

            Output:
                    ChatDTO         :   chat aggiornata
         */

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
        /*
            rimuove un utente dalla chat

            Input:
                    memberToRemove  :   utente da rimuovere
                    MemberDTO

                    chatName        :   nome della chat da cui rimuovere l'utente
                    String

            Output:
                    ChatDTO         :   chat aggiornata
         */

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
        request.setAdmin(true);
        Member member = memberService.convertFromDTO(request, chat);

        memberService.create(member);
    }


    //--- ESCI DALLA CHAT (per l'utente selfuser) ---
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

            Input:
                    request     :   messaggio da aggiungere
                    MessageDTO

                    chatName    :   nome della chat in cui il messaggio è stato inviato
                    String
        */
        Chat chat = chatService.getChatByName(chatName);
        Member sender = memberService.getMemberByUsernameAndChat(request.getSender(), chat);
        Message messageToAdd = messageService.convertFromDTO(request, chat, sender);
        messageService.create(messageToAdd);
    }
}
