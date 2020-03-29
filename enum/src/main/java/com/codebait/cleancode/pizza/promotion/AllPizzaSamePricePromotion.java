package com.codebait.cleancode.pizza.promotion;

import com.codebait.cleancode.pizza.Pizza;
import com.codebait.cleancode.pizza.PizzaOrder;
import com.codebait.cleancode.pizza.ReceiptItem;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class AllPizzaSamePricePromotion implements OrderPromotionApplier {

  private final ReceiptItemCreator receiptItemCreator;

  AllPizzaSamePricePromotion(ReceiptItemCreator receiptItemCreator) {
    this.receiptItemCreator = receiptItemCreator;
  }

  @Override
  public List<ReceiptItem> getReceiptItems(List<PizzaOrder> pizzaOrders) {
    BigDecimal min = pizzaOrders.stream().map(PizzaOrder::getPizza).map(Pizza::getPrice)
        .min(Comparator.naturalOrder())
        .orElseThrow(() -> new IllegalArgumentException("Pizza order is empty."));
    return pizzaOrders.stream()
        .map(pizzaOrder -> receiptItemCreator.getReceiptItem(pizzaOrder, min))
        .collect(Collectors.toList());
  }
}
