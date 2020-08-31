package matvik.service;

import matvik.dao.DeliveryRepository;
import matvik.entity.Delivery.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryServiceImpl {
    @Autowired
    private DeliveryRepository deliveryRepository;

    public List<Delivery> findAll() {
        return deliveryRepository.findAll();
    }

    public List<Delivery> findAllByDeliveredFalse() {
        return deliveryRepository.findAllByDeliveredFalse();
    }

    public Delivery findById(int id) {
        Optional<Delivery> result = deliveryRepository.findById(id);
        Delivery delivery;
        if(result.isPresent()) delivery = result.get();
        else {
            throw new RuntimeException("Did not find delivery id - " + id);
        }
        return delivery;
    }

    public void save(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

}
