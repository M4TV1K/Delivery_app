package matvik.entity.Transport;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Boat extends Transport {
    @NotNull
    private int numberOfEngines;

    public Boat() {}

    public Boat(String producer, boolean available, float pricePerDay, float fuelConsumption, float maxLoad, float maxVolume, float maxFuelVolume, int numberOfEngines) {
        super(producer, available, pricePerDay, fuelConsumption, maxLoad, maxVolume, maxFuelVolume);
        setNumberOfEngines(numberOfEngines);
    }

    public int getNumberOfEngines() {
        return numberOfEngines;
    }
    public void setNumberOfEngines(int numberOfEngines) {
        if (numberOfEngines <= 0) throw new IllegalArgumentException("Number of engines must be bigger than 0");
        this.numberOfEngines = numberOfEngines;
    }

}
