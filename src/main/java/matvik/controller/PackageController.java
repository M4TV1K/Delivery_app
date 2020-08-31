package matvik.controller;

import matvik.entity.Delivery.Delivery;
import matvik.entity.Delivery.Package;
import matvik.entity.People.Customer;
import matvik.entity.Transport.Transport;
import matvik.service.CustomerServiceImpl;
import matvik.service.DeliveryServiceImpl;
import matvik.service.PackageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Controller
public class PackageController {
    @Autowired
    private PackageServiceImpl packageService;
    @Autowired
    DeliveryServiceImpl deliveryService;
    @Autowired
    private CustomerServiceImpl customerService;

    @GetMapping("/add_package")
    public String createPackage(Model thModel) {
        /*Customer customer1 = new Customer(
                "Roman",
                "Matviichuk",
                "test1@test.com",
                "123 123 123",
                null,
                20
        );
        Customer customer2 = new Customer(
                "Robert",
                "Junior",
                "test2@test.com",
                "4321 123 123",
                "1213123123",
                10
        );
        Customer customer3 = new Customer(
                "Zabka",
                "Zabkovska",
                "test12@test.com",
                "323 123 123",
                null,
                20
        );
        Customer customer4 = new Customer(
                "Myszka",
                "Szyska",
                "test4@test.com",
                "423 123 123",
                null,
                20
        );
        customerService.save(customer1);
        customerService.save(customer2);
        customerService.save(customer3);
        customerService.save(customer4);*/

        List<Customer> customers = customerService.findAll();
        List<Delivery> deliveries = deliveryService.findAllByDeliveredFalse();
        Package myPackage = new Package();
        thModel.addAttribute("error", null);
        thModel.addAttribute("package", myPackage);
        thModel.addAttribute("customers", customers);
        thModel.addAttribute("deliveries", deliveries);
        return "add_package";
    }

    @PostMapping("/savePackage")
    public String savePackage(
            @ModelAttribute("myPackage") Package myPackage,
            @RequestParam("customer") String customer,
            @RequestParam("receiver") String receiver,
            @RequestParam("deliveryID") String deliveryId,
            Model thModel
    ) {
        try {
            Delivery delivery = deliveryService.findById(Integer.parseInt(deliveryId));
            Customer c = customerService.findById(Integer.parseInt(customer));
            Customer r = customerService.findById(Integer.parseInt(receiver));
            myPackage.setDeliverFrom(c);
            myPackage.setDeliverTo(r);
            delivery.addPackage(myPackage);
            packageService.save(myPackage);
        }
        catch (IllegalArgumentException e) {
            List<Customer> customers = customerService.findAll();
            List<Delivery> deliveries = deliveryService.findAllByDeliveredFalse();
            thModel.addAttribute("error", e.getMessage());
            thModel.addAttribute("package", myPackage);
            thModel.addAttribute("customers", customers);
            thModel.addAttribute("deliveries", deliveries);
            return "add_package";
        }
        return "redirect:/";
    }
}
