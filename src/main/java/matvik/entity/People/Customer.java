package matvik.entity.People;

import matvik.entity.Delivery.Package;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer extends Person {
    @NotNull
    private int discount;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "deliverTo", cascade = CascadeType.PERSIST)
    List<Package> packagesSent = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "deliverFrom", cascade = CascadeType.PERSIST)
    List<Package> packagesAwaiting = new ArrayList<>();

    public Customer() {}
    public Customer(String firstName, String lastName, String email, String phoneNumber, String additionalNumber, int discount) {
        super(firstName, lastName, email, phoneNumber, additionalNumber);
        this.discount = discount;
    }

    @Override
    public void showInfo() {
        System.out.println("Customer {"
                + getFirstName() + " "
                + getLastName() + " "
                + getEmail() + " "
                + getPhoneNumber() + " "
                + getAdditionalNumber() + " "
                + getDiscount() + "}"
        );
    }

    public int getDiscount() {
        return discount;
    }
    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
