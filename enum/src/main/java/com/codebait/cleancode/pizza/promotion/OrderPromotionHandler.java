package com.codebait.cleancode.pizza.promotion;

import com.codebait.cleancode.pizza.PizzaOrder;
import com.codebait.cleancode.pizza.ReceiptItem;
import java.util.List;

public interface OrderPromotionHandler {

  List<ReceiptItem> getReceiptItems(List<PizzaOrder> pizzaOrders);
}
