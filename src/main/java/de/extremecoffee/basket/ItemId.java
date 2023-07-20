package de.extremecoffee.basket;

import java.io.Serializable;

public class ItemId implements Serializable {
	public Long bagSizeId;

	public Long productId;

	@Override
	public boolean equals(Object o) {
		ItemId itemId = (ItemId) o;
		if (itemId.bagSizeId == this.bagSizeId && itemId.productId == this.productId) {
			return true;
		}
		return false;
	}
}
