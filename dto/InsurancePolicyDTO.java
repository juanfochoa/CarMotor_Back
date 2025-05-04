package co.edu.uniandes.dse.carmotor.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class InsurancePolicyDTO {
    private Long id;
    private String price;
    private Date duration;
    private Double premiumRate;
    private String insuranceCompany;
}