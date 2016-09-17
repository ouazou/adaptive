// *****************************************************************
// *****************************************************************
// *****************************************************************
// PLEASE DO NOT MODIFY THIS FILE - IT IS NOT A PART OF THE EXERCISE
// *****************************************************************
// *****************************************************************
// *****************************************************************
package com.weareadaptive.recruitment.runtime;

import com.weareadaptive.recruitment.contract.TradeBooker;
import com.weareadaptive.recruitment.exception.TradeBookingException;

public class TradeBookerImpl implements TradeBooker {

    public void buy(String stockSymbol, int volume, double price) throws TradeBookingException {
        // simulating a long running call
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new TradeBookingException(String.format("Interrupted error occurred while buying stock '%s'", stockSymbol));
        }

        if (price % 2 == 0) {
            throw new TradeBookingException(String.format("Unknown error occurred while buying stock '%s'", stockSymbol));
        }
    }

    public void sell(String stockSymbol, int volume, double price) throws TradeBookingException {
        // simulating a long running call
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new TradeBookingException(String.format("Interrupted error occurred while selling stock '%s'", stockSymbol));

        }

        if (price % 2 == 0) {
            throw new TradeBookingException(String.format("Unknown error occurred while selling stock '%s'", stockSymbol));
        }
    }
}
