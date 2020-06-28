

package com.pega.framework.http;

public class HttpParamsBean {
    String COPYRIGHT;
    private static final String VERSION = "$Id: HttpParamsBean.java 207810 2016-08-16 07:01:58Z BalanaveenreddyKappeta $";
    String loggedOutAuthCode;
    String authCode;
    String transactionID;
    String harnessID;

    public HttpParamsBean() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
        this.loggedOutAuthCode = "";
        this.authCode = "";
        this.transactionID = "";
        this.harnessID = "";
    }

    public String getLoggedOutAuthCode() {
        return this.loggedOutAuthCode;
    }

    public void setLoggedOutAuthCode(final String loggedOutAuthCode) {
        this.loggedOutAuthCode = loggedOutAuthCode;
    }

    public String getAuthCode() {
        return this.authCode;
    }

    public void setAuthCode(final String authCode) {
        this.authCode = authCode;
    }

    public String getTransactionID() {
        return this.transactionID;
    }

    public void setTransactionID(final String transactionID) {
        this.transactionID = transactionID;
    }

    public String getHarnessID() {
        return this.harnessID;
    }

    public void setHarnessID(final String harnessID) {
        this.harnessID = harnessID;
    }
}
