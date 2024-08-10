package it.unife.ingsw202324.Chat.repositories;

import it.unife.ingsw202324.Chat.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


// --- CLASSE CHE DEFINISCE IL REPOSITORY DI CHAT ---
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByName(String chatName);


}
