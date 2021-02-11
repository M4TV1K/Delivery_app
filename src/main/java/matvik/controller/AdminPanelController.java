package matvik.controller;

import matvik.entity.Delivery.Delivery;
import matvik.entity.Delivery.Good;
import matvik.entity.Delivery.Package;
import matvik.entity.People.Customer;
import matvik.service.DeliveryServiceImpl;
import matvik.service.GoodServiceImpl;
import matvik.service.PackageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class AdminPanelController {
    @Autowired
    DeliveryServiceImpl deliveryService;
    @Autowired
    PackageServiceImpl packageService;
    @Autowired
    GoodServiceImpl goodService;

    @GetMapping("/")
    public String adminPanel(Model thModel) {
        /*List<Delivery> deliveries = deliveryService.findAll();
        for (Delivery delivery : deliveries) {
            for (Package pack : delivery.getCopyPackages()) {
                for (Good good : pack.getCopyContainsGoods()) {
                    System.out.println(good.getTitle() + " " + good.getWarehouse().getAddressAndLoad());
                    good.getWarehouse().removeGood(good);
                    goodService.save(good);
                }
            }
        }*/

        List<Delivery> deliveryList = deliveryService.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        thModel.addAttribute("formatter", formatter);
        thModel.addAttribute("deliveries", deliveryList);
        return "admin_panel";
    }

    @GetMapping("/delivery/{id}")
    public String getDelivery(Model thModel, @PathVariable String id) {
        Delivery delivery = deliveryService.findById(Integer.parseInt(id));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        thModel.addAttribute("delivery", delivery);
        thModel.addAttribute("formatter", formatter);
        return "delivery";
    }

    @GetMapping("/package/{id}")
    public String getPackage(Model thModel, @PathVariable String id) {
        Package pack = packageService.findById(Integer.parseInt(id));
        thModel.addAttribute("pack", pack);
        return "package";
    }
}
