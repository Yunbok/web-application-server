package util;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class HeaderUtilsTest {

    @Test
    public void url_분리() {

        String header = "GET /index.html HTTP/1.1";

        String url = HeaderUtils.getUriInHeader(header);

        assertThat("/index.html", is(url));
    }



    @Test
    public void url_파라메터_제거버전() {

        String pullUrl = "/index.html?name=123&password=1234";

        String url = HeaderUtils.getRealUrl(pullUrl);

        assertThat("/index.html", is(url));
    }

    @Test
    public void url_제거버전() {

        String pullUrl = "/index.html?name=123&password=1234";

        String url = HeaderUtils.getParamInUrl(pullUrl);

        assertThat("name=123&password=1234", is(url));
    }

    @Test
    public void url_제거버전2() {

        String pullUrl = "/index.html";

        String url = HeaderUtils.getParamInUrl(pullUrl);

        assertThat("", is(url));
    }

    @Test
    public void paramToMap() {

        String pullUrl = "name=123&password=1234";

        Map<String,String> map =  HeaderUtils.paramToMap(pullUrl);

        assertThat("123", is(map.get("name")));
        assertThat("1234", is(map.get("password")));
    }
}
