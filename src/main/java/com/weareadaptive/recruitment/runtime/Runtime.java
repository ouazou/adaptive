// *****************************************************************
// *****************************************************************
// *****************************************************************
// PLEASE DO NOT MODIFY THIS FILE - IT IS NOT A PART OF THE EXERCISE
// *****************************************************************
// *****************************************************************
// *****************************************************************
package com.weareadaptive.recruitment.runtime;

import com.weareadaptive.recruitment.contract.PriceTick;
import com.weareadaptive.recruitment.contract.SupportedSymbol;
import com.weareadaptive.recruitment.contract.TradeOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Runtime implements Runnable {
    private Queue<PriceTick> priceTicks;
    private Queue<TradeOrder> tradeOrders;
    private PriceTicker priceTicker;
    private OrderManagement orderManagement;

    public Runtime(Queue<TradeOrder> tradeOrders) {
        priceTicks = new ConcurrentLinkedQueue<>();
        this.tradeOrders = tradeOrders;
        final Thread runtime = new Thread(this);
        runtime.start();
    }

    @Override
    public void run() {
        priceTicker = new PriceTicker(priceTicks, getSupportedSymbols());
        orderManagement = new OrderManagement(priceTicks, tradeOrders);
    }

    private List<SupportedSymbol> getSupportedSymbols() {
        final ArrayList<SupportedSymbol> result = new ArrayList<>();
        result.add(new SupportedSymbol("AAPL"));
        result.add(new SupportedSymbol("MSFT"));
        result.add(new SupportedSymbol("GOOG"));
        result.add(new SupportedSymbol("AMZN"));
        result.add(new SupportedSymbol("NFLX"));
        result.add(new SupportedSymbol("FB"));
        return result;
    }

    public void cancelAllOrders() {
        orderManagement.cancelAllOrders();
    }

    public void shutdown() {
        System.out.println("Runtime received request to shut down OMS and Prices");
        priceTicker.shutdown();
        orderManagement.shutdown();
    }

}
