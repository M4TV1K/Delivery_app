package matvik.entity.Delivery;

import matvik.entity.Transport.Transport;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    private String fromCity;
    @NotNull
    private String toCity;
    @NotNull
    private LocalDateTime departureTime;
    @NotNull
    private LocalDateTime arrivalTime;
    @NotNull
    private boolean delivered;

    @ManyToOne
    private Transport transport;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private List<Package> packages = new ArrayList<>();

    public Delivery() {}

    public Delivery(String fromCity, String toCity, LocalDateTime departureTime, LocalDateTime arrivalTime, boolean delivered) {
        setFromCity(fromCity);
        setToCity(toCity);
        setDepartureTime(departureTime);
        setArrivalTime(arrivalTime);
        setDelivered(delivered);
    }

    public int getId() {
        return id;
    }
    private void setId(int id) {
        this.id = id;
    }

    public float getWeight() {
        float weight = 0;
        for (Package p : packages) {
            weight += p.getWeight();
        }
        return weight;
    }
    public boolean canFitPackage(float weight) {
        if (transport != null) return getWeight() + weight < transport.getMaxLoad();
        return true;
    }

    public float getFreeSpace() {
        if (transport != null) {
            return transport.getMaxLoad() - getWeight();
        }
        return -1;
    }

    public String getFromCity() {
        return fromCity;
    }
    public void setFromCity(String fromCity) {
        Transport.stringChecker(fromCity);
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }
    public void setToCity(String toCity) {
        Transport.stringChecker(toCity);
        this.toCity = toCity;
    }

    private static LocalDateTime createTimeOfString(String dateTime) {
        String[] split = dateTime.split("T");
        String[] date = split[0].split("-");
        String[] time = split[1].split(":");
        return LocalDateTime.of(
                Integer.parseInt(date[0]),
                Integer.parseInt(date[1]),
                Integer.parseInt(date[2]),
                Integer.parseInt(time[0]),
                Integer.parseInt(time[1])
        );
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(String departureTime) {
        setDepartureTime(createTimeOfString(departureTime));
    }
    public void setDepartureTime(LocalDateTime departureTime) {
        if (departureTime == null) throw new IllegalArgumentException("Departure time must be set");
        if (arrivalTime != null && departureTime.isAfter(arrivalTime))
            throw new IllegalArgumentException("Departure time cannot be after Arrival time");
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(String arrivalTime) {
        setArrivalTime( createTimeOfString(arrivalTime));
    }
    public void setArrivalTime(LocalDateTime arrivalTime) {
        if (arrivalTime == null) throw new IllegalArgumentException("Arrival time must be set");
        if (departureTime != null && arrivalTime.isBefore(departureTime))
            throw new IllegalArgumentException("Arrival time cannot be before Departure time");
        this.arrivalTime = arrivalTime;
    }

    public boolean isDeliveryDateIntersected(Delivery delivery) {
        if (delivery == null) throw new IllegalArgumentException("Delivery must exist");
        if (delivery.departureTime.isBefore(departureTime) && delivery.arrivalTime.isBefore(arrivalTime)) {
            System.out.println("Completely before");
            return false;
        }
        if (delivery.departureTime.isAfter(departureTime) && delivery.arrivalTime.isAfter(arrivalTime)) {
            System.out.println("Completely after");
            return false;
        }
        System.out.println("intersected");
        return true;
    }

    public boolean isDelivered() {
        return delivered;
    }
    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getPath() {
        return fromCity + " - " + toCity;
    }

    public void setTransport(Transport transport) {
        if (transport == null) throw new IllegalArgumentException("Transport should be initialized");
        if (this.transport != transport && this.transport != null && delivered)
            throw new IllegalArgumentException("Delivery is already delivered");
        if (this.transport != transport) {
            transport.addDelivery(this);
            this.transport = transport;
        }

    }
    public Transport getTransport() {
        return transport;
    }

    public String getTransportAndFreeSpace() {
        if (transport != null) {
            return transport.getTypeAndProduct() + " (free: " + getFreeSpace() + ")";
        }
        return "";
    }

    private List<Package> getPackages() {
        return packages;
    }
    public List<Package> getCopyPackages() {
        return new ArrayList<>(packages);
    }
    private void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    public void addPackage(Package myPackage) {
        if (myPackage == null) throw new IllegalArgumentException("Package must exist");
        if (myPackage.getDelivery() != null) {
            if (packages.contains(myPackage))
                System.out.println("Package is already assigned to this delivery");
            else
                throw new IllegalArgumentException("Package is already assigned to another delivery");
        }
        else {
            if (transport != null && !canFitPackage(myPackage.getWeight())) {
                throw new IllegalArgumentException("This package cannot be assigned to this delivery " +
                        " because it will be overloaded");
            }
            packages.add(myPackage);
            myPackage.setDelivery(this);
        }
    }
    public void removePackage(Package myPackage) {
        if (myPackage != null && packages.contains(myPackage)) {
            packages.remove(myPackage);
            myPackage.deletePackage();
        }
        // if (packages.isEmpty()) deleteDelivery();
    }
    public void deleteDelivery() {
        if (transport != null) {
            Transport t = transport;
            transport = null;
            t.removeDelivery(this);
        }
        if (!packages.isEmpty()) {
            for (Package p : packages) {
                removePackage(p);
            }
        }
    }
}
