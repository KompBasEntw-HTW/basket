package de.extremecoffee;

import de.extremecoffee.basket.Basket;
import de.extremecoffee.basket.BasketItem;
import de.extremecoffee.basket.Item;
import de.extremecoffee.basket.ItemId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.UUID;

@Transactional
@ApplicationScoped
public class BasketService {

  UUID createNewBasket() {
    var basket = new Basket();
    basket.persist();
    return basket.id;
  }

  Basket updateItemInBasket(UUID basketId, Integer quantity, ItemId itemId) {
    Basket basket = Basket.findById(basketId);
    Item item = Item.findById(itemId);
    if (item == null || basket == null) {
      return null;
    }
    var basketItemsInBasket =
        basket.basketItems.stream().filter(basketItem -> basketItem.item.equals(item)).toList();

    if (!basketItemsInBasket.isEmpty() && quantity == 0) {
      var basketItem = basketItemsInBasket.get(0);
      basket.basketItems.remove(basketItem);
      basketItem.delete();
    }

    if (basketItemsInBasket.isEmpty() && (quantity > 0)) {
      BasketItem basketItem = new BasketItem();
      basketItem.quantity = quantity;
      basketItem.item = item;
      basketItem.basket = basket;
      basketItem.persist();
      basket.basketItems.add(basketItem);
    }

    if (!basketItemsInBasket.isEmpty() && (quantity > 0)) {
      basketItemsInBasket.get(0).quantity = quantity;
    }
    return Basket.findById(basket.id);
  }
}
