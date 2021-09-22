package com.example.onlinestore.service;

import com.example.onlinestore.model.Address;
import com.example.onlinestore.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public Address findById(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Long saveAddress(Address address) {
        if (address.getId() == null) {
            address.setCreatedDate(new Date());
        }
        address.setLastUpdatedDate(new Date());
        Address newAddress = addressRepository.save(address);
        return newAddress.getId();
    }

    @Transactional
    public void deleteAddressById(Long productId) {
        Address address = addressRepository.findById(productId).orElseThrow(EntityNotFoundException::new);
        deleteAddress(address);
    }

    @Transactional
    public void deleteAddress(Address address) {
        address.setRecordStatus(0);
        saveAddress(address);
    }
}
