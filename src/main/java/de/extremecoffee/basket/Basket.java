package de.extremecoffee.basket;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Basket extends PanacheEntityBase {
  @Id
  @GeneratedValue
  @Column(columnDefinition = "uuid")
  @Schema(readOnly = true)
  public UUID id;

  public String userName;

  @UpdateTimestamp
  public ZonedDateTime updateDate;

  @OneToMany(mappedBy = "basket")
  public Set<BasketItem> basketItems = new HashSet<BasketItem>();

  public static Basket findByUserName(String userName) {
    return find("userName", userName).firstResult();
  }
}
