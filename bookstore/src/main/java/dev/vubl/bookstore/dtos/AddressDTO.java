package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record AddressDTO(Integer id, String city, String commune, String street) {}
