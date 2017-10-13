package org.kablambda.apis;

import com.google.api.client.http.GenericUrl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class UrlUtils {
    public static UrlBuilder builder(String base) {
        return new UrlBuilder(base);
    }

    public static class UrlBuilder {
        private final String base;
        private final Map<String,Object> params = new HashMap<>();

        private UrlBuilder(String base) {

            this.base = base;
        }

        public UrlBuilder param(String k, Object v) {
            params.put(k,v);
            return this;
        }

        public GenericUrl build() {
            StringBuilder sb = new StringBuilder(base);
            String sep = "?";
            for (Map.Entry<String, Object> e : params.entrySet()) {
                if (e.getValue() != null) {
                    try {
                        sb.append(sep).append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue().toString(), "UTF8"));
                    } catch (UnsupportedEncodingException e1) {
                        throw new RuntimeException(e1);
                    }
                    sep = "&";
                }
            }
            return new GenericUrl(sb.toString());
        }
    }
}
