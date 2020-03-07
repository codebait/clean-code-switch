package com.codebait.cleancode.pizza;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PizzaOrder {

  private Pizza pizza;
  private boolean takeaway;
}
