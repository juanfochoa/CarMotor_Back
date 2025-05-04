package co.edu.uniandes.dse.carmotor.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class MaintenanceHistoryDTO {
    private Long id;
    private Date date;
    private String type;
    private String address;
}