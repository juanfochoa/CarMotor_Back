package co.edu.uniandes.dse.carmotor.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class BankingEntity extends BaseEntity {
    private String name;
    private String uriLogo;
    private String assessorPhone;
}