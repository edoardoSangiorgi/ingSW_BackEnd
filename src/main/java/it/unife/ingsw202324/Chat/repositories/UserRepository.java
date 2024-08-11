package it.unife.ingsw202324.Chat.repositories;

import it.unife.ingsw202324.Chat.models.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member, Long> {

}
