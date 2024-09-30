package com.test.mtlsclient.controller;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.http.client.methods.HttpPost;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/car")
public class CarController {
    private static final Logger log = LoggerFactory.getLogger(CarController.class);

    @PostMapping("/unlock")
    public ResponseEntity<String> unlock() throws Exception {
        createRequest();
        return ResponseEntity.ok().body("Car unlock!");
    }

    private String createRequest() throws Exception {
        String cert = "-----BEGIN CERTIFICATE-----\n" +
                "MIIDzDCCArQCFHAfxCYShGe+vq3K+VCTxckSntaXMA0GCSqGSIb3DQEBCwUAMIGZ\n" +
                "MQswCQYDVQQGEwJCUjERMA8GA1UECAwIU0FPIFBVTE8xEjAQBgNVBAcMCVNBTyBQ\n" +
                "QVVMTzEVMBMGA1UECgwMTEFCIERPIEJSVU5PMREwDwYDVQQLDAhdVEVTVCBDQTES\n" +
                "MBAGA1UEAwwJbG9jYWxob3N0MSUwIwYJKoZIhvcNAQkBFhZhZG1pbkB0ZXN0Y2Eu\n" +
                "bG9jYWxob3N0MB4XDTI0MDkzMDExMDUwOFoXDTI1MDkzMDExMDUwOFowgaoxCzAJ\n" +
                "BgNVBAYTAkJSMRIwEAYDVQQIDAlTQU8gUEFVTE8xEjAQBgNVBAcMCVNBTyBQQVVM\n" +
                "TzEVMBMGA1UECgwMTEFCIERPIEJSVU5PMRgwFgYDVQQLDA9URVNUIENMSUVOVCBD\n" +
                "U1IxEjAQBgNVBAMMCWxvY2FsaG9zdDEuMCwGCSqGSIb3DQEJARYfYWRtaW5AdGVz\n" +
                "dC1jbGllbnQtY3NyLmxvY2FsaG9zdDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC\n" +
                "AQoCggEBAKLmsIzUj77q2jRkC6cnVJdesKmzZ1BESVDEJQbHPMzACnnc7RArjpXz\n" +
                "bQ5Evmis6D9DOVUHc4akjNzsTgzuaLwAlM5C4tlLkIQQtFkfLf27FYe0lHFbZpJ5\n" +
                "TOHLgsVVV90XA/tfbcn8HOpeiHPc/GLjjV8Zn1WOZsRSzv+mPNo4LYYgUY01xdby\n" +
                "29z/RTqatBSik7thgbsO6PDy+aUsztm2U5/jWieLiZBBjTckNEllI+KlIRRieSkE\n" +
                "fpFjiwKbB1nFL5iQkZVQIhkM+QEokYEOa0OxH7ppoEXxbhNoHWsexfHQmf+hPv2u\n" +
                "45/lGAjKi5Vqz5FWmj4+wlSjyw+yR78CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEA\n" +
                "LypK+IVhNLMQU+Ex4GHvVDkeBs4Dgmkzmr44Q8Qj6w/271N86yAD/RO+ftcRs1e9\n" +
                "fLCtTjFnhK3lzhYSuyztNAPJ0YIKvX0hbDnEeUgYvdoB1Ocy1iu4airLtAJTN/3j\n" +
                "T+fn4pAFTeUf8RswYamPt41y62Y/7Ldz0i+/ok3zGkh+M56U5CnCPCT7kkalbGov\n" +
                "EslnsGI0dzN8oh2OU2TCq4mfCoLR/Z4ZYWWBG286i2jLu5OPR/jw4GWL0tv85uhn\n" +
                "3AIqFLH/zAtWi41sZI0pMNkzh3EJ4gHLmWOK6VVldB19+Qbm0mPEjDAeLVzK5zQi\n" +
                "yGyJNWUgnyNuia1wDsLkiw==\n" +
                "-----END CERTIFICATE-----\n";
        String key = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCi5rCM1I++6to0\n" +
                "ZAunJ1SXXrCps2dQRElQxCUGxzzMwAp53O0QK46V820ORL5orOg/QzlVB3OGpIzc\n" +
                "7E4M7mi8AJTOQuLZS5CEELRZHy39uxWHtJRxW2aSeUzhy4LFVVfdFwP7X23J/Bzq\n" +
                "Xohz3Pxi441fGZ9VjmbEUs7/pjzaOC2GIFGNNcXW8tvc/0U6mrQUopO7YYG7Dujw\n" +
                "8vmlLM7ZtlOf41oni4mQQY03JDRJZSPipSEUYnkpBH6RY4sCmwdZxS+YkJGVUCIZ\n" +
                "DPkBKJGBDmtDsR+6aaBF8W4TaB1rHsXx0Jn/oT79ruOf5RgIyouVas+RVpo+PsJU\n" +
                "o8sPske/AgMBAAECggEAB7IONRj1kXdWx5LSWhspi90VS28GOmx/ailZ9nADF0kb\n" +
                "NZaSPGlSWxMc28ezlw5UCmkKlFG9y2aKoQUoysOYfwkn/nZ6cPl44kRHwM4QRiRI\n" +
                "aBjHynMe7Vx9nU0X6GqXtAIaVg20Cg627vt4RsDxy1okm5GCTXBkunGPBdS8mzJo\n" +
                "6IWGH7+3+GRaKju8edt/+PhRdp7sdBvIy5j4cmkZKSR1u/HJSQsyD5f/8sM4eLCT\n" +
                "WvozEYQA0yo9cqeY7jbpDcdVB0yqhI9sHtcW4irDvW3mBejnpefubiu5rBNWhW0D\n" +
                "WKJlqxRVUJQRv4RR+lvJC9i//KNtY505bTm1MY0BwQKBgQDOOR3TXtgxDB94gq3F\n" +
                "XP3SZX1ExLwEG6oU/Bu8jDHOTI9vVh+NCJxKSpCOKcNYg1dL21kc2CUDNi/OaM5U\n" +
                "UumBHk3HhdfMWOteL+UAOG2LeH6uEqu1npQvbTTibQ4w6Rz5p4HSihJrwTihaCtr\n" +
                "O/EQ89rB1BX2XthX8VS05I0FYwKBgQDKOKIGrOPjgGFmHaJPHR5iTor6YR2hF8RW\n" +
                "+/ikAwXzzDDoOURcbpr66D/q+pgoLyVSxgLAdItS9TkvbEzQVb4nQBCkYfYjupRW\n" +
                "PuKz4mSKUTDb9481SYMOcLP1gY33sK5ipR6hTQp+KICmeAzmdbd2B/JMzC9GBOOu\n" +
                "+zHH151g9QKBgE2HD31tRJ17z0EA788o3IuCeAuoYn6w38ov1UWW9wp1od7bY9Uj\n" +
                "jY4oZhvr+3KFPgZYmjHL2G5SqrpQkHEHEp3mBJuj57L9Is8v9kmU7SnnzMgoYMFQ\n" +
                "sqj5x03wbctZd/krThUWr0IvS9jyTpBr4n7bDjycPP14PqEhKxUubw29AoGAVs3g\n" +
                "n0TvveSJ6q+l0JgLRt+4QKoXceMl4MDghmbV00U/aS/L7HF+DV5Msa1IS1eRiep9\n" +
                "m+drNv0UwwqWHofvzCFwA2AZrFSXAD6WSelQhx86/fFSg+k+XKB8X1JxgnVRJf8D\n" +
                "GvJbIrR4JP07bGHzkrLui5DKaiAHLEGfqM0hPVECgYEArRvPOWr+5ySF8SbMPHyG\n" +
                "mnYDr3MmOSJyWypAMrECdWtasjLmOQG7pdhrML3Qfeg2NXf6BF6yI03mRjZ341TO\n" +
                "Z2TRoQZOwIt+ptudcgkq9sXWZG8aKmhloJfYJAgM282JSSNyJ5S1pcyQYwa0kjwG\n" +
                "7jTQW9gU4jTNnPYrfjbiAcY=\n" +
                "-----END PRIVATE KEY-----";
        String ca = "-----BEGIN CERTIFICATE-----\n" +
                "MIIEFTCCAv2gAwIBAgIUDnfzikTu39e5T0sF+ZBiqHrYITowDQYJKoZIhvcNAQEL\n" +
                "BQAwgZkxCzAJBgNVBAYTAkJSMREwDwYDVQQIDAhTQU8gUFVMTzESMBAGA1UEBwwJ\n" +
                "U0FPIFBBVUxPMRUwEwYDVQQKDAxMQUIgRE8gQlJVTk8xETAPBgNVBAsMCF1URVNU\n" +
                "IENBMRIwEAYDVQQDDAlsb2NhbGhvc3QxJTAjBgkqhkiG9w0BCQEWFmFkbWluQHRl\n" +
                "c3RjYS5sb2NhbGhvc3QwHhcNMjQwOTMwMTEwMjU0WhcNMjUwOTMwMTEwMjU0WjCB\n" +
                "mTELMAkGA1UEBhMCQlIxETAPBgNVBAgMCFNBTyBQVUxPMRIwEAYDVQQHDAlTQU8g\n" +
                "UEFVTE8xFTATBgNVBAoMDExBQiBETyBCUlVOTzERMA8GA1UECwwIXVRFU1QgQ0Ex\n" +
                "EjAQBgNVBAMMCWxvY2FsaG9zdDElMCMGCSqGSIb3DQEJARYWYWRtaW5AdGVzdGNh\n" +
                "LmxvY2FsaG9zdDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJD2idYe\n" +
                "E4kRtw8gvnucovWwKbM7GrNUxIhu5SbgqFAXe7YPoqRrEARAmltaXPrd1dD10oud\n" +
                "a10kKz4qb9nQcVsO953ANlDA+qw8i07eNzqIL8OKNsF85yKCaqA55LBZTgZY8kRZ\n" +
                "mHGoESUq9xKDigJOEa3znnVY3NbFQ1V8486gtUMMPpTNyyvrr8SW0IXxfAiD0O0J\n" +
                "ZCY0yVErFyRP71efizGkc7pl2vXVztTr+bGZm9oehri7OSAK8thBK9xxC2gjZlRa\n" +
                "6M5eQY5Ph2o0Qps5JSF86CI585/1kZJ2yd7GhpGphQbgypHMF3VwJtOSD2IUPdKN\n" +
                "bLGWU4wRpu5bOeECAwEAAaNTMFEwHQYDVR0OBBYEFHYqxxGawQlWB7AuGJO6Ky4u\n" +
                "3DIcMB8GA1UdIwQYMBaAFHYqxxGawQlWB7AuGJO6Ky4u3DIcMA8GA1UdEwEB/wQF\n" +
                "MAMBAf8wDQYJKoZIhvcNAQELBQADggEBAH/8VcLYQV++0jepB0Tv8lWJ+RdYU/S4\n" +
                "uxmiJNhrK5Znb3s6ITAv/qdbYj7KqPvR6DO+kckPZZehrorptm7nDAZQ0kvF6ikD\n" +
                "sM9KrnIlxe0RYLSrtZlQL4APdU5PnXCBhX/pcMYGs2y6sYm5P2dYPr4nIcpY3yC/\n" +
                "OYb4lH7DIoCC51zUFhSv20dZ/rpDuXcrnCesfED8ZlNo2B6rEl9nWzw4pBbVYZlz\n" +
                "QBX1E8STh3d1pb44s2bXH1ifvnYFCjfuc3M2Mec7rXrifbE0uHbq7NL2kxFhp0Ig\n" +
                "WNkYC3jK/yEsE8V+cQ7xEtUN/7Gzn9y41yno6A44tS8hvi/BoJ1Ii8w=\n" +
                "-----END CERTIFICATE-----\n"; // OPICIONAL

        X509Certificate caCert = convertToX509Cert(ca);
        X509Certificate certificate = convertToX509Cert(cert);
        PrivateKey privateKey = convertToPrivateKey(key);

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, "123".toCharArray());
        keyStore.setCertificateEntry("cert-alias", certificate);
        keyStore.setKeyEntry("key-alias", privateKey, "123".toCharArray(), new Certificate[]{certificate});

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, "123".toCharArray());
        trustStore.setCertificateEntry("ca-cert", caCert);

        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(keyStore, "123".toCharArray())
                .loadTrustMaterial(trustStore, null)
                .build();

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000) // 5 ms
                .setSocketTimeout(5000) // 5 ms
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .setDefaultRequestConfig(requestConfig)
                .build();


        HttpPost postRequest = new HttpPost("https://localhost:8443/api/v1/engine/start");
        postRequest.addHeader("Content-Type", "application/json");

        String jsonBody = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
        StringEntity entity = new StringEntity(jsonBody);
        postRequest.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(postRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        log.info(String.valueOf(statusCode));

        HttpEntity responseEntity = response.getEntity();
        if (responseEntity != null) {
            String result = EntityUtils.toString(responseEntity);
            log.info(result);
            return result;
        }

        return "";
    }

    public X509Certificate convertToX509Cert(String cert) throws Exception {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        try (ByteArrayInputStream certInputStream = new ByteArrayInputStream(cert.getBytes(StandardCharsets.UTF_8))) {
            return (X509Certificate) factory.generateCertificate(certInputStream);
        }
    }

    public PrivateKey convertToPrivateKey(String key) throws Exception {
        String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
