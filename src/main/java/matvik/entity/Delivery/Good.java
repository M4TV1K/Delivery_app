package matvik.entity.Delivery;

import matvik.entity.Transport.Transport;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Good implements Comparable<Good> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    private String title;
    @NotNull
    private float weight;
    @NotNull
    private float volume;
    @NotNull
    private float goodPrice;

    @ManyToOne
    private Warehouse warehouse;

    @ManyToOne
    private Package myPackage;

    public Good() {}
    private Good(String title, float weight, float volume, float goodPrice) {
        setTitle(title);
        setWeight(weight);
        setVolume(volume);
        setGoodPrice(goodPrice);
    }

    public static Good createGood(
            Package myPackage,
            String title,
            float weight,
            float volume,
            float goodPrice)
    {
        if (myPackage == null) throw new IllegalArgumentException("Package cannot be undefined");
        Good good = new Good(title, weight, volume, goodPrice);
        myPackage.addGood(good);
        return good;
    }

    public int getId() {
        return id;
    }
    private void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        Transport.stringChecker(title);
        this.title = title;
    }

    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        if (weight <= 0 || weight > 5000) throw new IllegalArgumentException("Weight cannot be smaller than 0 or bigger than 5000");
        this.weight = weight;
    }

    public float getVolume() {
        return volume;
    }
    public void setVolume(float volume) {
        if (volume <= 0 || volume > 500) throw new IllegalArgumentException("Volume cannot be smaller than 0 or bigger than 500");
        this.volume = volume;
    }

    public float getGoodPrice() {
        return goodPrice;
    }
    public void setGoodPrice(float goodPrice) {
        if (goodPrice < 0 || goodPrice > 1000000) throw new IllegalArgumentException("Good price cannot be smaller than 0 or bigger than 1 000 000");
        this.goodPrice = goodPrice;
    }

    public float getDeliveryPrice() {
        return (goodPrice / 100f) * 5;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }
    public void setWarehouse(Warehouse warehouse) {
        if (warehouse == null) this.warehouse = null;
        else if (this.warehouse != warehouse) {
            if (this.warehouse != null) warehouse.removeGood(this);
            if (!warehouse.canFitGood(volume)) throw new IllegalArgumentException("Warehouse is out of free space!");
            this.warehouse = warehouse;
            warehouse.addGood(this);
        }
    }
    public Package getMyPackage() {
        return myPackage;
    }
    public void setMyPackage(Package myPackage) {
        if (this.myPackage != null && this.myPackage != myPackage)
            throw new IllegalArgumentException("Package is already occupied");
        else if (this.myPackage != myPackage) {
            if (myPackage.getDelivery() != null && !myPackage.getDelivery().canFitPackage(weight)) {
                throw new IllegalArgumentException("This package cannot fit good " +
                        "because delivery will be overloaded");
            }
            this.myPackage = myPackage;
            myPackage.addGood(this);
        }
    }

    public void deleteGood() {
        if (warehouse != null) {
            warehouse.removeGood(this);
            warehouse = null;
        }
        if (myPackage != null) {
            Package p = myPackage;
            myPackage = null;
            p.removeGood(this);
        }
    }

    @Override
    public int compareTo(Good o) {
        return (int) (this.getWeight() - o.getWeight());
    }
}
