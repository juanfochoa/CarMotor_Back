package co.edu.uniandes.dse.carmotor.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class LocationEntity extends BaseEntity {
    private String name;
    private String address;
    private String phoneNumber;
    private String schedule;

    @PodamExclude
    @OneToMany(fetch=FetchType.LAZY)
    private List<TestDriveEntity> testDrives = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<VehicleEntity> vehicles = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<AssessorEntity> assessors = new ArrayList<>();
}