package de.extremecoffee.basket;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(ItemId.class)
public class Item extends PanacheEntityBase {
  @Id public Long bagSizeId;

  @Id public Long productId;

  public Integer quantityInStock;

  @Override
  public boolean equals(Object o) {
    Item item = (Item) o;
    if (item.bagSizeId == this.bagSizeId && item.productId == this.productId) {
      return true;
    }
    return false;
  }
}
