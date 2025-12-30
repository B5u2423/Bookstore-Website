package dev.vubl.bookstore.dtos;

import java.util.List;
import lombok.Builder;

@Builder
public record AccountDetailDTO(
    String email, String name, String phoneNumber, List<AddressDTO> addressList) {}
