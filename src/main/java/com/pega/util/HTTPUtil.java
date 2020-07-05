

package com.pega.util;

import com.jayway.restassured.*;
import com.jayway.restassured.response.*;
import com.pega.*;
import com.pega.exceptions.*;
import com.sun.jersey.api.client.*;
import com.sun.jersey.core.util.*;
import org.testng.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;

public class HTTPUtil {


    private static String URL;
    private static String cookie;

    static {
        HTTPUtil.URL = null;
        HTTPUtil.cookie = null;
    }

    public HTTPUtil(final String url) {

        HTTPUtil.URL = url;
    }

    public static String getResponseText(final String url, final String cookie) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.79 Safari/535.11");
        connection.setRequestProperty("Cookie", cookie);
        connection.setRequestMethod("GET");
        final InputStream is = connection.getInputStream();
        return getStringFromInputStream(is);
    }

    public static String getStringFromInputStream(final InputStream is) throws IOException {
        BufferedReader br = null;
        final StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return sb.toString();
        } finally {
            if (br != null) {
                br.close();
            }
        }
        if (br != null) {
            br.close();
        }
        return sb.toString();
    }

    public String getPostRequestResponse(final String url, final String[][] params) throws IOException {
        handleSSLCertificates();
        final Client client = Client.create();
        final WebResource webResource = client.resource(url);
        Reporter.log("URL" + url, true);
        final MultivaluedMapImpl form = new MultivaluedMapImpl();
        for (int rows = params.length, i = 0; i < rows; ++i) {
            Reporter.log(params[i][0] + "--" + params[i][1], true);
            form.add(params[i][0], params[i][1]);
        }
        final ClientResponse response = (ClientResponse) webResource.type("application/x-www-form-urlencoded").post((Class) ClientResponse.class, form);
        final String output = (String) response.getEntity((Class) String.class);
        Reporter.log("Output from Server .... \n" + output);
        return output;
    }

    public static void handleSSLCertificates() {
        Reporter.log("All SSL Certificates will be accepted...", true);
        final TrustManager[] trustAllCerts = {new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
            }
        }};
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
        } catch (Exception e) {
            throw new PegaWebDriverException("Unable to install all trusting trust manager for accepting all SSL certificates...");
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        final HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(final String hostname, final SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    public static void appendPropertyFileWithUTF8Format(final String oldPropFilePath, final String newPropFilePath, final String mergedPropFilePath, final String comment) throws IOException {
        final File oldPropFile = new File(oldPropFilePath);
        final File newPropFile = new File(newPropFilePath);
        final File mergedPropFile = new File(mergedPropFilePath);
        InputStream is1 = null;
        FileInputStream is2 = null;
        FileOutputStream os = null;
        Label_0171:
        {
            Label_0127:
            {
                Label_0083:
                {
                    try {
                        is1 = new FileInputStream(oldPropFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        break Label_0083;
                    } finally {
                        is1.close();
                    }
                    is1.close();
                    try {
                        is2 = new FileInputStream(newPropFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        break Label_0127;
                    } finally {
                        is2.close();
                    }
                }
                is2.close();
                try {
                    os = new FileOutputStream(mergedPropFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    break Label_0171;
                } finally {
                    os.close();
                }
            }
            os.close();
        }
        final Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(is1, StandardCharsets.UTF_8));
            prop.load(new InputStreamReader(is2, StandardCharsets.UTF_8));
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            prop.store(new OutputStreamWriter(os, StandardCharsets.UTF_8), comment);
        } catch (IOException e2) {
            e2.printStackTrace();
            try {
                is2.close();
                is1.close();
                os.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return;
        } finally {
            try {
                is2.close();
                is1.close();
                os.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        try {
            is2.close();
            is1.close();
            os.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public static int getURLAvailabilityResponseCode(final String url) throws IOException {
        Reporter.log("All SSL Certificates will be accepted...", true);
        final TrustManager[] trustAllCerts = {new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
            }
        }};
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
        } catch (Exception e) {
            throw new PegaWebDriverException("Unable to install all trusting trust manager for accepting all SSL certificates...");
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        final HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(final String hostname, final SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        Reporter.log("Checking availability of URL: " + url, true);
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(180000);
        connection.setReadTimeout(180000);
        connection.setRequestMethod("HEAD");
        final int responseCode = connection.getResponseCode();
        return responseCode;
    }

    public static void mergeTestFlowBranch(final TestEnvironment testEnv) {
        String prpcUrl = testEnv.getConfiguration().getSUTConfig().getURL();
        String filePath = "data" + File.separator + "users.properties";
        if (FileUtil.isFileExists("Data" + File.separator + "users.properties")) {
            filePath = "Data" + File.separator + "users.properties";
        }
        final Properties props = FileUtil.loadPropertiesFile(filePath);
        if (prpcUrl.contains("prweb")) {
            final String str = prpcUrl.split("prweb")[0];
            prpcUrl = str + "prweb/";
        }
        final String restUrl = prpcUrl + "PRRestService/BuildSmoke/Application/Merge?UserIdentifier=NewBuildSmokeDeveloper&Password=cGVnYQ%3D%3D&branchlist=testBranch";
        Reporter.log("Url for branch to merge is " + restUrl, true);
        final Response res = RestAssured.given().auth().preemptive().basic(props.getProperty("BUILD_SMOKE_USER_ID"), props.getProperty("BUILD_SMOKE_PASSWORD")).header("cache-control", "no-cache").header("content-type", "application/x-www-form-urlencoded", new Object[0]).get(restUrl, new Object[0]);
        Reporter.log(String.valueOf(res.statusCode()), true);
        Reporter.log(res.body().asString(), true);
        Reporter.log("Successfully merged testbranchflow.", true);
    }

    public static void mergeTestFlowBranch(final String url, final String username, final String password) {
        String prpcUrl = url;
        if (prpcUrl.contains("prweb")) {
            final String str = prpcUrl.split("prweb")[0];
            prpcUrl = str + "prweb/";
        }
        final String restUrl = prpcUrl + "PRRestService/BuildSmoke/Application/Merge?UserIdentifier=NewBuildSmokeDeveloper&Password=cGVnYQ%3D%3D&branchlist=testBranch";
        Reporter.log("Url for branch to merge is " + restUrl, true);
        final Response res = RestAssured.given().auth().preemptive().basic(username, password).header("cache-control", "no-cache").header("content-type", "application/x-www-form-urlencoded", new Object[0]).get(restUrl, new Object[0]);
        Reporter.log(String.valueOf(res.statusCode()), true);
        Reporter.log(res.body().asString(), true);
        Reporter.log("Successfully merged testbranchflow.", true);
    }

    public enum TransaltionLang {
        ENGLISH("ENGLISH", 0, "en"),
        HINDI("HINDI", 1, "hi"),
        TELUGU("TELUGU", 2, "te"),
        CHINESE_SIMPLIFIED("CHINESE_SIMPLIFIED", 3, "zh-CN"),
        CHINESE_TRADITIONAL("CHINESE_TRADITIONAL", 4, "zh-TW"),
        JAPANESE("JAPANESE", 5, "ja"),
        FRENCH("FRENCH", 6, "fr"),
        SPANISH("SPANISH", 7, "es"),
        GERMAN("GERMAN", 8, "de"),
        POLISH("POLISH", 9, "pl");

        private String lang;

        TransaltionLang(final String name, final int ordinal, final String lang) {
            this.lang = lang;
        }

        public String getLang() {
            return this.lang;
        }
    }
}
