// *****************************************************************
// *****************************************************************
// *****************************************************************
// PLEASE DO NOT MODIFY THIS FILE - IT IS NOT A PART OF THE EXERCISE
// *****************************************************************
// *****************************************************************
// *****************************************************************
package com.weareadaptive.recruitment;

import com.weareadaptive.recruitment.contract.OrderDirection;
import com.weareadaptive.recruitment.contract.OrderType;
import com.weareadaptive.recruitment.contract.TradeOrder;
import com.weareadaptive.recruitment.order.OrderFactory;
import com.weareadaptive.recruitment.runtime.Runtime;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Program {
    public static void main(String[] args) {
        final ConcurrentLinkedQueue<TradeOrder> tradeOrders = new ConcurrentLinkedQueue<>();

        final OrderFactory factory = new OrderFactory();
        final Runtime runtime = new Runtime(tradeOrders);

        tradeOrders.add(factory.create(OrderType.Limit, OrderDirection.Buy, "AAPL", 100, 50));
        tradeOrders.add(factory.create(OrderType.Limit, OrderDirection.Sell, "MSFT", 50, 20));

        System.out.println("Press enter to exit");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        runtime.cancelAllOrders();
        runtime.shutdown();

    }
}
