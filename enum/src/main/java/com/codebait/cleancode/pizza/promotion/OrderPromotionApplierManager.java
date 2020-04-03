package com.codebait.cleancode.pizza.promotion;

import java.math.BigDecimal;
import java.time.DayOfWeek;

public class OrderPromotionApplierManager {

  private final ReceiptItemCreator receiptItemCreator;

  public OrderPromotionApplierManager(BigDecimal packagePrice) {
    this.receiptItemCreator = new ReceiptItemCreator(packagePrice);
  }

  public OrderPromotionApplier getOrderApplier(DayOfWeek dayOfWeek) {
    PromotionType of = PromotionType.of(dayOfWeek);
    return getOrderApplier(of);
  }

  public OrderPromotionApplier getOrderApplier(PromotionType promotionType) {
    return promotionType.getPromotionHandlerFunction().apply(receiptItemCreator);
  }
}
