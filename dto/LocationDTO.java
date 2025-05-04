package co.edu.uniandes.dse.carmotor.dto;

import lombok.Data;

@Data
public class LocationDTO {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String schedule;
}