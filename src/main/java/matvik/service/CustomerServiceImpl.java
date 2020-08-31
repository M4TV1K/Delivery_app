package matvik.service;

import matvik.dao.CustomerRepository;
import matvik.entity.People.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl {
    @Autowired
    CustomerRepository customerRepository;

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(int theId) {
        Optional<Customer> result = customerRepository.findById(theId);
        Customer customer = null;
        if(result.isPresent()){
            customer = result.get();
        }else{
            throw new RuntimeException("Did not find customer id - " + theId);
        }
        return customer;
    }
}
