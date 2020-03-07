package com.codebait.cleancode.pizza.promotion;

import com.codebait.cleancode.pizza.Pizza;
import com.codebait.cleancode.pizza.PizzaOrder;
import com.codebait.cleancode.pizza.ReceiptItem;
import java.math.BigDecimal;

class ReceiptItemCreator {

  private final BigDecimal packagePrice;

  ReceiptItemCreator(BigDecimal packagePrice) {
    this.packagePrice = packagePrice;
  }

  ReceiptItem getReceiptItem(PizzaOrder pizzaOrder) {
    Pizza pizza = pizzaOrder.getPizza();
    if (pizzaOrder.isTakeaway()) {
      BigDecimal price = pizza.getPrice().add(packagePrice);
      return new ReceiptItem(pizza.getName(), price, price);
    }
    return getReceiptItem(pizza);
  }

  ReceiptItem getReceiptItem(Pizza pizza) {
    return new ReceiptItem(pizza.getName(), pizza.getPrice(), pizza.getPrice());
  }

  ReceiptItem getReceiptItem(PizzaOrder pizzaOrder, BigDecimal newPricePizza) {
    Pizza pizza = pizzaOrder.getPizza();
    if (pizzaOrder.isTakeaway()) {
      BigDecimal price = pizza.getPrice().add(packagePrice);
      BigDecimal priceAfterDiscount = newPricePizza.add(packagePrice);
      return new ReceiptItem(pizza.getName(), price, priceAfterDiscount);
    }
    return new ReceiptItem(pizza.getName(), pizza.getPrice(), newPricePizza);
  }
}
