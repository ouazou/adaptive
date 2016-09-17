// *****************************************************************
// *****************************************************************
// *****************************************************************
// PLEASE DO NOT MODIFY THIS FILE - IT IS NOT A PART OF THE EXERCISE
// *****************************************************************
// *****************************************************************
// *****************************************************************
package com.weareadaptive.recruitment.runtime;

import com.weareadaptive.recruitment.contract.OrderStatus;
import com.weareadaptive.recruitment.contract.PriceTick;
import com.weareadaptive.recruitment.contract.TradeOrder;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

class OrderManagement implements Runnable {

    private final List<TradeOrder> registeredOrders = new CopyOnWriteArrayList<>();
    private final Queue<TradeOrder> tradeOrders;
    private final Queue<PriceTick> priceTicks;
    private AtomicBoolean shutdown = new AtomicBoolean(false);

    OrderManagement(Queue<PriceTick> priceTicks, Queue<TradeOrder> tradeOrders) {
        this.priceTicks = priceTicks;
        this.tradeOrders = tradeOrders;
        final Thread oms = new Thread(this);
        oms.start();
    }

    @Override
    public void run() {
        System.out.println("OrderManagementSystem running");

        while (!shutdown.get()) {
            final PriceTick priceTick = priceTicks.poll();
            if (priceTick != null) {
                acceptPrice(priceTick);
            }

            final TradeOrder order = tradeOrders.poll();
            if (order != null) {
                registerOrder(order);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("OrderManagementSystem stopped");
    }

    void cancelAllOrders() {
        if (registeredOrders.stream().filter(o -> o.getStatus().equals(OrderStatus.Active)).count() > 0) {
            System.out.println("Cancelling open order.");
            registeredOrders.forEach(TradeOrder::cancel);
        } else {
            System.out.println("No running order.");
        }
    }

    void shutdown() {
        System.out.println("Requested shutdown of OrderManagementSystem");
        shutdown.set(true);
    }

    private void registerOrder(TradeOrder tradeOrder) {
        tradeOrder.setTradeSuccessHandler(message -> System.out.println(String.format("Success : %s", message)));
        tradeOrder.setTradeFailureHandler((message, exception) ->
                System.out.println(String.format("Failed with message %s and exception %s", message, exception)));
        printOrder(tradeOrder);
        registeredOrders.add(tradeOrder);
    }

    private void acceptPrice(PriceTick priceTick) {
        for (TradeOrder tradeOrder : registeredOrders) {
            if (tradeOrder.getStatus().equals(OrderStatus.Active)) {
                if (tradeOrder.getSymbol().equals(priceTick.getSymbol())) {
                    try {
                        tradeOrder.onPriceTick(priceTick);
                    } catch (Exception generalException) {
                        System.out.println(String.
                                format("FATAL ERROR - onPriceTick to Order %s threw an exception",
                                        tradeOrder.toString()));
                        throw generalException;
                    }
                }
            }
        }
    }

    private void printOrder(TradeOrder tradeOrder) {
        System.out.println(String.format("Received New Order: %s %s - order (%s - %s)", tradeOrder.getDirection().toString(),
                tradeOrder.getType().toString(), tradeOrder.getSymbol().toString(), tradeOrder.getStatus().toString()));
    }
}
