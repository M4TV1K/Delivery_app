package matvik.entity.Transport;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Truck extends Transport {
    @NotNull
    private int amountOfWheels;
    @NotNull
    private String truckType;

    public Truck() {}

    public Truck(String producer, boolean available, float pricePerDay, float fuelConsumption, float maxLoad, float maxVolume, float maxFuelVolume, int amountOfWheels, String truckType) {
        super(producer, available, pricePerDay, fuelConsumption, maxLoad, maxVolume, maxFuelVolume);
        setAmountOfWheels(amountOfWheels);
        setTruckType(truckType);
    }

    public int getAmountOfWheels() {
        return amountOfWheels;
    }
    public void setAmountOfWheels(int amountOfWheels) {
        if (amountOfWheels <= 3) throw new IllegalArgumentException("Amount of wheels must be bigger than 3");
        this.amountOfWheels = amountOfWheels;
    }

    public String getTruckType() {
        return truckType;
    }
    public void setTruckType(String truckType) {
        Transport.stringChecker(truckType);
        this.truckType = truckType;
    }
}
