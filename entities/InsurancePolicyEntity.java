package co.edu.uniandes.dse.carmotor.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
public class InsurancePolicyEntity extends BaseEntity {
    private Double price;
    private Double premiumRate;
    private String insuranceCompany;

    @Temporal(TemporalType.DATE)
    private Date duration;
}