package com.codebait.cleancode.pizza;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

class OrderService {

  private final Clock clock;
  private final BigDecimal packagePrice;

  OrderService(Clock clock, BigDecimal packagePrice) {
    this.clock = clock;
    this.packagePrice = packagePrice;
  }

  Receipt prepareReceipt(List<PizzaOrder> pizzaOrders) {
    List<ReceiptItem> receiptItems;
    switch (LocalDate.now(clock).getDayOfWeek()) {
      case MONDAY:
        receiptItems = allPizzaSamePrice(pizzaOrders);
        break;
      case WEDNESDAY:
        receiptItems = twoSamePizzaForOne(pizzaOrders);
        break;
      case FRIDAY:
        receiptItems = freePackage(pizzaOrders);
        break;
      default:
        receiptItems = withoutDiscount(pizzaOrders);
    }
    return getReceipt(receiptItems);
  }

  private List<ReceiptItem> twoSamePizzaForOne(List<PizzaOrder> pizzaOrders) {
    return pizzaOrders.stream()
        .collect(Collectors.groupingBy(PizzaOrder::getPizza, Collectors.toList()))
        .entrySet()
        .stream()
        .flatMap(this::getPizzaWithTwoForOnePromo).collect(Collectors.toList());
  }

  private Receipt getReceipt(List<ReceiptItem> receiptItems) {
    BigDecimal totalPrice = receiptItems.stream().map(ReceiptItem::getPriceAfterDiscount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return new Receipt(receiptItems, totalPrice);
  }

  private Stream<ReceiptItem> getPizzaWithTwoForOnePromo(
      Entry<Pizza, List<PizzaOrder>> pizzaListEntry) {
    List<PizzaOrder> pizzaOrders = pizzaListEntry.getValue();

    Builder<ReceiptItem> builder = Stream.builder();
    for (int i = 0; i < pizzaOrders.size(); i++) {
      PizzaOrder pizzaOrder = pizzaOrders.get(i);
      boolean isLast = i == pizzaOrders.size() - 1;
      if (isLast && i != 1) {
        builder.add(getReceiptItem(pizzaOrder));
      } else if (i % 2 == 0) {
        builder.add(getReceiptItem(pizzaOrder,
            pizzaOrder.getPizza().getPrice().subtract(BigDecimal.valueOf(0.1))));
      } else {
        builder.add(getReceiptItem(pizzaOrder, BigDecimal.valueOf(0.1)));
      }
    }

    return builder.build();
  }


  private List<ReceiptItem> freePackage(List<PizzaOrder> pizzaOrders) {
    return pizzaOrders.stream()
        .map(PizzaOrder::getPizza)
        .map(pizza -> new ReceiptItem(pizza.getName(), pizza.getPrice(), pizza.getPrice()))
        .collect(Collectors.toList());
  }

  private List<ReceiptItem> withoutDiscount(List<PizzaOrder> pizzaOrders) {
    return pizzaOrders.stream()
        .map(this::getReceiptItem).collect(Collectors.toList());
  }

  private ReceiptItem getReceiptItem(PizzaOrder pizzaOrder) {
    Pizza pizza = pizzaOrder.getPizza();
    if (pizzaOrder.isTakeaway()) {
      BigDecimal price = pizza.getPrice().add(packagePrice);
      return new ReceiptItem(pizza.getName(), price, price);
    }
    return new ReceiptItem(pizza.getName(), pizza.getPrice(), pizza.getPrice());
  }

  private List<ReceiptItem> allPizzaSamePrice(List<PizzaOrder> pizzaOrders) {
    BigDecimal min = pizzaOrders.stream().map(PizzaOrder::getPizza).map(Pizza::getPrice)
        .min(Comparator.naturalOrder())
        .orElseThrow(() -> new IllegalArgumentException("Pizza order is empty."));
    return pizzaOrders.stream()
        .map(pizzaOrder -> getReceiptItem(pizzaOrder, min)).collect(Collectors.toList());
  }

  private ReceiptItem getReceiptItem(PizzaOrder pizzaOrder, BigDecimal newPricePizza) {
    Pizza pizza = pizzaOrder.getPizza();
    if (pizzaOrder.isTakeaway()) {
      BigDecimal price = pizza.getPrice().add(packagePrice);
      BigDecimal priceAfterDiscount = newPricePizza.add(packagePrice);
      return new ReceiptItem(pizza.getName(), price, priceAfterDiscount);
    }
    return new ReceiptItem(pizza.getName(), pizza.getPrice(), newPricePizza);
  }

}
