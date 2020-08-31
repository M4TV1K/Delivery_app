package matvik.entity.Transport;

import matvik.entity.Delivery.Delivery;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    private String producer;
    @NotNull
    private boolean available;
    @NotNull
    private float pricePerDay;
    @NotNull
    private float fuelConsumption;
    @NotNull
    private float maxLoad;
    @NotNull
    private float maxVolume;
    @NotNull
    private float maxFuelVolume;
    @Transient
    private static final int MIN_MAX_LOAD = 1000;

    @OneToMany(mappedBy = "transport", cascade = CascadeType.PERSIST)
    private List<Delivery> deliveryList = new ArrayList<>();


    public Transport() {}

    public Transport(String producer, boolean available, float pricePerDay, float fuelConsumption, float maxLoad, float maxVolume, float maxFuelVolume) {
        setProducer(producer);
        setAvailable(available);
        setPricePerDay(pricePerDay);
        setFuelConsumption(fuelConsumption);
        setMaxLoad(maxLoad);
        setMaxVolume(maxVolume);
        setMaxFuelVolume(maxFuelVolume);
    }

    // derived attribute
    public float getMaxDistance() {
        return maxFuelVolume / fuelConsumption;
    }

    public int getId() {
        return id;
    }
    private void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id cannot be smaller than 1");
        this.id = id;
    }

    public String getProducer() {
        return producer;
    }
    public void setProducer(String producer) {
        Transport.stringChecker(producer);
        this.producer = producer;
    }

    public String getType() {
        String[] type = getClass().toString().split("\\.");
        return type[type.length - 1];
    }

    public String getInfo() {
        return getType() + " " + producer + " " + maxLoad;
    }

    public String getTypeAndProduct() {
        return getType() + " " + producer;
    }

    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public float getPricePerDay() {
        return pricePerDay;
    }
    public void setPricePerDay(float pricePerDay) {
        if (pricePerDay < 0) throw new IllegalArgumentException("Price per day cannot be smaller than 0");
        this.pricePerDay = pricePerDay;
    }

    public float getFuelConsumption() {
        return fuelConsumption;
    }
    public void setFuelConsumption(float fuelConsumption) {
        if (fuelConsumption < 5) throw new IllegalArgumentException("Fuel consumption cannot be smaller than 5L/hr");
        this.fuelConsumption = fuelConsumption;
    }

    public float getMaxLoad() {
        return maxLoad;
    }
    public void setMaxLoad(float maxLoad) {
        if (maxLoad < MIN_MAX_LOAD) throw new IllegalArgumentException("Max load cannot be smaller than " +
                + MIN_MAX_LOAD + "kg");
        this.maxLoad = maxLoad;
    }

    public float getMaxVolume() {
        return maxVolume;
    }
    public void setMaxVolume(float maxVolume) {
        if (maxLoad < 100) throw new IllegalArgumentException("Max Volume cannot be smaller than 100L");
        this.maxVolume = maxVolume;
    }

    public float getMaxFuelVolume() {
        return maxFuelVolume;
    }
    public void setMaxFuelVolume(float maxFuelVolume) {
        if (maxLoad < 40) throw new IllegalArgumentException("Max Fuel Volume cannot be smaller than 40L");
        this.maxFuelVolume = maxFuelVolume;
    }

    private List<Delivery> getDeliveryList() {
        return deliveryList;
    }
    public List<Delivery> getCopyDeliveryList() {
        return new ArrayList<>(deliveryList);
    }
    private void setDeliveryList(List<Delivery> deliveryList) {
        this.deliveryList = deliveryList;
    }
    public void addDelivery(Delivery delivery) {
        if (!deliveryList.contains(delivery)) {
            if (delivery == null) throw new IllegalArgumentException("Delivery must be created before assignment");
            if (delivery.getTransport() != null) {
                if (deliveryList.contains(delivery)) System.out.println("Delivery is already assigned to this transport");
                else throw new IllegalArgumentException("Delivery is already assigned to another transport");
            }
            else {
                for (Delivery d : deliveryList) {
                    if (d.isDeliveryDateIntersected(delivery)) {
                        throw new IllegalArgumentException("Transport will be occupied on that time " +
                                "[" + d.getDepartureTime() + " - " + d.getArrivalTime() + "]");
                    }
                }
                deliveryList.add(delivery);
                delivery.setTransport(this);
            }
        }
    }
    public void removeDelivery(Delivery delivery) {
        if (delivery == null) throw new IllegalArgumentException("You cannot remove delivery that doesn't exist");
        deliveryList.remove(delivery);
    }

    public static void stringChecker(String str) {
        if (str == null) throw new IllegalArgumentException("String cannot be null");
        if (str.isEmpty()) throw new IllegalArgumentException("String cannot be empty");
    }
}
