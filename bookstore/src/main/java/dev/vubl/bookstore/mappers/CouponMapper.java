package dev.vubl.bookstore.mappers;

import dev.vubl.bookstore.dtos.CouponDTO;
import dev.vubl.bookstore.entities.Coupon;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CouponMapper {
  CouponMapper INSTANCE = Mappers.getMapper(CouponMapper.class);

  CouponDTO toDto(Coupon c);

  Coupon toEntity(CouponDTO dto);
}
