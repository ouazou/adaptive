package com.weareadaptive.recruitment.order.tools;

import com.weareadaptive.recruitment.contract.OrderDirection;
import com.weareadaptive.recruitment.contract.OrderStatus;
import com.weareadaptive.recruitment.contract.TradeBooker;
import com.weareadaptive.recruitment.exception.TradeBookingException;
import com.weareadaptive.recruitment.order.impl.TradeOrderImpl;

import java.util.concurrent.CompletableFuture;

/**
 * Created by ouazou on 2016-09-18.
 */
public class OrderTool {

  public static void bookOrder(TradeOrderImpl tradeOrder, TradeBooker tradeBooker) {
    CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> {
      try {
        if (tradeOrder.getDirection() == OrderDirection.Buy)
          tradeBooker.buy(tradeOrder.getSymbol(), tradeOrder.getVolume(), tradeOrder.getPrice());
        else
          tradeBooker.sell(tradeOrder.getSymbol(), tradeOrder.getVolume(), tradeOrder.getPrice());
      } catch (TradeBookingException e) {
        tradeOrder.setStatus(OrderStatus.Failed);
        tradeOrder.tradeFailure(
                String.format("Order %s", tradeOrder.toString()), e);
        throw new RuntimeException(e);
      }
      return tradeOrder;
    }).thenAccept(order -> {
      order.setStatus(OrderStatus.Completed);
      order.tradeSuccess(String.format("Order %s", tradeOrder.toString()));

    });
  }


}
