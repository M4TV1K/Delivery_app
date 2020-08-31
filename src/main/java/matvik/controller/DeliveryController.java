package matvik.controller;

import matvik.entity.Delivery.Delivery;
import matvik.entity.Delivery.Package;
import matvik.entity.Transport.Boat;
import matvik.entity.Transport.Plane;
import matvik.entity.Transport.Transport;
import matvik.entity.Transport.Truck;
import matvik.service.DeliveryServiceImpl;
import matvik.service.PackageServiceImpl;
import matvik.service.TransportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class DeliveryController {
    @Autowired
    DeliveryServiceImpl deliveryService;
    @Autowired
    TransportServiceImpl transportService;

    @GetMapping("/add_delivery")
    public String createDelivery(Model thModel) {
        /*Transport transport = new Plane(
                "Boeing",
                true,
                1000f,
                100f,
                100000f,
                10000f,
                1000f,
                "Cargo"
        );
        Transport transport1 = new Truck(
                "Mercedes",
                true,
                500f,
                20f,
                50000f,
                100000f,
                200f,
                8,
                "Very long long truck"
        );
        Transport transport2 = new Boat(
                "Carrier",
                true,
                1000f,
                100f,
                1000000f,
                1000000f,
                1000f,
                2
        );
        transportService.save(transport);
        transportService.save(transport1);
        transportService.save(transport2);*/

        List<Transport> transports = transportService.findAll();
        Delivery delivery = new Delivery();
        thModel.addAttribute("error", null);
        thModel.addAttribute("delivery", delivery);
        thModel.addAttribute("transports", transports);
        return "add_delivery";
    }

    @PostMapping("/saveDelivery")
    public String saveDelivery(
            Model thModel,
            @ModelAttribute("delivery") Delivery delivery,
            @RequestParam("departure") String departure,
            @RequestParam("arrival") String arrival,
            @RequestParam("transportId") String transportId
    ) {
        try {
            delivery.setDepartureTime(departure);
            delivery.setArrivalTime(arrival);
            Transport transport = transportService.findById(Integer.parseInt(transportId));
            delivery.setTransport(transport);
            deliveryService.save(delivery);
        } catch (IllegalArgumentException e) {
            List<Transport> transports = transportService.findAll();
            thModel.addAttribute("error", e.getMessage());
            thModel.addAttribute("delivery", delivery);
            thModel.addAttribute("transports", transports);
            return "add_delivery";
        }
        return "redirect:/";
    }

    @GetMapping("/mark_delivery/{id}")
    public String markDelivery(@PathVariable String id) {
        Delivery delivery = deliveryService.findById(Integer.parseInt(id));
        delivery.setDelivered(true);
        deliveryService.save(delivery);
        return "redirect:/delivery/" + id;
    }
}
