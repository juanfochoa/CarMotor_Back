package co.edu.uniandes.dse.carmotor.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UserDetailDTO extends UserDTO {
    private List<TestDriveDTO> testDrives = new ArrayList<>();
    private List<VehicleDTO> vehicles = new ArrayList<>();
}