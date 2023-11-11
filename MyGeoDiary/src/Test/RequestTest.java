package Test;

import nominatimRequests.Request;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;



class RequestTest {

    @Test
    void makeNewRequest() throws IOException, ParserConfigurationException, SAXException {
        Request request = new Request();
        request.makeNewRequest(11.97179947,51.34364472);
    }
}