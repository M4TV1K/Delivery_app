package matvik.controller;

import matvik.entity.Delivery.Delivery;
import matvik.entity.Delivery.Good;
import matvik.entity.Delivery.Package;
import matvik.entity.Delivery.Warehouse;
import matvik.entity.People.Customer;
import matvik.service.GoodServiceImpl;
import matvik.service.PackageServiceImpl;
import matvik.service.WarehouseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class GoodController {
    @Autowired
    private WarehouseServiceImpl warehouseService;
    @Autowired
    private PackageServiceImpl packageService;
    @Autowired
    private GoodServiceImpl goodService;

    @GetMapping("/add_good")
    public String createGood(Model thModel) {
        /*Warehouse warehouse1 = new Warehouse("Gieldowa 25", 1000000f);
        Warehouse warehouse2 = new Warehouse("Pocztowa 35", 10000f);
        Warehouse warehouse3 = new Warehouse("Marynowa 45", 1000f);
        warehouseService.save(warehouse1);
        warehouseService.save(warehouse2);
        warehouseService.save(warehouse3);*/

        List<Warehouse> warehouses = warehouseService.findAll();
        List<Package> packages = packageService.findAll();
        packages.removeIf(pack -> pack.getDelivery().isDelivered());
        Good good = new Good();
        thModel.addAttribute("error", null);
        thModel.addAttribute("warehouses", warehouses);
        thModel.addAttribute("packages", packages);
        thModel.addAttribute("good", good);
        return "add_good";
    }

    @PostMapping("/saveGood")
    public String saveGood(
            @ModelAttribute("good") Good good,
            @RequestParam("warehouse") String warehouseId,
            @RequestParam("package") String myPackageId,
            Model thModel
    ) {
        try {
            Warehouse warehouse = warehouseService.findById(Integer.parseInt(warehouseId));
            Package pack = packageService.findById(Integer.parseInt(myPackageId));
            warehouse.addGood(good);
            pack.addGood(good);
            goodService.save(good);
        } catch (IllegalArgumentException e) {
            List<Warehouse> warehouses = warehouseService.findAll();
            List<Package> packages = packageService.findAll();
            packages.removeIf(pack -> pack.getDelivery().isDelivered());
            thModel.addAttribute("error", null);
            thModel.addAttribute("warehouses", warehouses);
            thModel.addAttribute("packages", packages);
            thModel.addAttribute("good", good);
            return "add_good";
        }
        return "redirect:/";
    }
}
