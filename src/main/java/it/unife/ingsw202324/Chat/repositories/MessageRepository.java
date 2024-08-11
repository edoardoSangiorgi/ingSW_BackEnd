package it.unife.ingsw202324.Chat.repositories;

import it.unife.ingsw202324.Chat.models.entities.Chat;
import it.unife.ingsw202324.Chat.models.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


// --- CLASSE CHE DEFINISCE IL REPOSITORY DI MESSAGE ---

public interface MessageRepository extends JpaRepository<Message, Long>{
    List<Message> findByChat(Chat chat);

    //-- recupera il messaggio
    Message findFirstByChatIdOrderByTimestampDesc(Long chatId);
}
