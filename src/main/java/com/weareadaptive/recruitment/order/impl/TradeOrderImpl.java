package com.weareadaptive.recruitment.order.impl;

import com.weareadaptive.recruitment.contract.*;
import com.weareadaptive.recruitment.exception.TradeBookingException;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by ouazou on 2016-09-18.
 */
public class TradeOrderImpl implements TradeOrder {

  private String symbol;
  private OrderDirection direction;
  private OrderType type;
  private volatile OrderStatus status;
  private double price;
  private int volume;
  private BiConsumer<String, TradeBookingException> failureHandler;
  private Consumer<String> successHandler;

  public TradeOrderImpl(String symbol,
                        OrderDirection direction,
                        OrderType type,
                        double price,
                        int volume) {
    this.symbol = symbol;
    this.direction = direction;
    this.type = type;
    this.status = OrderStatus.Active;
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
    return this.status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
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
    this.status = OrderStatus.Cancelled;
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
