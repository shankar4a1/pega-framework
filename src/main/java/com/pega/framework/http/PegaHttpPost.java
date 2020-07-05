

package com.pega.framework.http;

import org.apache.http.*;
import org.apache.http.client.methods.*;

import java.io.*;
import java.util.*;

public interface PegaHttpPost extends PegaHttpUriRequest {


    HttpPost getHttpPost();

    void addParams(final Map<String, String> p0);

    void addParam(final String p0, final String p1);

    void addParam(final String p0) throws UnsupportedEncodingException;

    void setEntity(final HttpEntity p0);
}
