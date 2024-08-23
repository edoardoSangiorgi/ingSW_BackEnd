package it.unife.ingsw202324.Chat.services;

import it.unife.ingsw202324.Chat.models.DTOs.MemberDTO;
import it.unife.ingsw202324.Chat.models.entities.Chat;
import it.unife.ingsw202324.Chat.models.entities.Member;
import it.unife.ingsw202324.Chat.models.entities.User;
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

    public List<Member> convertListFromDTO(List<MemberDTO> listToConvert, Chat chat){
        List<Member> convertedList = new ArrayList<>();
        for(MemberDTO memberToConvert: listToConvert){
            convertedList.add(convertFromDTO(memberToConvert, chat));
        }

        return convertedList;
    }

    public Member convertFromUser(User userToConvert, Chat chat){
        return new Member(
                userToConvert.getUsername(),
                userToConvert.getName(),
                userToConvert.getSurname(),
                userToConvert.getBirthDate(),
                false,
                false,
                chat
        );
    }

    public List<Member> convertFromUserList(List<User> listToConvert, Chat chat){
        List<Member> convertedList = new ArrayList<>();
        for(User userToConvert: listToConvert){
            convertedList.add(convertFromUser(userToConvert, chat));
        }

        return convertedList;
    }

    public User convertToUser(Member memberToConvert){
        return new User(
                memberToConvert.getUsername(),
                memberToConvert.getName(),
                memberToConvert.getSurname(),
                memberToConvert.getBirthDate()
        );
    }

    public List<User> convertToUserList(List<Member> listToConvert){
        List<User> convertedList = new ArrayList<>();
        for(Member memberToConvert: listToConvert){
            convertedList.add(convertToUser(memberToConvert));
        }

        return convertedList;
    }


    //### METODI ##########################################################################################

    public void create(Member memberToSave){
        userRepository.save(memberToSave);
    }

    //--- CREA UNA LISTA DI UTENTI -----------------------------------------------------------------
    public void createList(List<Member> membersToCreate){
        for(Member memberToCreate: membersToCreate){
            create(memberToCreate);
        }
    }

    public List<Member> getMembersByChat(Chat chat){
        return userRepository.findAllById_ChatAndDeletedIsFalse(chat);
    }

    //--- AGGIUNGI UTENTE ----------------------------------------------------------------------------------
    public void add(Member memberToAdd){
        create(memberToAdd);
    }


    //--- RIMUOVI UTENTE -----------------------------------------------------------------------------------
    public void remove(Member memberToDelete){
        memberToDelete.setDeleted(true);
        create(memberToDelete);
    }

    public Member getMemberByUsernameAndChat(String username, Chat chat){
        Member.MemberPK memberPK = new Member.MemberPK(username, chat);
        return userRepository.findMemberById(memberPK);
    }


}
