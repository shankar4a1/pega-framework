

package com.pega.framework.http;

import org.apache.http.client.methods.*;

public interface PegaHttpGet extends PegaHttpUriRequest {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: PegaHttpGet.java 142258 2015-06-30 11:44:42Z SachinVellanki $";

    HttpGet getHttpGet();
}
