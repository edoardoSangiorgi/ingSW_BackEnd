package it.unife.ingsw202324.Chat.repositories;

import it.unife.ingsw202324.Chat.models.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


// --- CLASSE CHE DEFINISCE IL REPOSITORY DI CHAT ---
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByName(String chatName);

    @Query("SELECT DISTINCT c " +
            "FROM Chat c JOIN Member m ON m.id.chat = c " +
            "WHERE m.id.username='selfuser' and m.deleted = false")
    List<Chat> findAllChatsWithNonDeletedSelfUser();

}
