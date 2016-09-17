// *****************************************************************
// *****************************************************************
// *****************************************************************
// PLEASE DO NOT MODIFY THIS FILE - IT IS NOT A PART OF THE EXERCISE
// *****************************************************************
// *****************************************************************
// *****************************************************************
package com.weareadaptive.recruitment.contract;

public enum OrderType {
    // A limit order triggers when a minimum specified threshold has been breached in favour of the client.
    Limit,

    // A limit order triggers immediately at the current market price.
    Market,

    // A stop order triggers when a maximum specified threshold has been breached against the client, to limit losses.
    Stop,

    // A trailing stop behaves like a Stop order except the threshold dynamically adjusts.
    TrailingStop,

    // A stop limit order combines the features of a stop order with those of a limit order.
    StopLimit
}
