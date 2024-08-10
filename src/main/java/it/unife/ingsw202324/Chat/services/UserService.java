package it.unife.ingsw202324.Chat.services;

import it.unife.ingsw202324.Chat.DTOs.UserDTO;
import it.unife.ingsw202324.Chat.models.User;
import it.unife.ingsw202324.Chat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    //### CONVERSIONE #####################################################################################

    public UserDTO convertToDTO(User user){

        return new UserDTO(
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.isAdmin(), 
                user.getBirthDate()
        );
        
    }

    public User convertFromDTO(UserDTO userDTO){

        return new User(
                userDTO.getUsername(),
                userDTO.getName(),
                userDTO.getSurname(),
                userDTO.getBirthDate(),
                userDTO.isAdmin(),
                null
        );

    }



    //### METODI ##########################################################################################

    public void create(User userToSave){
        userRepository.save(userToSave);
    }

    public void delete(User userToDelete){

    }


}
