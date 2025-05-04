package co.edu.uniandes.dse.carmotor.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
    
@Data
@Entity
public class MaintenanceHistoryEntity extends BaseEntity {
	private String type;
	private String address;

	@Temporal(TemporalType.DATE)
    private Date date;
}   