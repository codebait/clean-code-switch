package com.codebait.cleancode.pizza.promotion;

import java.math.BigDecimal;
import java.time.DayOfWeek;

public class OrderPromotionApplierManager {

  private final ReceiptItemCreator receiptItemCreator;

  public OrderPromotionApplierManager(BigDecimal packagePrice) {
    this.receiptItemCreator = new ReceiptItemCreator(packagePrice);
  }


  public OrderPromotionApplier getOrderHandler(DayOfWeek dayOfWeek) {
    return switch (dayOfWeek) {
      case MONDAY -> new AllPizzaSamePricePromotion(receiptItemCreator);
      case WEDNESDAY -> new TwoForOnePromotion(receiptItemCreator);
      case FRIDAY -> new FreePackagePromotion(receiptItemCreator);
      default -> new NoDiscountPromotion(receiptItemCreator);
    };
  }
}
