package co.edu.uniandes.dse.carmotor.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class VehicleDetailDTO extends VehicleDTO {
    private List<PhotoDTO> photos = new ArrayList<>();
    private List<BankingDTO> banks = new ArrayList<>();
    private List<InsurancePolicyDTO> insurancePolicies = new ArrayList<>();
    private List<MaintenanceHistoryDTO> maintenanceHistories = new ArrayList<>();
    private List<AssessorDTO> assessors = new ArrayList<>();
    private List<TestDriveDTO> testDrives = new ArrayList<>();
}