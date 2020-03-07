package com.codebait.cleancode.pizza;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Receipt {

  private List<ReceiptItem> items;
  private BigDecimal totalPrice;
}
