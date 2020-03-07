package com.codebait.cleancode.pizza.promotion;

import java.math.BigDecimal;
import java.time.DayOfWeek;

public class OrderPromotionHandlerManager {

  private final ReceiptItemCreator receiptItemCreator;

  public OrderPromotionHandlerManager(BigDecimal packagePrice) {
    this.receiptItemCreator = new ReceiptItemCreator(packagePrice);
  }


  public OrderPromotionHandler getOrderHandler(DayOfWeek dayOfWeek) {
    return switch (dayOfWeek) {
      case MONDAY -> new AllPizzaSamePricePromotion(receiptItemCreator);
      case WEDNESDAY -> new TwoForOnePromotion(receiptItemCreator);
      case FRIDAY -> new FreePackagePromotion(receiptItemCreator);
      default -> new NoDiscountPromotion(receiptItemCreator);
    };
  }
}
