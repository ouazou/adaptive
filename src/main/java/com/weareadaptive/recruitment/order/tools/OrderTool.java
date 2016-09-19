package com.weareadaptive.recruitment.order.tools;

import com.weareadaptive.recruitment.contract.OrderStatus;
import com.weareadaptive.recruitment.contract.TradeBooker;
import com.weareadaptive.recruitment.exception.TradeBookingException;
import com.weareadaptive.recruitment.order.impl.TradeOrderImpl;

import java.util.concurrent.CompletableFuture;

/**
 * Created by ouazou on 2016-09-18.
 */
public class OrderTool {


  public static void bookByOrder(final TradeOrderImpl order, TradeBooker tradeBooker,
                           double price, int volume) {
    CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> {
      // big computation task
      try {
        tradeBooker.buy(order.getSymbol(), volume, price);
      } catch (TradeBookingException e) {
        handleException(order, e);
      }
      return order;
    }).thenApply(or -> {
      or.setStatus(OrderStatus.Completed);
      return or;
    });

  }

  public static void bookSellOrder(final TradeOrderImpl order, TradeBooker tradeBooker,
                             double price, int volume) {
    CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> {
      // big computation task
      try {
        tradeBooker.buy(order.getSymbol(), volume, price);
      } catch (TradeBookingException e) {
        handleException(order, e);
      }
      return order;
    }).thenApply(or -> {
      or.setStatus(OrderStatus.Completed);
      return or;
    });
  }

  public static void handleException(TradeOrderImpl tradeOrder, TradeBookingException ex) {
    tradeOrder.tradeFailure(
        String.format("Order (%s,%s)", tradeOrder.getSymbol(), tradeOrder.getDirection()), ex);
    tradeOrder.setStatus(OrderStatus.Failed);
  }


}
