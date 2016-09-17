// *****************************************************************
// *****************************************************************
// *****************************************************************
// PLEASE DO NOT MODIFY THIS FILE - IT IS NOT A PART OF THE EXERCISE
// *****************************************************************
// *****************************************************************
// *****************************************************************
package com.weareadaptive.recruitment.contract;

import com.weareadaptive.recruitment.exception.TradeBookingException;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface TradeOrder {
    String getSymbol();
    OrderDirection getDirection();
    OrderType getType();
    OrderStatus getStatus();

    void setTradeFailureHandler(BiConsumer<String, TradeBookingException> failureHandler);
    void setTradeSuccessHandler(Consumer<String> successHandler);

    void onPriceTick(PriceTick priceTick);
    void cancel();

    String toString();
}
