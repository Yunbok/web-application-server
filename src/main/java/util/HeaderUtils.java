package util;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HeaderUtils {

    public static String getUriInHeader(String header) {
        String[] headerArr = header.split(" ");
        return headerArr[1];
    }

    public static String getRealUrl(String url) {
        int index = url.indexOf("?");

        if (index == -1) {
            return url;
        }
        return url.substring(0, index);
    }

    public static String getParamInUrl(String url) {
        int index = url.indexOf("?");

        if (index == -1) {
            return "";
        }

        return url.substring(index+1);
    }

    public static Map<String, String> paramToMap(String paramText) {
        int length = paramText.length();
        String[] paramArr = paramText.split("&");
        Map<String, String> paramMap = new ConcurrentHashMap<>();

        if (length == 0) {
            return paramMap;
        }

        for (String param : paramArr) {
            paramMap = addSubstrParam(param, paramMap);
        }

        return paramMap;
    }

    private static Map<String,String> addSubstrParam (final String param,final Map<String, String> paramMap) {
        final Map<String , String> resultMap = new ConcurrentHashMap<>(paramMap);
        final int index = param.indexOf("=");

        if (index == -1 ) {
            return resultMap;
        }

        final String key = param.substring(0, index);
        final String value = param.substring(index + 1);

        resultMap.put(key, value);

        return resultMap;
    }

    public static int getContentLength(String line) {
        String[] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }

}
