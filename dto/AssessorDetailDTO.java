package co.edu.uniandes.dse.carmotor.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AssessorDetailDTO extends AssessorDTO {
    private List<VehicleDTO> vehicles = new ArrayList<>();
}