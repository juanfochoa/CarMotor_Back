package co.edu.uniandes.dse.carmotor.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AssessorEntity extends BaseEntity {
    private String name;
    private String uriPhoto;
    private String contactInfo;

    @PodamExclude
    @OneToMany(mappedBy = "assessor", fetch = FetchType.LAZY)
    private List<VehicleEntity> vehicles = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private LocationEntity location;
}