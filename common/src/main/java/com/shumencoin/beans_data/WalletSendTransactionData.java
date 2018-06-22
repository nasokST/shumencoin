package com.shumencoin.beans_data;

import java.io.Serializable;

import com.shumencoin.crypto.CryptoData;

public class WalletSendTransactionData implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -5692323163482704173L;

    public TransactionData getTransactionData() {
        return transactionData;
    }
    public void setTransactionData(TransactionData transactionData) {
        this.transactionData = transactionData;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public CryptoData getCryptoData() {
	return cryptoData;
    }
    public void setCryptoData(CryptoData cryptoData) {
	this.cryptoData = cryptoData;
    }

    private TransactionData transactionData;
    private String password;
    private CryptoData cryptoData;
}
