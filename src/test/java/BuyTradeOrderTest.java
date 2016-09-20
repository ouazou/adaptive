import com.weareadaptive.recruitment.contract.OrderDirection;
import com.weareadaptive.recruitment.contract.OrderType;
import com.weareadaptive.recruitment.contract.TradeBooker;
import com.weareadaptive.recruitment.exception.TradeBookingException;
import com.weareadaptive.recruitment.order.impl.TradeOrderImpl;
import com.weareadaptive.recruitment.order.tools.OrderTool;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BuyTradeOrderTest {

        @Test
        public void failedBookOrderBuyOrderLimit() throws Exception {

            TradeBooker tradeBooker = Mockito.mock(TradeBooker.class);
            BiConsumer<String, TradeBookingException> failureConsumer = Mockito.mock(BiConsumer.class);
            TradeBookingException exceptionToThrow=new TradeBookingException("Failed");
            doThrow(exceptionToThrow)
                .when(tradeBooker)
                .buy("SYMB", 10, 50.00);
            TradeOrderImpl tradeOrder = new TradeOrderImpl(
                "SYMB",
                OrderDirection.Buy,
                OrderType.Limit,
                50.00,
                10
            );
            tradeOrder.setTradeFailureHandler(failureConsumer);
            OrderTool.bookOrder(tradeOrder, tradeBooker);
            tradeOrder.getRunningTask().get();
            verify(tradeBooker,never()).sell("SYMB", 10, 50.00);
            verify(tradeBooker).buy("SYMB", 10, 50.00);
            verify(failureConsumer)
                .accept(String.format("Order %s", tradeOrder.toString()),
                        exceptionToThrow);
        }

        @Test
        public void successfullBookOrderBuyOrderLimit() throws Exception {

            TradeBooker tradeBooker = Mockito.mock(TradeBooker.class);
            BiConsumer<String, TradeBookingException> failureConsumer = Mockito.mock(BiConsumer.class);
            Consumer<String> successConsumer = Mockito.mock(Consumer.class);
            TradeOrderImpl tradeOrder = new TradeOrderImpl(
                "SYMB",
                OrderDirection.Buy,
                OrderType.Limit,
                25.00,
                10
            );
            tradeOrder.setTradeSuccessHandler(successConsumer);
            OrderTool.bookOrder(tradeOrder, tradeBooker);
            verify(tradeBooker).buy("SYMB", 10, 25.00);
            verify(tradeBooker,never()).sell("SYMB", 10, 25.00);
            verify(successConsumer)
                .accept(String.format("Order %s", tradeOrder.toString()));
        }

        @Test
        public void failedBookOrderSellOrderLimit() throws Exception {

            TradeBooker tradeBooker = Mockito.mock(TradeBooker.class);
            BiConsumer<String, TradeBookingException> failureConsumer = Mockito.mock(BiConsumer.class);
            TradeBookingException exceptionToThrow=new TradeBookingException("Failed");
            doThrow(exceptionToThrow)
                .when(tradeBooker)
                .sell("SYMB", 10, 50.00);
            TradeOrderImpl tradeOrder = new TradeOrderImpl(
                "SYMB",
                OrderDirection.Sell,
                OrderType.Limit,
                50.00,
                10
            );
            tradeOrder.setTradeFailureHandler(failureConsumer);
            OrderTool.bookOrder(tradeOrder, tradeBooker);
            tradeOrder.getRunningTask().get();
            verify(tradeBooker,never()).buy("SYMB", 10,50.00);
            verify(tradeBooker).sell("SYMB", 10, 50.00);
            verify(failureConsumer)
                .accept(String.format("Order %s", tradeOrder.toString()),
                        exceptionToThrow);
        }

        @Test
        public void successfullBookOrderSellOrderLimit() throws Exception {

            TradeBooker tradeBooker = Mockito.mock(TradeBooker.class);
            BiConsumer<String, TradeBookingException> failureConsumer = Mockito.mock(BiConsumer.class);
            Consumer<String> successConsumer = Mockito.mock(Consumer.class);
            TradeOrderImpl tradeOrder = new TradeOrderImpl(
                "SYMB",
                OrderDirection.Sell,
                OrderType.Limit,
                25.00,
                10
            );
            tradeOrder.setTradeSuccessHandler(successConsumer);
            OrderTool.bookOrder(tradeOrder, tradeBooker);
            verify(tradeBooker).sell("SYMB", 10, 25.00);
            verify(tradeBooker,never()).buy("SYMB", 10, 25.00);
            verify(successConsumer)
                .accept(String.format("Order %s", tradeOrder.toString()));
        }
}
