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
        // Member ---> MemberDTO
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
            Lista di Member ---> Lista di MemberDTO
         */
        List<MemberDTO> convertedList = new ArrayList<>();
        for(Member member: listToConvert){
            convertedList.add(convertToDTO(member));
        }

        return convertedList;
    }

    public Member convertFromDTO(MemberDTO memberDTO, Chat chat){
        // MemberDTO ---> Member
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
        // Lista di MemberDTO ---> Lista di Member
        List<Member> convertedList = new ArrayList<>();
        for(MemberDTO memberToConvert: listToConvert){
            convertedList.add(convertFromDTO(memberToConvert, chat));
        }

        return convertedList;
    }

    public Member convertFromUser(User userToConvert, Chat chat){
        // User ---> Member
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
        // Lista di User ---> Lista di Member
        List<Member> convertedList = new ArrayList<>();
        for(User userToConvert: listToConvert){
            convertedList.add(convertFromUser(userToConvert, chat));
        }

        return convertedList;
    }

    public User convertToUser(Member memberToConvert){
        // Member ---> User
        return new User(
                memberToConvert.getUsername(),
                memberToConvert.getName(),
                memberToConvert.getSurname(),
                memberToConvert.getBirthDate()
        );
    }

    public List<User> convertToUserList(List<Member> listToConvert){
        // Lista di Member ---> Lista di User
        List<User> convertedList = new ArrayList<>();
        for(Member memberToConvert: listToConvert){
            convertedList.add(convertToUser(memberToConvert));
        }

        return convertedList;
    }


    //### METODI ##########################################################################################

    public void create(Member memberToSave){
        /*
            dalva un nuovo membro sul db

            Input:
                    memberToSave    :   membro da salvare
                    Member
         */
        userRepository.save(memberToSave);
    }

    //--- CREA UNA LISTA DI UTENTI -----------------------------------------------------------------
    public void createList(List<Member> membersToCreate){
        /*
            salva sul db una lista di membri

            Input:
                    membersToCreate     :   membri da salvare
                    List<Member>
         */
        for(Member memberToCreate: membersToCreate){
            create(memberToCreate);
        }
    }


    //--- LEGGI MEMBRI DI UNA CHAT --------------------------------------------------------------------------
    public List<Member> getMembersByChat(Chat chat){
        /*
            legge dal db tutti i membri di una chat

            Input:
                    chat            :   chat della quale si voglio i membri
                    Chat

            Output:
                    List<Member>    :   lista dei membri
         */
        return userRepository.findAllById_ChatAndDeletedIsFalse(chat);
    }


    //--- AGGIUNGI UTENTE ----------------------------------------------------------------------------------
    public void add(Member memberToAdd){
        /*
            salva un nuovo utente sulla chat

            Input:
                    memberToAdd     :   membro da salvare
                    Member
         */
        create(memberToAdd);
    }


    //--- RIMUOVI UTENTE -----------------------------------------------------------------------------------
    public void remove(Member memberToRemove){
        /*
            cancella logicamente un utente dalla chat
            setta deleted = true

            Input:
                    memberToRemove  :   membro da cancellare
                    Member
         */
        memberToRemove.setDeleted(true);
        create(memberToRemove);
    }

    //--- LEGGI UTENTE -------------------------------------------------------------------------------------
    public Member getMemberByUsernameAndChat(String username, Chat chat){
        /*
            legge un membro data la sua chiave primaria

            Input:
                    username    :   username dell'utente
                    String

                    chat        :   chat di cui l'utente fa parte
                    Chat

            Output:
                    Member      :   utente trovato
         */
        Member.MemberPK memberPK = new Member.MemberPK(username, chat);
        return userRepository.findMemberById(memberPK);
    }


}
