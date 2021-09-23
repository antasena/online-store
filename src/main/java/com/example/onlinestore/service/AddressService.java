package com.example.onlinestore.service;

import com.example.onlinestore.exceptionhandling.RecordNotFoundException;
import com.example.onlinestore.model.Address;
import com.example.onlinestore.repository.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Service
@Slf4j
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public Address findById(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Long saveAddress(Address address) {
        log.debug("Saving address");
        if (address.getId() == null) {
            address.setCreatedDate(new Date());
        }
        address.setLastUpdatedDate(new Date());
        Address newAddress = addressRepository.save(address);
        return newAddress.getId();
    }

    @Transactional
    public void deleteAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new RecordNotFoundException(Address.class, addressId));
        deleteAddress(address);
    }

    @Transactional
    public void deleteAddress(Address address) {
        log.debug("Deletring address: ", address.getId());
        address.setRecordStatus(0);
        address.setLastUpdatedDate(new Date());
        saveAddress(address);
    }
}
