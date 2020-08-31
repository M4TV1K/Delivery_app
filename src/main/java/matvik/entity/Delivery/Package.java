package matvik.entity.Delivery;

import matvik.entity.People.Customer;
import matvik.entity.Transport.Transport;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    @Column(unique = true)
    private String packageNumber;
    @NotNull
    private String address;


    @ManyToOne
    private Delivery delivery;

    @ManyToOne
    private Customer deliverTo;

    @ManyToOne
    private Customer deliverFrom;

    @OneToMany(mappedBy = "myPackage", cascade = CascadeType.ALL)
    private List<Good> containsGoods = new ArrayList<>();

    public Package() {}

    private Package(String packageNumber, String address, Customer from, Customer to) {
        setPackageNumber(packageNumber);
        setAddress(address);
        setDeliverFrom(from);
        setDeliverTo(to);
    }

    public static Package createPackage(
            Delivery delivery,
            String packageNumber,
            String address,
            Customer from,
            Customer to
    ) {
        if (delivery == null) throw new IllegalArgumentException("The delivery doesn't exist");
        Package myPackage = new Package(packageNumber, address, from, to);
        delivery.addPackage(myPackage);
        return myPackage;
    }

    public int getId() {
        return id;
    }
    private void setId(int id) {
        this.id = id;
    }
    public float getWeight() {
        float weight = 0;
        for (Good good : containsGoods) {
            weight += good.getWeight();
        }
        return weight;
    }

    //ordered constraint
    public void sort() {
        Collections.sort(containsGoods);
    }

    public Customer getDeliverTo() {
        return deliverTo;
    }
    public void setDeliverTo(Customer deliverTo) {
        this.deliverTo = deliverTo;
    }
    public Customer getDeliverFrom() {
        return deliverFrom;
    }
    public void setDeliverFrom(Customer deliverFrom) {
        this.deliverFrom = deliverFrom;
    }

    public Delivery getDelivery() {
        return delivery;
    }
    public void setDelivery(Delivery delivery) {
        if (delivery == null) throw new IllegalArgumentException("Delivery cannot be null");
        if (delivery.getTransport() != null && !delivery.canFitPackage(getWeight())) {
            throw new IllegalArgumentException("This package cannot be assigned to this delivery " +
                    " because it will be overloaded");
        }
        if (this.delivery != null && this.delivery != delivery)  {
            throw new IllegalArgumentException("Package is assigned to another directory");
        }
        this.delivery = delivery;
    }

    public String getPackageNumber() {
        return packageNumber;
    }
    public void setPackageNumber(String packageNumber) {
        Transport.stringChecker(packageNumber);
        this.packageNumber = packageNumber;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        Transport.stringChecker(address);
        this.address = address;
    }

    private List<Good> getContainsGoods() {
        return containsGoods;
    }
    public ArrayList<Good> getCopyContainsGoods() {
        return new ArrayList<>(containsGoods);
    }
    private void setContainsGoods(List<Good> containsGoods) {
        this.containsGoods = containsGoods;
    }

    public void addGood(Good good) {
        if (good == null) throw new IllegalArgumentException("Good should exist");
        if (good.getMyPackage() != null) {
            if (containsGoods.contains(good)) System.out.println("Good is already assigned to this package");
            else throw new IllegalArgumentException("Good is already assigned to another package");
        }
        else {
            if (delivery != null && !delivery.canFitPackage(good.getWeight())) {
                throw new IllegalArgumentException("This package cannot fit this good " +
                        "because delivery will be overloaded");
            }
            else  {
                containsGoods.add(good);
                sort();
                good.setMyPackage(this);
            }
        }
    }
    public void removeGood(Good good) {
        if (good != null && containsGoods.contains(good)) {
            containsGoods.remove(good);
            good.deleteGood();
        }
        // if (containsGoods.isEmpty()) deletePackage();
    }

    public String getAddressAndReceiver() {
        return address + " - " + deliverTo.getFullName() + " (free: " + delivery.getFreeSpace() + ")";
    }

    public void deletePackage() {
        if (delivery != null) {
            Delivery d = delivery;
            delivery = null;
            d.removePackage(this);
        }
        if (!containsGoods.isEmpty()) {
            for (Good good : containsGoods) {
                removeGood(good);
            }
        }
    }

    @Override
    public String toString() {
        return "Package{" +
                "packageNumber='" + packageNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
