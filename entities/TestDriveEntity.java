package co.edu.uniandes.dse.carmotor.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class TestDriveEntity extends BaseEntity{
    @Temporal(TemporalType.DATE)
    private Date date;

    @PodamExclude
    @ManyToOne
    private UserEntity user;

    @PodamExclude
    @ManyToMany(fetch = FetchType.LAZY)
    private List<VehicleEntity> vehicles = new ArrayList<>();
}