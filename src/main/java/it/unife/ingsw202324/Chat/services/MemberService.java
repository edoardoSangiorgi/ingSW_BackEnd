package it.unife.ingsw202324.Chat.services;

import it.unife.ingsw202324.Chat.models.DTOs.MemberDTO;
import it.unife.ingsw202324.Chat.models.entities.Chat;
import it.unife.ingsw202324.Chat.models.entities.Member;
import it.unife.ingsw202324.Chat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<MemberDTO> convertListToDTO(List<Member> listToConvert){
        /*
            converte una lista di Member in MemberDTO
         */
        List<MemberDTO> convertedList = new ArrayList<>();
        for(Member member: listToConvert){
            convertedList.add(convertToDTO(member));
        }

        return convertedList;
    }

    public Member convertFromDTO(MemberDTO memberDTO, Chat chat){

        return new Member(
                memberDTO.getUsername(),
                memberDTO.getName(),
                memberDTO.getSurname(),
                memberDTO.getBirthDate(),
                memberDTO.getDeleted(),
                memberDTO.getAdmin(),
                chat
        );

    }



    //### METODI ##########################################################################################

    public void create(Member memberToSave){
        userRepository.save(memberToSave);
    }

    public List<Member> getMembersByChat(Chat chat){
        return userRepository.findAllById_ChatAndDeletedIsFalse(chat);
    }

    //--- AGGIUNGI UTENTE ----------------------------------------------------------------------------------
    public void add(Member memberToAdd){
//        Chat chat = memberToAdd.getChat();
//        chat.addMember(memberToAdd);
        create(memberToAdd);
    }


    //--- RIMUOVI UTENTE -----------------------------------------------------------------------------------
    public void remove(Member memberToDelete){
        memberToDelete.setDeleted(true);
        create(memberToDelete);
    }

    public Member getMemberByName(String username){
        return userRepository.findMemberById_Username(username);
    }


}
