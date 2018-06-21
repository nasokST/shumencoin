package com.shumencoin.beans_data;

import java.math.BigInteger;

public class BalanceBean {

    private String address;
    
    private BigInteger confirmedBalance;
    
    private BigInteger pendingBalance;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigInteger getConfirmedBalance() {
        return confirmedBalance;
    }

    public void setConfirmedBalance(BigInteger confirmedBalance) {
        this.confirmedBalance = confirmedBalance;
    }

    public BigInteger getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(BigInteger pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

}
