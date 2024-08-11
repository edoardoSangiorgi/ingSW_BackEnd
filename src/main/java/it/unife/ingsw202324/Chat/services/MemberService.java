package it.unife.ingsw202324.Chat.services;

import it.unife.ingsw202324.Chat.models.DTOs.MemberDTO;
import it.unife.ingsw202324.Chat.models.entities.Chat;
import it.unife.ingsw202324.Chat.models.entities.Member;
import it.unife.ingsw202324.Chat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    UserRepository userRepository;

    //### CONVERSIONE #####################################################################################

    public MemberDTO convertToDTO(Member member){

        return new MemberDTO(
                member.getUsername(),
                member.getName(),
                member.getSurname(),
                member.getBirthDate(),
                member.getDeleted(),
                member.getAdmin()
        );
        
    }

    public Member convertFromDTO(MemberDTO memberDTO, Chat chat){

        return new Member(
                memberDTO.getUsername(),
                memberDTO.getName(),
                memberDTO.getSurname(),
                memberDTO.getBirthDate(),
                memberDTO.getDeleted(),
                chat,
                memberDTO.getAdmin()
        );

    }



    //### METODI ##########################################################################################

    public void create(Member memberToSave){
        userRepository.save(memberToSave);
    }

    public void delete(Member memberToDelete){
        memberToDelete.setDeleted(true);
        create(memberToDelete);
    }


}
