package de.extremecoffee;

import de.extremecoffee.basket.Item;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class StockConsumer {

  @Incoming("stock")
  @Transactional
  public void consume(JsonObject p) {
    StockDto stockDto = p.mapTo(StockDto.class);
    Item item = new Item();
    item.bagSizeId = stockDto.bagSizeId();
    item.productId = stockDto.productId();
    item.quantityInStock = stockDto.quantity();
    if (!item.isPersistent()) {
      item.persist();
    }
  }
}
