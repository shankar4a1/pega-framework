

package com.pega.config.util;

import java.util.regex.*;

public class FetchPRPCDetails {
    String COPYRIGHT;
    private static final String VERSION = "$Id: FetchPRPCDetails.java 208617 2016-09-09 09:29:52Z BalanaveenreddyKappeta $";

    public FetchPRPCDetails() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    }

    private String fetchSysInfo(final String url) throws Exception {
       /* final SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial((KeyStore)null, (TrustStrategy)new TrustSelfSignedStrategy());
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
     //   final Registry<ConnectionSocketFactory> registry = (Registry<ConnectionSocketFactory>)RegistryBuilder.create().register("http", (Object)new PlainConnectionSocketFactory()).register("https", (Object)sslsf).build();
       // final BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager((Lookup)registry);
        final BasicCookieStore cookieStore = new BasicCookieStore();
        final RequestConfig config = RequestConfig.custom().setSocketTimeout(180000).setConnectTimeout(180000).setStaleConnectionCheckEnabled(true).setConnectionRequestTimeout(180000).build();
      //  final HttpClient client = (HttpClient)HttpClientBuilder.create().setDefaultCookieStore((CookieStore)cookieStore).setDefaultRequestConfig(config).setConnectionManager((HttpClientConnectionManager)connectionManager).build();
        String sysInfo = null;
        final HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute((HttpUriRequest)get);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
        final Pattern pattern = Pattern.compile("action=\"(.+?)\"");
        final Matcher m = pattern.matcher(content);
        m.find();
        final String action = m.group(1);
        final String completeUrl = url.replace("/prweb/PRServlet", action);
        final HttpPost loginReq = new HttpPost(completeUrl);
        final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add((NameValuePair)new BasicNameValuePair("pzAuth", "guest"));
        nvps.add((NameValuePair)new BasicNameValuePair("UserIdentifier", "administrator@pega.com"));
        nvps.add((NameValuePair)new BasicNameValuePair("Password", "install"));
        nvps.add((NameValuePair)new BasicNameValuePair("pyActivity", "Code-Security.Login"));
        loginReq.setEntity((HttpEntity)new UrlEncodedFormEntity((Iterable)nvps, Consts.UTF_8));
        response = client.execute((HttpUriRequest)loginReq);
        entity = response.getEntity();
        content = EntityUtils.toString(entity);
        final String req1URL = String.valueOf(completeUrl.replace("STANDARD", "TABTHREAD1")) + "?pyActivity=%40baseclass.doUIAction&action=openRuleByKeys&pySystemName=pega&pxObjClass=Data-Admin-System&api=openRuleByKeys&ObjClass=Data-Admin-System&Format=harness&contentID=0f5226b6-6a6b-06a0-b607-22d4212c527d&dynamicContainerID=de12ecd7-b2b3-4849-bff1-ee2797014513&tabIndex=3&prevContentID=a65eaec5-03a2-4706-87dc-7683564ac82c&prevRecordkey=System%20-%20General&portalThreadName=STANDARD&portalName=Developer&pzHarnessID=HID77616B0B56966B3D1735110F19533FB1";
        final HttpPost req1 = new HttpPost(req1URL);
        response = client.execute((HttpUriRequest)req1);
        entity = response.getEntity();
        content = EntityUtils.toString(entity);
        if (content.contains("This is not a multi-tenant system")) {
            sysInfo = "<Tenant>Non-MT<Tenant>";
        }
        else if (content.contains("This is a multi-tenant system")) {
            sysInfo = "<Tenant>MT<Tenant>";
        }
        final String req2URL = String.valueOf(completeUrl) + "?pyActivity=GetWebInfo&=&target=popup&pzHarnessID=HID2916E64F515C56E8477629AED244CC6B";
        final HttpPost req2 = new HttpPost(req2URL);
        response = client.execute((HttpUriRequest)req2);
        entity = response.getEntity();
        content = EntityUtils.toString(entity);
        return String.valueOf(sysInfo) + "\r\n" + content;*/
        return "";
    }

    public String[] getEnvInfo(final String url) {
        String xml = null;
        try {
            xml = this.fetchSysInfo(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String server = "";
        String db = "";
        String os = "";
        String tenant = "";
        String buildName = "";
        String buildDate = "";
        String buildMajorVersion = "";
        String buildMinorVersion = "";
        String buildLabel = "";
        if (xml != null) {
            buildName = this.getVal(xml, "BuildName");
            server = this.getVal(xml, "ServerInfo");
            db = this.getVal(xml, "DBProductName");
            os = this.getVal(xml, "os.name");
            tenant = this.getVal(xml, "Tenant");
            buildDate = this.getVal(xml, "BuildDate");
            buildMajorVersion = this.getVal(xml, "BuildMajorVersion");
            buildMinorVersion = this.getVal(xml, "BuildMinorVersion");
            buildLabel = this.getVal(xml, "Label");
        }
        return new String[]{server, db, os, tenant, buildName, buildDate, buildMajorVersion, buildMinorVersion, buildLabel};
    }

    private String getVal(final String xml, final String tag) {
        String val = "";
        final Pattern p = Pattern.compile("(<" + tag + ">)(.*?)<");
        final Matcher m = p.matcher(xml);
        if (m.find()) {
            val = m.group(2);
        }
        return val;
    }
}
