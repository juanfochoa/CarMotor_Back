package co.edu.uniandes.dse.carmotor.dto;

import co.edu.uniandes.dse.carmotor.entities.UserRoleEnum;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String identifier;
    private String phone;
    private UserRoleEnum role;
}