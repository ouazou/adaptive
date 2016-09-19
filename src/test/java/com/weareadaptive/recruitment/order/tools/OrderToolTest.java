package com.weareadaptive.recruitment.order.tools;

import com.weareadaptive.recruitment.contract.OrderDirection;
import com.weareadaptive.recruitment.contract.OrderType;
import com.weareadaptive.recruitment.contract.TradeBooker;
import com.weareadaptive.recruitment.exception.TradeBookingException;
import com.weareadaptive.recruitment.order.impl.TradeOrderImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.doThrow;

/**
 * Created by zouarab on 9/19/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderToolTest {


    @Test(expected = TradeBookingException.class)
    public void bookOrder() throws Exception {
        TradeBooker tradeBooker = Mockito.mock(TradeBooker.class);
        doThrow(TradeBookingException.class).when(tradeBooker).buy("SYMB", 10, 50.00);
        TradeOrderImpl tradeOrder = new TradeOrderImpl("SYMB", OrderDirection.Buy, OrderType.Limit, 50.00, 10);
        OrderTool.bookOrder(tradeOrder, tradeBooker);
    }

}