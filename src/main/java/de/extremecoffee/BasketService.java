package de.extremecoffee;

import de.extremecoffee.basket.Basket;
import de.extremecoffee.basket.BasketItem;
import de.extremecoffee.basket.Item;
import de.extremecoffee.basket.ItemId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class BasketService {

  @Transactional
  Basket updateItemInBasket(String userName, List<ItemDto> itemDtoList) {
    Basket basket = Basket.findByUserName(userName);
    if (basket == null) {
      basket = new Basket();
      basket.userName = userName;
      basket.persist();
    }

    // Returns null if one of the items doesnt exist
    for (var itemDto : itemDtoList) {
      var itemId = new ItemId();
      itemId.productId = itemDto.productId();
      itemId.bagSizeId = itemDto.bagSizeId();
      Item item = Item.findById(itemId);
      if (item == null) {
        return null;
      }
    }

    for (var itemDto : itemDtoList) {
      var itemId = new ItemId();
      itemId.productId = itemDto.productId();
      itemId.bagSizeId = itemDto.bagSizeId();
      Item item = Item.findById(itemId);

      var basketItemsInBasket =
              basket.basketItems.stream().filter(basketItem -> basketItem.item.equals(item)).toList();

      if (!basketItemsInBasket.isEmpty() && itemDto.quantity() == 0) {
        var basketItem = basketItemsInBasket.get(0);
        basket.basketItems.remove(basketItem);
        basketItem.delete();
      }

      if (basketItemsInBasket.isEmpty() && (itemDto.quantity() > 0)) {
        BasketItem basketItem = new BasketItem();
        basketItem.quantity = itemDto.quantity();
        basketItem.item = item;
        basketItem.basket = basket;
        basketItem.persist();
        basket.basketItems.add(basketItem);
      }

      if (!basketItemsInBasket.isEmpty() && (itemDto.quantity() > 0)) {
        basketItemsInBasket.get(0).quantity = itemDto.quantity();
      }
    }
    return Basket.findById(basket.id);
  }

  Basket getBasket(String userName) {
    return Basket.findByUserName(userName);
  }

  @Transactional
  Basket deleteItemFromBasket(String userName, ItemId itemId) {
    Basket basket = Basket.findByUserName(userName);
    Item item = Item.findById(itemId);

    if (item == null) {
      return null;
    }

    var basketItemsInBasket =
            basket.basketItems.stream().filter(basketItem -> basketItem.item.equals(item)).toList();
    if (!basketItemsInBasket.isEmpty()) {
      var basketItem = basketItemsInBasket.get(0);
      basket.basketItems.remove(basketItem);
      basketItem.delete();
    }
    return basket;
  }
}
