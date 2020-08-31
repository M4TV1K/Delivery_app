package matvik.entity.People;

import matvik.entity.Transport.Transport;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Worker extends Person {
    @NotNull
    private int salary;

    @ManyToOne
    private Worker manager;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.PERSIST)
    private List<Worker> subordinates = new ArrayList<>();

    @ElementCollection
    private final List<EnumWorker> enumWorkers = new ArrayList<>();

    //Driver
    private String licenseCategory;
    private boolean occupied = false;
    //Manager
    private int maxSubordinates;
    //Loader
    private String insuranceIndex;


    public Worker() {
    }

    private Worker(
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String additionalNumber,
            int salary,
            EnumWorker workerType,
            Worker manager
    ) {
        super(firstName, lastName, email, phoneNumber, additionalNumber);
        setSalary(salary);
        enumWorkers.add(EnumWorker.Worker);
        addWorkerType(workerType);
        setManager(manager);
    }

    //Driver
    public Worker(String firstName, String lastName, String email, String phoneNumber, String additionalNumber,
                  int salary, Worker manager, EnumWorker workerType, String licenseCategory, boolean occupied) {
        this(firstName, lastName, email, phoneNumber, additionalNumber, salary, workerType, manager);
        setLicenseCategory(licenseCategory);
        this.occupied = occupied;
    }

    //Manager
    public Worker(String firstName, String lastName, String email, String phoneNumber, String additionalNumber,
                  int salary, Worker manager, EnumWorker workerType, int maxSubordinates) {
        this(firstName, lastName, email, phoneNumber, additionalNumber, salary, workerType, manager);
        setMaxSubordinates(maxSubordinates);
    }

    //Loader
    public Worker(String firstName, String lastName, String email, String phoneNumber, String additionalNumber,
                  int salary, Worker manager,  EnumWorker workerType, String insuranceIndex) {
        this(firstName, lastName, email, phoneNumber, additionalNumber, salary, workerType, manager);
        this.insuranceIndex = insuranceIndex;
    }

    @Override
    public void showInfo() {
        System.out.print("Worker:"
                + "\nFirst name: " + getFirstName()
                + "\nLast name: " + getLastName()
                + "\nEmail: " + getEmail()
                + "\nPhone number: " + getPhoneNumber()
                + (getAdditionalNumber() != null ? "\nAdditional number: " + getAdditionalNumber() : "")
                + "\nSalary: " + getSalary()
        );
        if (enumWorkers.contains(EnumWorker.Driver)) {
            System.out.print("\nLicense Category: " + getLicenseCategory()
                    + "\nCurrent status: " + (isOccupied() ? "On the way" : "available")
            );
        }
        if (enumWorkers.contains(EnumWorker.Manager)) {
            System.out.print("\nMaximum subordinates: " + getMaxSubordinates());
        }
        if (enumWorkers.contains(EnumWorker.Loader)) {
            System.out.print("\nInsurance index: " + getInsuranceIndex());
        }
        System.out.println();
    }


    //Implementation of overlapping
    public void addWorkerType(EnumWorker enumWorker) {
        if (enumWorker == null) throw new IllegalArgumentException("Worker type cannot be undefined");
        if (!enumWorkers.contains(enumWorker)) enumWorkers.add(enumWorker);
    }

    public void removeWorkerType(EnumWorker enumWorker) {
        if (enumWorker != EnumWorker.Worker) enumWorkers.remove(enumWorker);
    }


    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        if (salary < 500) throw new IllegalArgumentException("Salary cannot be smaller than 0");
        this.salary = salary;
    }

    // DRIVER
    public String getLicenseCategory() {
        if (!enumWorkers.contains(EnumWorker.Driver)) throw new IllegalArgumentException("Worker is not a driver");
        return licenseCategory;
    }

    public void setLicenseCategory(String licenseCategory) {
        if (!enumWorkers.contains(EnumWorker.Driver)) throw new IllegalArgumentException("Worker is not a driver");
        Transport.stringChecker(licenseCategory);
        this.licenseCategory = licenseCategory;
    }

    public boolean isOccupied() {
        if (!enumWorkers.contains(EnumWorker.Driver)) throw new IllegalArgumentException("Worker is not a driver");
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        if (!enumWorkers.contains(EnumWorker.Driver)) throw new IllegalArgumentException("Worker is not a driver");
        this.occupied = occupied;
    }

    // MANAGER
    public int getMaxSubordinates() {
        if (!enumWorkers.contains(EnumWorker.Manager)) throw new IllegalArgumentException("Worker is not a manager");
        return maxSubordinates;
    }

    public void setMaxSubordinates(int maxSubordinates) {
        if (!enumWorkers.contains(EnumWorker.Manager)) throw new IllegalArgumentException("Worker is not a manager");
        if (maxSubordinates <= 0 || maxSubordinates > 25)
            throw new IllegalArgumentException("Max number of subordinates should be bigger than 0 and smaller than 25");
        this.maxSubordinates = maxSubordinates;
    }

    // LOADER
    public String getInsuranceIndex() {
        if (!enumWorkers.contains(EnumWorker.Loader)) throw new IllegalArgumentException("Worker is not a loader");
        return insuranceIndex;
    }

    public void setInsuranceIndex(String insuranceIndex) {
        if (!enumWorkers.contains(EnumWorker.Loader)) throw new IllegalArgumentException("Worker is not a loader");
        this.insuranceIndex = insuranceIndex;
    }

    public boolean notManager() {
        return !enumWorkers.contains(EnumWorker.Manager);
    }
    public Worker getManager() {
        return manager;
    }

    public void setManager(Worker manager) {
        if (enumWorkers.contains(EnumWorker.Manager)) { // CUSTOM RESTRAIN
            if (manager != null) throw new IllegalArgumentException("Manager cannot have manager above him");
            return;
        }
        if (manager == null) throw new IllegalArgumentException("Manager cannot be undefined");
        if (manager.notManager()) throw new IllegalArgumentException(getLastName() + " is not a manager");
        if (manager != this.manager) {
            if (this.manager != null) this.manager.removeSubordinate(this);
            this.manager = manager;
            manager.addSubordinate(this);
        }
    }
    public void unsetManager(Worker manager) {
        if (this.manager != null) {
            manager.removeSubordinate(this);
            this.manager = null;
        }
    }

    private List<Worker> getSubordinates() {
        return subordinates;
    }
    public List<Worker> getCopySubordinates() {
        return new ArrayList<>(subordinates);
    }
    private void setSubordinates(List<Worker> subordinates) {
        this.subordinates = subordinates;
    }
    public void addSubordinate(Worker worker) {
        if (worker == null) throw new IllegalArgumentException("Worker cannot be undefined");
        if (notManager()) throw new IllegalArgumentException(getLastName() + " is not a manager");
        if (worker.getManager() != null && worker.getManager() != this)
            throw new IllegalArgumentException("Worker already has another manager");

        if (!subordinates.contains(worker)) {
            subordinates.add(worker);
            worker.setManager(this);
        }
    }

    public void removeSubordinate(Worker worker) {
        if (subordinates.contains(worker)) {
            subordinates.remove(worker);
            worker.unsetManager(this);
        }
    }
}


        /* TODO TEST OF THE WORKER
        @Autowired
        private WorkerServiceImpl workerService;
        Worker manager = new Worker(
                "Roman",
                "Matviichuk",
                "some@some.some",
                "1222113",
                null,
                100000,
                null,
                EnumWorker.Manager,
                25
        );
        Worker driver = new Worker(
                "Bakan",
                "Chuk",
                "some1@1some1.some",
                "124113",
                "3123312",
                1000,
                manager,
                EnumWorker.Driver,
                "License123",
                false
        );
        manager.showInfo();
        driver.showInfo();
        System.out.println("AFTER DB");
        workerService.save(manager);
        workerService.save(driver);*/