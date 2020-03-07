package com.codebait.cleancode.pizza;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceiptItem {

  private String name;
  private BigDecimal price;
  private BigDecimal priceAfterDiscount;
}
