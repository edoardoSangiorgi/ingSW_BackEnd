package it.unife.ingsw202324.Chat;

import it.unife.ingsw202324.Chat.models.DTOs.*;
import it.unife.ingsw202324.Chat.models.entities.Chat;
import it.unife.ingsw202324.Chat.models.entities.Member;
import it.unife.ingsw202324.Chat.models.entities.Message;
import it.unife.ingsw202324.Chat.services.ChatService;
import it.unife.ingsw202324.Chat.services.MemberService;
import it.unife.ingsw202324.Chat.services.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ChatTests {

	@Autowired
	ChatService chatService;
	@Autowired
	MemberService memberService;
	@Autowired
	MessageService messageService;

	@Test
	void createChatTest() {

		System.out.println("Loading resources...");

		Chat chat = new Chat(
				null,
				"Event-1",
				"group",
				false,
				LocalDate.of(2024, 5, 23),
				null,
				 null
		);

		chatService.create(chat);

		System.out.println("members creation...");
		// Creiamo i membri
		Member member1 = new Member("user1", "Nicola", "Rossi", LocalDate.of(2002, 7, 31), false, chat, false);
		Member member2 = new Member("user2", "Margherita", "Verdi", LocalDate.of(2002, 7, 31), false, chat, true); // admin
		Member member3 = new Member("user3", "Anna", "Bianchi", LocalDate.of(2002, 7, 31), false, chat, false);

		// Creiamo una lista e aggiungiamo i membri
		List<Member> members = new ArrayList<>();
		members.add(member1);
		members.add(member2);
		members.add(member3);
		
		
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
		Chat foundChat = chatService.getChatByName("Event-1");
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


		Chat chat = chatService.getChatByName("Event-1");
		MemberDTO memberToAdd = new MemberDTO("user10", "Alessandro", "Bianchi", LocalDate.of(2002, 7, 31), false, false);

		//-- Aggiungi l'utente alla chat
		memberService.add(memberService.convertFromDTO(memberToAdd, chat));

		System.out.println("Updated.");
	}

	@Test
	void removeUser(){

		//-- conversione in model obj
		Chat chat = chatService.getChatByName("Event-1");
		MemberDTO memberToRemove = new MemberDTO("user10", "Alessandro", "Bianchi", LocalDate.of(2002, 7, 31), false, false);

		Member member = memberService.convertFromDTO(memberToRemove, chat);
		memberService.remove(member);

		System.out.println("Updated.");
	}

	@Test
	void createMessageTest(){

		Chat chat = chatService.getChatByName("Event-1");
		MessageDTO request = new MessageDTO(
				"ciao a tutti!",
				"user1",
				LocalDateTime.of(2024, 8, 13, 13, 45)
		);
		Member sender = memberService.getMemberByName(request.getSenderUsername());
		Message messageToAdd = messageService.convertFromDTO(request, chat, sender);

		messageService.create(messageToAdd);

		System.out.println("updated.");
	}

}
