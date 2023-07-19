package de.extremecoffee.basket;

import java.io.Serializable;

public class ItemId implements Serializable {
  public Long bagSizeId;

  public Long productId;

  @Override
  public boolean equals(Object o) {
    Item item = (Item) o;
    if (bagSizeId == this.bagSizeId && productId == this.productId) {
      return true;
    }
    return false;
  }
}
