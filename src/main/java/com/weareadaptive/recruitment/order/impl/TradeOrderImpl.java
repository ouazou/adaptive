package com.weareadaptive.recruitment.order.impl;

import com.weareadaptive.recruitment.contract.OrderDirection;
import com.weareadaptive.recruitment.contract.OrderStatus;
import com.weareadaptive.recruitment.contract.OrderType;
import com.weareadaptive.recruitment.contract.PriceTick;
import com.weareadaptive.recruitment.contract.TradeOrder;
import com.weareadaptive.recruitment.exception.TradeBookingException;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by ouazou on 2016-09-18.
 */
public class TradeOrderImpl implements TradeOrder {

  private String symbol;
  private OrderDirection direction;
  private OrderType type;
  private AtomicReference<OrderStatus> status=new AtomicReference<>(OrderStatus.Active);
  private double price;
  private int volume;
  private BiConsumer<String, TradeBookingException> failureHandler;
  private Consumer<String> successHandler;
  private Future runningTask;

  public TradeOrderImpl(String symbol,
                        OrderDirection direction,
                        OrderType type,
                        double price,
                        int volume) {
    this.symbol = symbol;
    this.direction = direction;
    this.type = type;
    this.price = price;
    this.volume = volume;
  }

  @Override
  public String getSymbol() {
    return this.symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  @Override
  public OrderDirection getDirection() {
    return this.direction;
  }

  public void setDirection(OrderDirection direction) {
    this.direction = direction;
  }

  @Override
  public OrderType getType() {
    return this.type;
  }

  public void setType(OrderType type) {
    this.type = type;
  }


  @Override
  public OrderStatus getStatus() {
    return this.status.get();
  }

  public void setStatus(OrderStatus status) {
    if (!this.status.compareAndSet(OrderStatus.Active,status)){
      throw new IllegalStateException("Invalid state transition");
    }
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getVolume() {
    return volume;
  }

  public void setVolume(int volume) {
    this.volume = volume;
  }

  @Override
  public void setTradeFailureHandler(BiConsumer<String, TradeBookingException> failureHandler) {

    this.failureHandler = failureHandler;
  }

  @Override
  public void setTradeSuccessHandler(Consumer<String> successHandler) {
    this.successHandler = successHandler;
  }

  public BiConsumer<String, TradeBookingException> getTradeFailureHandler() {
    return failureHandler;
  }

  public Consumer<String> getTradeSuccessHandler() {
    return successHandler;
  }

  public Future getRunningTask() {
    return runningTask;
  }

  public void setRunningTask(Future runningTask) {
    this.runningTask = runningTask;
  }

  @Override
  public void onPriceTick(PriceTick priceTick) {
    switch (type) {
      case Limit:
        if ((direction == OrderDirection.Buy && this.price > priceTick.getPrice())
            || (direction == OrderDirection.Sell && this.price < priceTick.getPrice())) {

          this.price = priceTick.getPrice();
        }
        break;
      case Market:
      case Stop:
      case TrailingStop:
      case StopLimit:
        throw new UnsupportedOperationException();
      default:
    }
  }

  @Override
  public void cancel() {
    if (this.status.compareAndSet(OrderStatus.Active, OrderStatus.Cancelled)) {
      runningTask.cancel(true);
    }
  }

  public void tradeSuccess(String message) {
    this.successHandler.accept(message);

  }

  public void tradeFailure(String message, TradeBookingException ex) {
    this.failureHandler.accept(message, ex);
  }


  @Override
  public String toString() {
    return "TradeOrderImpl{" +
           "symbol='" + symbol + '\'' +
           ", direction=" + direction +
           ", type=" + type +
           ", status=" + status +
           ", price=" + price +
           ", volume=" + volume +
           '}';
  }
}
