package de.extremecoffee.basket;

import java.io.Serializable;

public class ItemId implements Serializable {
  public Long bagSizeId;

  public Long productId;

  @Override
  public boolean equals(Object o) {
    Item item = (Item) o;
    if (item.bagSizeId == bagSizeId && item.productId == productId) {
      return true;
    }
    return false;
  }
}
