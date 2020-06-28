

package com.pega.framework.http;

import org.apache.http.*;

import java.io.*;

public interface PegaHttpResponse {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: PegaHttpResponse.java 142258 2015-06-30 11:44:42Z SachinVellanki $";

    HttpResponse getHttpResponse();

    String getResponseAsString();

    int getResponseCode();

    void consumeResponseQuietly();

    void writeResponseToFile(final File p0, final String p1);
}
