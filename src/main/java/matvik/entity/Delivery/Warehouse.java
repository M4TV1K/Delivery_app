package matvik.entity.Delivery;

import matvik.entity.Transport.Transport;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String address;
    private float maxVolume;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.PERSIST) //because I want to save goods with warehouse
    private List<Good> storedGoods = new ArrayList<>();

    public Warehouse(){}
    public Warehouse(String address, float maxVolume) {
        setAddress(address);
        setMaxVolume(maxVolume);
    }

    public int getId() {
        return id;
    }
    private void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        Transport.stringChecker(address);
        this.address = address;
    }
    public float getMaxVolume() {
        return maxVolume;
    }
    public void setMaxVolume(float maxVolume) {
        if (maxVolume < 1000) throw new IllegalArgumentException("Volume cannot be smaller than 1000L");
        this.maxVolume = maxVolume;
    }

    public String getAddressAndLoad() {
        return address + " (free: " + (getMaxVolume() - getCurrentLoad()) + ")";
    }

    public float getCurrentLoad() {
        return storedGoods.stream().map(Good::getVolume).reduce(0.0f, Float::sum);
    }
    public boolean canFitGood(float volume) {
        return getCurrentLoad() + volume <= maxVolume;
    }

    private List<Good> getStoredGoods() {
        return storedGoods;
    }
    public List<Good> getCopyStoredGoods() {
        return new ArrayList<>(storedGoods);
    }
    private void setStoredGoods(List<Good> storedGoods) {
        this.storedGoods = storedGoods;
    }
    public void addGood(Good good) {
        if (good == null) throw new IllegalArgumentException("Good should exist");
        if (this != good.getWarehouse()) {
            if (good.getWarehouse() != null) good.getWarehouse().removeGood(good);
            if (!canFitGood(good.getVolume())) throw new IllegalArgumentException("Warehouse is out of free space!");
            storedGoods.add(good);
            good.setWarehouse(this);
        }
    }
    public void removeGood(Good good) {
        storedGoods.remove(good);
        good.setWarehouse(null);
    }
}
