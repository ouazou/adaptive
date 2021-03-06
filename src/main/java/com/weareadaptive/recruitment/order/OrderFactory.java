package com.weareadaptive.recruitment.order;

import com.weareadaptive.recruitment.contract.OrderDirection;
import com.weareadaptive.recruitment.contract.OrderType;
import com.weareadaptive.recruitment.contract.TradeBooker;
import com.weareadaptive.recruitment.contract.TradeOrder;
import com.weareadaptive.recruitment.order.impl.TradeOrderImpl;
import com.weareadaptive.recruitment.order.tools.OrderTool;
import com.weareadaptive.recruitment.runtime.TradeBookerImpl;

public class OrderFactory {

  private final TradeBooker tradeBooker;

  public OrderFactory() {
    tradeBooker = new TradeBookerImpl();
  }

  public TradeOrder create(OrderType type, OrderDirection direction, String symbol, double price,
                           int volume) {

    TradeOrderImpl tradeOrder = new TradeOrderImpl(symbol, direction, type, price, volume);
    switch (type) {
      case Limit:
        OrderTool.bookOrder(tradeOrder, tradeBooker);
        break;
      case Market:
      case Stop:
      case TrailingStop:
      case StopLimit:
        throw new UnsupportedOperationException();
      default:
    }
    return tradeOrder;
  }


}
