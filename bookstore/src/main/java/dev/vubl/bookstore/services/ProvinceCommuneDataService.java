package dev.vubl.bookstore.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vubl.bookstore.dtos.Commune;
import dev.vubl.bookstore.dtos.Province;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ProvinceCommuneDataService {
  @Value("classpath:communes.json")
  private Resource communeRes;

  @Value("classpath:provinces.json")
  private Resource provinceRes;

  private List<Commune> communeList;
  private List<Province> provinceList;

  @PostConstruct
  public void init() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    // Deserialize JSON array into List<Ward>
    communeList = objectMapper.readValue(communeRes.getInputStream(), new TypeReference<>() {});
    provinceList = objectMapper.readValue(provinceRes.getInputStream(), new TypeReference<>() {});
  }

  public List<Commune> getCommunesOfProvince(String provinceCode) {
    return communeList.stream().filter(item -> item.provinceCode().equals(provinceCode)).toList();
  }

  public List<Province> getAllProvinces() {
    return provinceList;
  }
}
