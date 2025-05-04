package co.edu.uniandes.dse.carmotor.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class UserEntity extends BaseEntity {
    private String name;
    private String email;
    private String identifier;
    private String phone;
    private UserRoleEnum role;

    @PodamExclude
    @OneToMany(fetch = FetchType.LAZY)
    private List<VehicleEntity> vehicles = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TestDriveEntity> testDrives = new ArrayList<>();
}