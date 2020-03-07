package com.codebait.cleancode.pizza;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

  public static final BigDecimal PACKAGE_PRICE = BigDecimal.valueOf(2);
  public static final Clock MONDAY_CLOCK = Clock
      .fixed(Instant.parse("2020-03-02T12:00:00.00Z"), ZoneOffset.UTC);
  public static final Clock WEDNESDAY_CLOCK = Clock
      .fixed(Instant.parse("2020-03-04T12:00:00.00Z"), ZoneOffset.UTC);
  public static final Clock FRIDAY_CLOCK = Clock
      .fixed(Instant.parse("2020-03-06T12:00:00.00Z"), ZoneOffset.UTC);
  public static final Clock SATURDAY_CLOCK = Clock
      .fixed(Instant.parse("2020-03-07T12:00:00.00Z"), ZoneOffset.UTC);
  public static final String PIZZA_1 = "CARBONARA";
  public static final String PIZZA_2 = "CAPRICIOSA";
  public static final String PIZZA_3 = "AMERICA";
  public static final String PIZZA_4 = "AMERICA HOT";

  @Test
  void shouldPrepareReceiptWithoutDiscountOnSaturday() {
    // given
    OrderService orderService = new OrderService(SATURDAY_CLOCK, PACKAGE_PRICE);
    List<PizzaOrder> pizzaOrders = List.of(
        new PizzaOrder(new Pizza(PIZZA_1, BigDecimal.valueOf(25)), false),
        new PizzaOrder(new Pizza(PIZZA_2, BigDecimal.valueOf(27.9)), true),
        new PizzaOrder(new Pizza(PIZZA_3, BigDecimal.valueOf(35.5)), false),
        new PizzaOrder(new Pizza(PIZZA_4, BigDecimal.valueOf(37)), false)
    );
    // when
    Receipt receipt = orderService.prepareReceipt(pizzaOrders);
    // then
    List<ReceiptItem> expectedReceiptList = List
        .of(
            new ReceiptItem(PIZZA_1, BigDecimal.valueOf(25), BigDecimal.valueOf(25)),
            new ReceiptItem(PIZZA_2, BigDecimal.valueOf(29.9), BigDecimal.valueOf(29.9)),
            new ReceiptItem(PIZZA_3, BigDecimal.valueOf(35.5), BigDecimal.valueOf(35.5)),
            new ReceiptItem(PIZZA_4, BigDecimal.valueOf(37), BigDecimal.valueOf(37))
        );
    assertIterableEquals(expectedReceiptList, receipt.getItems());
    assertEquals(BigDecimal.valueOf(127.4), receipt.getTotalPrice());
  }

  @Test
  void shouldPrepareReceiptWithPriceOfCheapestPizzaOnMonday() {
    // given
    OrderService orderService = new OrderService(MONDAY_CLOCK, PACKAGE_PRICE);
    List<PizzaOrder> pizzaOrders = List.of(
        new PizzaOrder(new Pizza(PIZZA_1, BigDecimal.valueOf(25)), false),
        new PizzaOrder(new Pizza(PIZZA_2, BigDecimal.valueOf(27.9)), true),
        new PizzaOrder(new Pizza(PIZZA_3, BigDecimal.valueOf(35.5)), false),
        new PizzaOrder(new Pizza(PIZZA_4, BigDecimal.valueOf(37)), false)
    );
    // when
    Receipt receipt = orderService.prepareReceipt(pizzaOrders);
    // then
    List<ReceiptItem> expectedReceiptList = List
        .of(
            new ReceiptItem(PIZZA_1, BigDecimal.valueOf(25), BigDecimal.valueOf(25)),
            new ReceiptItem(PIZZA_2, BigDecimal.valueOf(29.9), BigDecimal.valueOf(27)),
            new ReceiptItem(PIZZA_3, BigDecimal.valueOf(35.5), BigDecimal.valueOf(25)),
            new ReceiptItem(PIZZA_4, BigDecimal.valueOf(37), BigDecimal.valueOf(25))
        );
    assertIterableEquals(expectedReceiptList, receipt.getItems());
    assertEquals(BigDecimal.valueOf(102), receipt.getTotalPrice());
  }

  @Test
  void shouldPrepareReceiptWithoutPackagePriceOnFriday() {
    // given
    OrderService orderService = new OrderService(FRIDAY_CLOCK, PACKAGE_PRICE);
    List<PizzaOrder> pizzaOrders = List.of(
        new PizzaOrder(new Pizza(PIZZA_1, BigDecimal.valueOf(25)), false),
        new PizzaOrder(new Pizza(PIZZA_2, BigDecimal.valueOf(27.9)), true),
        new PizzaOrder(new Pizza(PIZZA_3, BigDecimal.valueOf(35.5)), false),
        new PizzaOrder(new Pizza(PIZZA_4, BigDecimal.valueOf(37)), false)
    );
    // when
    Receipt receipt = orderService.prepareReceipt(pizzaOrders);
    // then
    List<ReceiptItem> expectedReceiptList = List
        .of(
            new ReceiptItem(PIZZA_1, BigDecimal.valueOf(25), BigDecimal.valueOf(25)),
            new ReceiptItem(PIZZA_2, BigDecimal.valueOf(27.9), BigDecimal.valueOf(27.9)),
            new ReceiptItem(PIZZA_3, BigDecimal.valueOf(35.5), BigDecimal.valueOf(35.5)),
            new ReceiptItem(PIZZA_4, BigDecimal.valueOf(37), BigDecimal.valueOf(37))
        );
    assertIterableEquals(expectedReceiptList, receipt.getItems());
    assertEquals(BigDecimal.valueOf(125.4), receipt.getTotalPrice());
  }

  @Test
  void shouldPrepareReceiptWithoutTwoForOnePriceOnWednesday() {
    // given
    OrderService orderService = new OrderService(WEDNESDAY_CLOCK, PACKAGE_PRICE);
    List<PizzaOrder> pizzaOrders = List.of(
        new PizzaOrder(new Pizza(PIZZA_1, BigDecimal.valueOf(25)), false),
        new PizzaOrder(new Pizza(PIZZA_1, BigDecimal.valueOf(25)), false),
        new PizzaOrder(new Pizza(PIZZA_1, BigDecimal.valueOf(25)), true),
        new PizzaOrder(new Pizza(PIZZA_3, BigDecimal.valueOf(35.5)), false),
        new PizzaOrder(new Pizza(PIZZA_3, BigDecimal.valueOf(35.5)), true),
        new PizzaOrder(new Pizza(PIZZA_4, BigDecimal.valueOf(37)), false)

    );
    // when
    Receipt receipt = orderService.prepareReceipt(pizzaOrders);
    // then
    List<ReceiptItem> expectedReceiptList = List
        .of(
            new ReceiptItem(PIZZA_4, BigDecimal.valueOf(37), BigDecimal.valueOf(37)),
            new ReceiptItem(PIZZA_1, BigDecimal.valueOf(25), BigDecimal.valueOf(24.9)),
            new ReceiptItem(PIZZA_1, BigDecimal.valueOf(25), BigDecimal.valueOf(0.1)),
            new ReceiptItem(PIZZA_1, BigDecimal.valueOf(27), BigDecimal.valueOf(27)),
            new ReceiptItem(PIZZA_3, BigDecimal.valueOf(35.5), BigDecimal.valueOf(35.4)),
            new ReceiptItem(PIZZA_3, BigDecimal.valueOf(37.5), BigDecimal.valueOf(2.1))
        );
    assertIterableEquals(expectedReceiptList, receipt.getItems());
    assertEquals(BigDecimal.valueOf(126.5), receipt.getTotalPrice());
  }
}
