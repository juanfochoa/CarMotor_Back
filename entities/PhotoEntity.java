package co.edu.uniandes.dse.carmotor.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class PhotoEntity extends BaseEntity {
    private String uri;
    private String area;
}