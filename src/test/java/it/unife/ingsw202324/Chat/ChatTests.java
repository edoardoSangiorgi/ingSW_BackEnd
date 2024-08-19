package it.unife.ingsw202324.Chat;

import it.unife.ingsw202324.Chat.models.DTOs.*;
import it.unife.ingsw202324.Chat.models.entities.Chat;
import it.unife.ingsw202324.Chat.models.entities.Member;
import it.unife.ingsw202324.Chat.models.entities.Message;
import it.unife.ingsw202324.Chat.models.entities.User;
import it.unife.ingsw202324.Chat.services.ChatService;
import it.unife.ingsw202324.Chat.services.MemberService;
import it.unife.ingsw202324.Chat.services.MessageService;
import it.unife.ingsw202324.Chat.services.TemplateRestConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ChatTests {

	@Autowired
	ChatService chatService;
	@Autowired
	MemberService memberService;
	@Autowired
	MessageService messageService;
	@Autowired
	TemplateRestConsumer templateRestConsumer;

	@Test
	void createChatTest() {

		System.out.println("Loading resources...");

		Chat chat = new Chat(
				null,
				"Event-2",
				"group",
				false,
				LocalDate.of(2024, 5, 23),
				null,
				 null
		);

		chatService.create(chat);

		System.out.println("members creation...");
		// Creiamo i membri
		Member member1 = new Member("user4", "Matteo", "Rossi", LocalDate.of(2002, 7, 31), false, true, chat);
		Member member2 = new Member("user2", "Sofia", "Verdi", LocalDate.of(2002, 7, 31), false, false, chat);
		Member member3 = new Member("user5", "Alessandro", "Bianchi", LocalDate.of(2002, 7, 31), false, false, chat);
		Member member4 = new Member("selfuser", "Tu", "Tu", LocalDate.of(2002, 7, 31), false, true, chat);

		// Creiamo una lista e aggiungiamo i membri
		List<Member> members = new ArrayList<>();
		members.add(member1);
		members.add(member2);
		members.add(member3);
		members.add(member4);

		
		System.out.println("chat saving...");

		for(Member member: members){
			memberService.create(member);
		}

		chat.setMembers(members);
		chatService.update(chat);

		System.out.println("chat saved.");




	}

	@Test
	void getChatTest(){
		//-- cerca la chat
		Chat foundChat = chatService.getChatByNameOrId("Event-1");
		//-- cerca i membri
		List<Member> foundMembers = memberService.getMembersByChat(foundChat);
		List<MemberDTO> members = memberService.convertListToDTO(foundMembers);
		//-- cerca i messaggi
		List<Message> foundMessages = messageService.getMessagesByChat(foundChat);
		List<MessageDTO> messages = messageService.convertListToDTO(foundMessages);


		ChatDTO convertedChat = chatService.convertToDTO(foundChat);
		convertedChat.setMembers(members);
		convertedChat.setMessages(messages);
		convertedChat.setEvent(null);

		System.out.println("reading completed.\n");
	}

	@Test
	void createChatWithDTOTest(){
		System.out.println("Loading resources...");

		// Creiamo i membri
		MemberDTO member1 = new MemberDTO("user7", "Nicola", "Rossi", LocalDate.of(2002, 7, 31), false, false);
		MemberDTO member2 = new MemberDTO("user8", "Margherita", "Verdi", LocalDate.of(2002, 7, 31), false, true); // admin
		MemberDTO member3 = new MemberDTO("user9", "Anna", "Bianchi", LocalDate.of(2002, 7, 31), false, false);

		// Creiamo una lista e aggiungiamo i membri
		List<MemberDTO> members = new ArrayList<>();
		members.add(member1);
		members.add(member2);
		members.add(member3);

		ChatDTO chat = new ChatDTO(
				"Event-3",
				"group",
				LocalDate.of(2024, 5, 23),
				members,
				null,
				new EventDTO(
						"description",
						"wedding",
						18,
						"Ferrara",
						LocalDateTime.of(2024, 8,30, 20, 30),
						LocalDateTime.of(2024, 8,31, 0,0)
				)
		);



		System.out.println("chat saving...");

		Chat convertedChat = chatService.convertFromDTO(chat);
		chatService.create(convertedChat);

		for(MemberDTO member: chat.getMembers()){
			memberService.create(memberService.convertFromDTO(member, convertedChat));
		}


		System.out.println("chat saved.");
	}

	@Test
	void getChatListTest(){

		//-- recupero le chat dal db
		List<Chat> allChats = chatService.getAll();

		//-- conversione
		List<BasicChatDTO> results = chatService.convertListToBasicDTO(allChats);
		System.out.println("read completed.\n");

	}

	@Test
	void addUserTest(){


		Chat chat = chatService.getChatByNameOrId("Event-1");
		MemberDTO memberToAdd = new MemberDTO("selfuser", "Tu", "Tu", LocalDate.of(2002, 7, 31), false, true);

		//-- Aggiungi l'utente alla chat
		memberService.add(memberService.convertFromDTO(memberToAdd, chat));

		System.out.println("Updated.");
	}

	@Test
	void removeUser(){

		//-- conversione in model obj
		Chat chat = chatService.getChatByNameOrId("Event-1");
		MemberDTO memberToRemove = new MemberDTO("user10", "Alessandro", "Bianchi", LocalDate.of(2002, 7, 31), false, false);

		Member member = memberService.convertFromDTO(memberToRemove, chat);
		memberService.remove(member);

		System.out.println("Updated.");
	}

	@Test
	void createMessageTest(){

		Chat chat = chatService.getChatByNameOrId("Event-1");
		MessageDTO request = new MessageDTO(
				"come state?",
				"selfuser",
				LocalDateTime.of(2024, 8, 13, 13, 45)
		);
		Member sender = memberService.getMemberByUsernameAndChat(request.getSender(), chat);
		Message messageToAdd = messageService.convertFromDTO(request, chat, sender);

		messageService.create(messageToAdd);

		System.out.println("updated.");
	}

	@Test
	void getUsersTest(){
		/*
            Input:
                    resourceName:   indirizzo risorsa mockoon
         */
		String uriBaseMock = "http://localhost:3000/";
		String resourceName = "users";

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<User[]> response = restTemplate.getForEntity(uriBaseMock + "available-users", User[].class);
		List<User> users = Arrays.asList(response.getBody());

		System.out.println("completed.");

	}

}
