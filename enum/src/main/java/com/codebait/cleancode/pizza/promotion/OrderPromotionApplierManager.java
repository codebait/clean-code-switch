package com.codebait.cleancode.pizza.promotion;

import java.math.BigDecimal;
import java.time.DayOfWeek;

public class OrderPromotionApplierManager {

  private final ReceiptItemCreator receiptItemCreator;

  public OrderPromotionApplierManager(BigDecimal packagePrice) {
    this.receiptItemCreator = new ReceiptItemCreator(packagePrice);
  }

  public OrderPromotionApplier getOrderApplier(DayOfWeek dayOfWeek) {
    return getOrderApplier(WorkingDay.of(dayOfWeek));
  }

  private OrderPromotionApplier getOrderApplier(WorkingDay workingDay) {
    return workingDay.getPromotionHandlerFunction().apply(receiptItemCreator);
  }
}
