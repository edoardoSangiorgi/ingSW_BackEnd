package it.unife.ingsw202324.Chat.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    /*-- mappa il singolo membro della chat --*/

    @EmbeddedId
    private MemberPK id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private Boolean deleted;
    private Boolean admin;

    public Member(String username, String name, String surname, LocalDate birthDate, Boolean deleted, Boolean admin, Chat chat){
        this.id = new MemberPK(username, chat);
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.deleted = deleted;
        this.admin = admin;
    }


    /*
        sottoclasse che serve per creare la chiave primaria

            -   username dell'utente
            -   chat in cui l'utente è memebro
                chat è anche chiave esterna
     */
    @Data
    @Embeddable
    public static class MemberPK implements Serializable{
        @Column(nullable = false)
        private String username;

        @ManyToOne
        @JoinColumn(name = "chat_id")
        private Chat chat;

        public MemberPK() {}

        public MemberPK(String username, Chat chat) {
            this.username = username;
            this.chat = chat;
        }
    }

    public String getUsername(){
        return this.id.getUsername();
    }

    public Chat getChat(){
        return this.id.getChat();
    }

}
