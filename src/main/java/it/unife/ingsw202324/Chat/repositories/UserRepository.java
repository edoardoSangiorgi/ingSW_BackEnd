package it.unife.ingsw202324.Chat.repositories;

import it.unife.ingsw202324.Chat.models.entities.Chat;
import it.unife.ingsw202324.Chat.models.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Member, Member.MemberPK> {

     List<Member> findAllById_ChatAndDeletedIsFalse(Chat chat);
     Member findMemberById(Member.MemberPK memberPK);

}
