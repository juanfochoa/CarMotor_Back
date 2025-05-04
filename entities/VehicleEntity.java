package co.edu.uniandes.dse.carmotor.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class VehicleEntity extends BaseEntity {
    private String brand;
    private String series;
    private String lastPlateDigit;
    private String model;
    private String type;
    private Integer capacity;
    private Double price;

    @PodamExclude
    @OneToMany(fetch = FetchType.LAZY)
    private List<MaintenanceHistoryEntity> maintenances = new ArrayList<>();

    @PodamExclude
    @OneToMany(fetch = FetchType.LAZY)
    private List<InsurancePolicyEntity> insurancePolicies = new ArrayList<>();

    @PodamExclude
    @ManyToMany(mappedBy = "vehicles", fetch = FetchType.LAZY)
    private List<TestDriveEntity> testDrives = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private AssessorEntity assessor;

    @PodamExclude
    @OneToMany(fetch = FetchType.LAZY)
    private List<BankingEntity> banks = new ArrayList<>();

    @PodamExclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoEntity> photos = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private LocationEntity location;
}