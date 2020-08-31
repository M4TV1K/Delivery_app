package matvik.service;

import matvik.dao.WarehouseRepository;
import matvik.entity.Delivery.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseServiceImpl {
    @Autowired
    private WarehouseRepository warehouseRepository;

    public Warehouse findById(int id) {
        Optional<Warehouse> result = warehouseRepository.findById(id);
        Warehouse warehouse;
        if(result.isPresent()) warehouse = result.get();
        else {
            throw new RuntimeException("Did not find warehouse id - " + id);
        }
        return warehouse;
    }

    public void save(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }
}
