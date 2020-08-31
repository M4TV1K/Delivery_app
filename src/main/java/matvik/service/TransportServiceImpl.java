package matvik.service;

import matvik.dao.TransportRepository;
import matvik.entity.People.Customer;
import matvik.entity.Transport.Transport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransportServiceImpl {
    @Autowired
    TransportRepository transportRepository;

    public List<Transport> findAll() {
        return transportRepository.findAll();
    }
    public Transport findById(int id) {
        Optional<Transport> result = transportRepository.findById(id);
        Transport transport;
        if (result.isPresent()){
            transport = result.get();
        } else{
            throw new RuntimeException("Did not find transport with id - " + id);
        }
        return transport;
    }
    public void save(Transport transport) {
        transportRepository.save(transport);
    }
}
