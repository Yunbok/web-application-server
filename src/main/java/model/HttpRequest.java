package model;

import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final Map<String, String> header = new HashMap<>();
    private final Map<String, String> parameter = new HashMap<>();
    private final String method;
    private final String path;

    public HttpRequest(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String line = br.readLine();
        String[] headerText = line.split(" ");
        String queryString = "";
        this.method = headerText[0].trim();
        if (headerText[1].contains("?")) {
            this.path = headerText[1].substring(0, headerText[1].indexOf("?"));
            queryString = headerText[1].substring(headerText[1].indexOf("?") + 1);
        } else {
            this.path = headerText[1];
        }

        while (!line.isEmpty()) {
            line = br.readLine();
            if ( line == null) {
                break;
            }
            int index = line.indexOf(":");
            if ( index > 0 ) {
                header.put(line.substring(0,index).trim(), line.substring(index + 1).trim());
            }
        }

        if (method.equals("POST")) {
            queryString = IOUtils.readData(br,Integer.parseInt(header.get("Content-Length")));
        }
        for (String param : queryString.split("&")  ) {
            if (param.contains("=")) {
                String[] text = param.split("=");
                parameter.put(text[0], text[1]);
            }
        }
    }

    public String getMethod() {
        return this.method;
    }
    public String getPath() {
        int index = path.indexOf("?");

        if (index == -1) {
            return path;
        }
        return path.substring(0, index);
    }
    public String getHeader(String key) {
        if ( !header.containsKey(key) ){
            return "";
        }
        return header.get(key);
    }
    public String getParameter(String key) {
        if ( !parameter.containsKey(key) ){
            return "";
        }
        return parameter.get(key);
    }

}
