package dev.vubl.bookstore.mappers;

import dev.vubl.bookstore.dtos.ShippingInfoDTO;
import dev.vubl.bookstore.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
  OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

  @Mapping(source = "info", target = "note")
  @Mapping(source = "phone", target = "phoneNumber")
  @Mapping(source = "communeName", target = "commune")
  @Mapping(source = "cityName", target = "city")
  Order toOrder(ShippingInfoDTO dto);
}
