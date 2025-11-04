package dev.vubl.bookstore.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@ToString
@MappedSuperclass
public class BaseEntity {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @CreationTimestamp
  @Column(name = "create_ts")
  @JsonIgnore
  private Instant createTimeStamp;

  @UpdateTimestamp
  @Column(name = "update_ts")
  @JsonIgnore
  private Instant updateTimeStamp;
}
