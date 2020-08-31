package matvik.service;

import matvik.dao.GoodRepository;
import matvik.entity.Delivery.Good;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodServiceImpl {
    @Autowired
    private GoodRepository goodRepository;

    public void save(Good good) {
        goodRepository.save(good);
    }
}
