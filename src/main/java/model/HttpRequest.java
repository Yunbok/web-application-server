package model;

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


    HttpRequest(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String line = br.readLine();
        String[] headerText = line.split(" ");

        this.method = headerText[0].trim();
        if (headerText[1].contains("?")) {
            this.path = headerText[1].substring(0, headerText[1].indexOf("?"));
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
