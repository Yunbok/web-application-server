package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HeaderUtils;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = br.readLine();
            final String url = HeaderUtils.getUriInHeader(line);
            final String realUrl = HeaderUtils.getRealUrl(url);
            int contentLength = 0;
            final String[] header = line.split(" ");


            while(!"".equals(line)) {
                line = br.readLine();
                log.debug(line);
                if (line.contains("Content-Length")) {
                    contentLength = HeaderUtils.getContentLength(line);
                }
            }

//            param = HeaderUtil.getParamInUrl(url);
//            Map<String, String> map =HeaderUtil.paramToMap(param);
            log.debug(line);
            if (url.startsWith("/user/create")) {
                String queryString = "";
                if ( "GET".equals(header[0])) {
                    final int index = url.indexOf("?");
                    queryString = url.substring(index + 1);
                } else if ("POST".equals(header[0])) {
                    String body = IOUtils.readData(br,contentLength);
                    queryString = body;
                }
                final Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);
                log.debug("params {} :" , params);
                final User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                log.debug("User {} :" , user);
            } else {
                final DataOutputStream dos = new DataOutputStream(out);
//            byte[] body = "Hello World22".getBytes();
                final byte[] body = Files.readAllBytes(new File("./webapp" + realUrl).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        } catch (final IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}