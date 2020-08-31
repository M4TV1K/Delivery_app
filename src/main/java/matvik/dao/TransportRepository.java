package matvik.dao;

import matvik.entity.Transport.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransportRepository extends JpaRepository<Transport, Integer> {
    List<Transport> findAll();
}
