**Implement stock trade order**

You are to modify the 'Adaptive.Recruitment.TradeOrders.Orders.OrderFactory' class to create orders that satisfy the following two requirements:
  - BUY 50 shares in Apple (stock symbol: AAPL) at a maximum price of $100.
  - SELL 200 shares in Microsoft (stock symbol: MSFT) at a minimum price of $50.

For further clarification on a limit order, see:
http://www.investopedia.com/terms/l/limitorder.asp

Do NOT modify any files that state they should not be modified in the header.
Do NOT implement any other OrderTypes than OrderType.Limit

The implementation should:
  - Implement the `com.weareadaptive.recruitment.contract.TradeOrder` interface.
  - Modify other files (without the Do Not Modify header) as necessary
  - Place a single BUY or SELL transaction via `com.weareadaptive.recruitment.contract.TradeBooker` for a received eligible price. Note:
    - If the call is successful then the trade was booked.
    - If an exception is thrown then the trade has failed and the order should not retry.
    - Call `TradeOrder.tradeSucess` upon success.
    - Call `TradeOrder.tradeFailure` upon failure.
    - Implement the ITradeOrder.Cancel() method to allow the order to be cancelled externally.

Unit test your order code.
Do not test the OrderFactory modifications.
You can reference any testing library of your choosing if helpful.
