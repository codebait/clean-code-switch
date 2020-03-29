package com.codebait.cleancode.pizza.promotion;

import java.time.DayOfWeek;
import java.util.function.Function;

enum WorkingDay {
  MONDAY(AllPizzaSamePricePromotion::new),
  WEDNESDAY(TwoForOnePromotion::new),
  FRIDAY(FreePackagePromotion::new),
  DEFAULT(NoDiscountPromotion::new);

  private final Function<ReceiptItemCreator, OrderPromotionApplier> promotionHandlerFunction;

  WorkingDay(Function<ReceiptItemCreator, OrderPromotionApplier> promotionHandlerFunction) {
    this.promotionHandlerFunction = promotionHandlerFunction;
  }

  static WorkingDay of(DayOfWeek dayOfWeek) {
    try {
      return valueOf(dayOfWeek.name());
    } catch (IllegalArgumentException e) {
      return DEFAULT;
    }
  }

  Function<ReceiptItemCreator, OrderPromotionApplier> getPromotionHandlerFunction() {
    return promotionHandlerFunction;
  }
}
