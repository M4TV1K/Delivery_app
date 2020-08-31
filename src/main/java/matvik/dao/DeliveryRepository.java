package matvik.dao;

import matvik.entity.Delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    public Delivery findDeliveryByToCity(String city);
    public List<Delivery> findAllByDeliveredFalse();
}
