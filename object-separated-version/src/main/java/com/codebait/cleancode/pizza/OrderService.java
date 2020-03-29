package com.codebait.cleancode.pizza;

import com.codebait.cleancode.pizza.promotion.OrderPromotionApplierManager;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

class OrderService {

  private final Clock clock;
  private final OrderPromotionApplierManager orderPromotionApplierManager;

  OrderService(Clock clock, BigDecimal packagePrice) {
    this.clock = clock;
    this.orderPromotionApplierManager = new OrderPromotionApplierManager(packagePrice);
  }

  Receipt prepareReceipt(List<PizzaOrder> pizzaOrders) {
    DayOfWeek dayOfWeek = LocalDate.now(clock).getDayOfWeek();
    return getReceipt(
        orderPromotionApplierManager.getOrderHandler(dayOfWeek).getReceiptItems(pizzaOrders));
  }


  private Receipt getReceipt(List<ReceiptItem> receiptItems) {
    BigDecimal totalPrice = receiptItems.stream().map(ReceiptItem::getPriceAfterDiscount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return new Receipt(receiptItems, totalPrice);
  }


}
