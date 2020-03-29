package com.codebait.cleancode.pizza.promotion;

import com.codebait.cleancode.pizza.Pizza;
import com.codebait.cleancode.pizza.PizzaOrder;
import com.codebait.cleancode.pizza.ReceiptItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

class TwoForOnePromotion implements OrderPromotionApplier {

  private final ReceiptItemCreator receiptItemCreator;

  public TwoForOnePromotion(ReceiptItemCreator receiptItemCreator) {
    this.receiptItemCreator = receiptItemCreator;
  }

  @Override
  public List<ReceiptItem> getReceiptItems(List<PizzaOrder> pizzaOrders) {
    return pizzaOrders.stream()
        .collect(Collectors.groupingBy(PizzaOrder::getPizza, Collectors.toList()))
        .entrySet()
        .stream()
        .flatMap(this::getPizzaWithTwoForOnePromo).collect(Collectors.toList());
  }

  private Stream<ReceiptItem> getPizzaWithTwoForOnePromo(
      Entry<Pizza, List<PizzaOrder>> pizzaListEntry) {
    List<PizzaOrder> pizzaOrders = pizzaListEntry.getValue();

    Builder<ReceiptItem> builder = Stream.builder();
    for (int i = 0; i < pizzaOrders.size(); i++) {
      PizzaOrder pizzaOrder = pizzaOrders.get(i);
      boolean isLast = i == pizzaOrders.size() - 1;
      if (isLast && i != 1) {
        builder.add(receiptItemCreator.getReceiptItem(pizzaOrder));
      } else if (i % 2 == 0) {
        builder.add(receiptItemCreator.getReceiptItem(pizzaOrder,
            pizzaOrder.getPizza().getPrice().subtract(BigDecimal.valueOf(0.1))));
      } else {
        builder.add(receiptItemCreator.getReceiptItem(pizzaOrder, BigDecimal.valueOf(0.1)));
      }
    }

    return builder.build();
  }

}
