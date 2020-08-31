package matvik.entity.People;

import matvik.entity.Transport.Transport;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String phoneNumber;
    private String additionalNumber; // optional

    public Person() {}

    public Person(String firstName, String lastName, String email, String phoneNumber, String additionalNumber) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setAdditionalNumber(additionalNumber);
    }

    public abstract void showInfo();

    public int getId() {
        return id;
    }
    private void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        Transport.stringChecker(firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        Transport.stringChecker(lastName);
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        Transport.stringChecker(email);
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        Transport.stringChecker(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public String getAdditionalNumber() {
        return additionalNumber;
    }
    public void setAdditionalNumber(String additionalNumber) {
        if (additionalNumber != null) {
            if (additionalNumber.length() == 0) throw new IllegalArgumentException("Additional number should have some digits");
            this.additionalNumber = additionalNumber;
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
