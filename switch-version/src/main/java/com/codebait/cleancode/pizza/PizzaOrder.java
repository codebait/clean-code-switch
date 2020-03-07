package com.codebait.cleancode.pizza;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class PizzaOrder {

  private Pizza pizza;
  private boolean takeaway;
}
