

package com.pega.framework.http;

import com.pega.*;
import org.apache.http.client.*;

public interface PegaHttpClient {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: PegaHttpClient.java 208547 2016-09-07 19:32:44Z PrashantSammeta $";

    PegaHttpResponse execute(final PegaHttpUriRequest p0);

    PegaHttpResponse execute(final PegaHttpUriRequest p0, final boolean p1);

    HttpClient getHttpClient();

    PegaHttpPost createPostRequest(final String p0);

    PegaHttpGet createGetRequest(final String p0);

    HttpParamsBean getHttpParamsBean();

    String getCurrentTabThreadNo();

    String incrementCurrentTabThreadNo();

    String decrementCurrentTabThreadNo();

    PegaHttpPost createPostRequest(final String p0, final String p1);

    PegaHttpGet createGetRequest(final String p0, final String p1);

    String getPropertyValueRequest(final String p0, final String p1, final String p2);

    String getPageListSize(final String p0, final String p1, final String p2);

    String getPageReference(final String p0, final String p1, final String p2, final String p3, final String p4);

    TestEnvironment getTestEnvironment();

    void setHarnessId(final String p0);

    String getHarnessId();

    void setSectionId(final String p0);

    String getSectionId();

    void setTransactionId(final String p0);

    String getTransactionId();
}
