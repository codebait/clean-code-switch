package com.codebait.cleancode.pizza.promotion;

import java.time.DayOfWeek;
import java.util.function.Function;

enum PromotionType {
  MONDAY(AllPizzaSamePricePromotion::new),
  WEDNESDAY(TwoForOnePromotion::new),
  FRIDAY(FreePackagePromotion::new),
  BLACK_FRIDAY(AllPizzaSamePricePromotion::new),
  DEFAULT(NoDiscountPromotion::new);

  private final Function<ReceiptItemCreator, OrderPromotionApplier> promotionHandlerFunction;

  PromotionType(Function<ReceiptItemCreator, OrderPromotionApplier> promotionHandlerFunction) {
    this.promotionHandlerFunction = promotionHandlerFunction;
  }

  static PromotionType of(DayOfWeek dayOfWeek) {
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
