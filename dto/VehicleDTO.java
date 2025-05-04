package co.edu.uniandes.dse.carmotor.dto;

import lombok.Data;

@Data
public class VehicleDTO {
    private Long id;
    private String brand;
    private String series;
    private String lastPlateDigit;
    private String model;
    private String type;
    private String capacity;
    private Double price;
}