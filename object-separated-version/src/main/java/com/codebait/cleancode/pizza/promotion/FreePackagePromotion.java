package com.codebait.cleancode.pizza.promotion;

import com.codebait.cleancode.pizza.PizzaOrder;
import com.codebait.cleancode.pizza.ReceiptItem;
import java.util.List;
import java.util.stream.Collectors;

class FreePackagePromotion implements OrderPromotionApplier {

  private final ReceiptItemCreator receiptItemCreator;

  public FreePackagePromotion(ReceiptItemCreator receiptItemCreator) {
    this.receiptItemCreator = receiptItemCreator;
  }


  @Override
  public List<ReceiptItem> getReceiptItems(List<PizzaOrder> pizzaOrders) {
    return pizzaOrders.stream()
        .map(PizzaOrder::getPizza)
        .map(receiptItemCreator::getReceiptItem)
        .collect(Collectors.toList());
  }
}
