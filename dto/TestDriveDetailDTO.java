package co.edu.uniandes.dse.carmotor.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TestDriveDetailDTO extends TestDriveDTO {
    private List<UserDTO> users = new ArrayList<>();
    private List<VehicleDTO> vehicles = new ArrayList<>();
}