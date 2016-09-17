// *****************************************************************
// *****************************************************************
// *****************************************************************
// PLEASE DO NOT MODIFY THIS FILE - IT IS NOT A PART OF THE EXERCISE
// *****************************************************************
// *****************************************************************
// *****************************************************************
package com.weareadaptive.recruitment.contract;

import com.weareadaptive.recruitment.exception.TradeBookingException;

public interface TradeBooker {
    void buy(String stockSymbol, int volume, double price) throws TradeBookingException;
    void sell(String stockSymbol, int volume, double price) throws TradeBookingException;
}
