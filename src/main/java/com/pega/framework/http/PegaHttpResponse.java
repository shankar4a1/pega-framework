

package com.pega.framework.http;

import org.apache.http.*;

import java.io.*;

public interface PegaHttpResponse {


    HttpResponse getHttpResponse();

    String getResponseAsString();

    int getResponseCode();

    void consumeResponseQuietly();

    void writeResponseToFile(final File p0, final String p1);
}
