package com.weareadaptive.recruitment.order.impl;

import com.weareadaptive.recruitment.contract.OrderDirection;
import com.weareadaptive.recruitment.contract.OrderStatus;
import com.weareadaptive.recruitment.contract.OrderType;
import com.weareadaptive.recruitment.contract.PriceTick;
import com.weareadaptive.recruitment.exception.TradeBookingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by zouarab on 9/19/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class TradeOrderImplTest {

  @Test
  public void testTradeOrder() throws Exception {
    BiConsumer<String, TradeBookingException> failureConsumer = Mockito.mock(BiConsumer.class);
    Consumer<String> successConsumer = Mockito.mock(Consumer.class);
    Future runningTask = Mockito.mock(Future.class);

    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Buy,
        OrderType.Limit,
        50.00,
        10
    );
    tradeOrder.setTradeFailureHandler(failureConsumer);
    tradeOrder.setTradeSuccessHandler(successConsumer);
    tradeOrder.setRunningTask(runningTask);
    assertThat(tradeOrder, hasProperty("symbol", equalTo("SYMB")));
    assertThat(tradeOrder, hasProperty("direction", equalTo(OrderDirection.Buy)));
    assertThat(tradeOrder, hasProperty("type", equalTo(OrderType.Limit)));
    assertThat(tradeOrder, hasProperty("status", equalTo(OrderStatus.Active)));
    assertThat(tradeOrder, hasProperty("price", equalTo(50.00)));
    assertThat(tradeOrder, hasProperty("volume", equalTo(10)));
    assertThat(tradeOrder, hasProperty("tradeFailureHandler", equalTo(failureConsumer)));
    assertThat(tradeOrder, hasProperty("tradeSuccessHandler", equalTo(successConsumer)));
    assertThat(tradeOrder, hasProperty("runningTask", equalTo(runningTask)));
  }

  @Test
  public void cancelActiveOrder() throws Exception {
    Future runningTask = Mockito.mock(Future.class);
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Buy,
        OrderType.Limit,
        50.00,
        10
    );
    tradeOrder.setRunningTask(runningTask);
    assertThat(tradeOrder, hasProperty("runningTask", equalTo(runningTask)));
    tradeOrder.cancel();
    verify(runningTask).cancel(true);
    assertThat(tradeOrder.getStatus(), equalTo(OrderStatus.Cancelled));
  }

  @Test
  public void cancelOtherThanActiveOrder() throws Exception {
    Future runningTask = Mockito.mock(Future.class);
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Buy,
        OrderType.Limit,
        50.00,
        10
    );
    tradeOrder.setStatus(OrderStatus.Completed);
    tradeOrder.setRunningTask(runningTask);
    assertThat(tradeOrder, hasProperty("runningTask", equalTo(runningTask)));
    tradeOrder.cancel();
    verify(runningTask, never()).cancel(true);
    assertThat(tradeOrder.getStatus(), equalTo(OrderStatus.Completed));
  }

  @Test
  public void tradeSuccess() throws Exception {
    Consumer<String> successConsumer = Mockito.mock(Consumer.class);
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Buy,
        OrderType.Limit,
        50.00,
        10
    );
    tradeOrder.setTradeSuccessHandler(successConsumer);
    tradeOrder.tradeSuccess("success");
    verify(successConsumer).accept("success");
  }

  @Test
  public void tradeFailure() throws Exception {
    BiConsumer<String, TradeBookingException> failureConsumer = Mockito.mock(BiConsumer.class);
    TradeBookingException exp = new TradeBookingException("Faillure");
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Buy,
        OrderType.Limit,
        50.00,
        10
    );
    tradeOrder.setTradeFailureHandler(failureConsumer);
    tradeOrder.tradeFailure("failure", exp);
    verify(failureConsumer).accept("failure", exp);
  }


  @Test
  public void onPriceTickLimitBuyMarketPriceLessThanOrderPrice() throws Exception {
    PriceTick priceTick = new PriceTick("SYMB", 10.00);
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Buy,
        OrderType.Limit,
        50.00,
        10
    );
    tradeOrder.onPriceTick(priceTick);
    assertThat(tradeOrder.getPrice(), equalTo(priceTick.getPrice()));
  }

  @Test
  public void onPriceTickLimitBuyMarketPriceGreaterThanOrderPrice() throws Exception {
    PriceTick priceTick = new PriceTick("SYMB", 100.00);
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Buy,
        OrderType.Limit,
        50.00,
        10
    );
    tradeOrder.onPriceTick(priceTick);
    assertThat(tradeOrder.getPrice(), not(priceTick.getPrice()));
    assertThat(tradeOrder.getPrice(), equalTo(50.00));
  }

  @Test
  public void onPriceTickLimitBuyMarketPriceEqualToOrderPrice() throws Exception {
    PriceTick priceTick = new PriceTick("SYMB", 100.00);
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Buy,
        OrderType.Limit,
        100.00,
        10
    );
    tradeOrder.onPriceTick(priceTick);
    assertThat(tradeOrder.getPrice(), equalTo(priceTick.getPrice()));
  }

  @Test
  public void onPriceTickLimitSellMarketPriceLessThanOrderPrice() throws Exception {
    PriceTick priceTick = new PriceTick("SYMB", 25.00);
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Sell,
        OrderType.Limit,
        50.00,
        10
    );
    tradeOrder.onPriceTick(priceTick);
    assertThat(tradeOrder.getPrice(), not(priceTick.getPrice()));
    assertThat(tradeOrder.getPrice(), equalTo(50.00));
  }

  @Test
  public void onPriceTickLimitSellMarketPriceGreaterThanOrderPrice() throws Exception {
    PriceTick priceTick = new PriceTick("SYMB", 100.00);
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Sell,
        OrderType.Limit,
        50.00,
        10
    );
    tradeOrder.onPriceTick(priceTick);
    assertThat(tradeOrder.getPrice(), equalTo(priceTick.getPrice()));

  }

  @Test
  public void onPriceTickLimitSellMarketPriceEqualToOrderPrice() throws Exception {
    PriceTick priceTick = new PriceTick("SYMB", 100.00);
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Sell,
        OrderType.Limit,
        100.00,
        10
    );
    tradeOrder.onPriceTick(priceTick);
    assertThat(tradeOrder.getPrice(), equalTo(priceTick.getPrice()));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void onPriceTickSellOtherThanLimitOrder() throws Exception {
    PriceTick priceTick = new PriceTick("SYMB", 100.00);
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Sell,
        OrderType.Stop,
        100.00,
        10
    );
    tradeOrder.onPriceTick(priceTick);

  }

  @Test(expected = UnsupportedOperationException.class)
  public void onPriceTickBuyOtherThanLimitOrder() throws Exception {
    PriceTick priceTick = new PriceTick("SYMB", 100.00);
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Buy,
        OrderType.Stop,
        100.00,
        10
    );
    tradeOrder.onPriceTick(priceTick);
  }

  @Test(expected = IllegalStateException.class)
  public void invalidStatusTransition() throws Exception {
    PriceTick priceTick = new PriceTick("SYMB", 100.00);
    TradeOrderImpl tradeOrder = new TradeOrderImpl(
        "SYMB",
        OrderDirection.Buy,
        OrderType.Stop,
        100.00,
        10
    );
    tradeOrder.setStatus(OrderStatus.Completed);
    tradeOrder.setStatus(OrderStatus.Failed);
  }



}