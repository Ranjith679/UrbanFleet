package com.urbanfleet.order_service.constants;

public enum OrderAction {


    // this is not direct state of an order but this represents the actions which is like checklist for a state

    CONFIRM, //customer placed order and restaurant accepted
    START_PREPARING,
    MARK_READY,
    ASSIGN_DELIVERY,
    COMPLETE,
    CANCEL
}
