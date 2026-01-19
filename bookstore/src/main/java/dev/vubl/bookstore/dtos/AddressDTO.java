package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record AddressDTO(
    Integer id, Integer cityId, String city, Integer communeId, String commune, String street) {}
