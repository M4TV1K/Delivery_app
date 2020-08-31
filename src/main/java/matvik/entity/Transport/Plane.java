package matvik.entity.Transport;

import matvik.entity.Transport.Transport;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Plane extends Transport {
    @NotNull
    private String planeType;

    public Plane() {}

    public Plane(
            String producer,
            boolean available,
            float pricePerDay,
            float fuelConsumption,
            float maxLoad,
            float maxVolume,
            float maxFuelVolume,
            String planeType
    ) {
        super(producer, available, pricePerDay, fuelConsumption, maxLoad, maxVolume, maxFuelVolume);
        setPlaneType(planeType);
    }

    public String getPlaneType() {
        return planeType;
    }
    public void setPlaneType(String planeType) {
        Transport.stringChecker(planeType);
        this.planeType = planeType;
    }
}
