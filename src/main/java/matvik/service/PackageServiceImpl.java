package matvik.service;

import matvik.dao.PackageRepository;
import matvik.entity.Delivery.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PackageServiceImpl {
    @Autowired
    PackageRepository packageRepository;

    public List<Package> findAll() {
        return packageRepository.findAll();
    }

    public Package findById(int id) {
        Optional<Package> result = packageRepository.findById(id);
        Package myPackage;
        if(result.isPresent()) myPackage = result.get();
        else {
            throw new RuntimeException("Did not find package id - " + id);
        }
        return myPackage;
    }
    public void save(Package myPackage) {
        packageRepository.save(myPackage);
    }
}
