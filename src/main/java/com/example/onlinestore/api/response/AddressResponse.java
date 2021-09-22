package com.example.onlinestore.api.response;

import com.example.onlinestore.model.Address;
import lombok.Data;

@Data
public class AddressResponse {
    private Long addresssId;
    private String fullAddress;

    public AddressResponse(Address address) {
        this.addresssId = address.getId();
        this.fullAddress = getFullAddress(address);
    }

    private String getFullAddress(Address address) {
        StringBuilder addressBuilder = new StringBuilder();
        addressBuilder.append(address.getAddressLine1());
        addressBuilder.append(", ");
        addressBuilder.append(address.getAddressLine2());
        addressBuilder.append(", ");
        addressBuilder.append(address.getPostcode());

        return addressBuilder.toString();
    }
}
