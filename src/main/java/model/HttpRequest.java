package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    final Map<String , String > header = new HashMap<>();
    final String method;
    final String path;


    HttpRequest(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String line = br.readLine();
        String[] headerText = line.split(" ");
        this.method = headerText[0].trim();
        this.path = headerText[1].trim();


        while (!line.isEmpty()) {
            line = br.readLine();
            System.out.println(line);
            if ( line == null) {
                break;
            }
            String[] lineArr = line.split(":");
            if ( lineArr.length > 1) {
                header.put(lineArr[0].trim(), lineArr[1].trim());
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

}
