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

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

class PriceTicker implements Runnable {

    private static final int PUBLISH_INTERVAL = 200;

    private final Queue<PriceTick> priceTickQueue;
    private final List<SupportedSymbol> supportedSymbols;
    private AtomicBoolean shutdown = new AtomicBoolean(false);

    PriceTicker(Queue<PriceTick> priceTickQueue, List<SupportedSymbol> supportedSymbols) {
        this.priceTickQueue = priceTickQueue;
        this.supportedSymbols = supportedSymbols;
        final Thread producer = new Thread(this);
        producer.start();
    }

    @Override
    public void run() {
        System.out.println("Price Producer running");
        while (!shutdown.get()) {
            priceTickQueue.addAll(supportedSymbols.stream().
                    map(supportedSymbol -> new PriceTick(supportedSymbol.getSymbol(), nextPrice())).
                    collect(Collectors.toList()));
            try {
                Thread.sleep(PUBLISH_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Price Producer stopped");

    }

    private double nextPrice() {
        return (ThreadLocalRandom.current().nextInt(4, 10) * 10.0) + ThreadLocalRandom.current().nextInt(1, 4);
    }

    void shutdown() {
        System.out.println("Requested shutdown of Price Producer");
        shutdown.set(true);
    }
}
