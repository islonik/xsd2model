package org.examples.xsd2model.services;

import org.examples.xsd2model.model.UserType;
import org.examples.xsd2model.model.request.Message;
import org.examples.xsd2model.model.request.RequestType;
import org.examples.xsd2model.model.response.ResponseType;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Nikita on 4/1/2016.
 */
public class TransformerServiceTest {

    private UserType userType;

    @Before
    public void setUp() {
        UserType userType = new UserType();
        userType.setLogin("nikita");
        userType.setPassword("password22");
        this.userType = userType;
    }

    @Test
    public void testObject2Xml2Object2Xml() throws Exception {
        // object
        RequestType requestType1 = new RequestType();
        requestType1.setUser(userType);
        Message message = new Message();
        message.setLog("log-1234567");
        message.setCommand("mkdir test");
        requestType1.setMessage(message);

        // toXml
        String xml1 = TransformerService.object2xml(requestType1);
        // toObject
        RequestType requestType2 = (RequestType) TransformerService.xml2object(xml1, RequestType.class);
        // toXml
        String xml2 = TransformerService.object2xml(requestType2);

        Assert.assertEquals(xml1, xml2);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<requestType xmlns:ns2=\"http://xsd2model.org/request\" xmlns:ns3=\"http://xsd2model.org/core\">\n" +
                "    <ns2:user>\n" +
                "        <ns3:login>nikita</ns3:login>\n" +
                "        <ns3:password>password22</ns3:password>\n" +
                "    </ns2:user>\n" +
                "    <ns2:message>\n" +
                "        <ns2:log>log-1234567</ns2:log>\n" +
                "        <ns2:command>mkdir test</ns2:command>\n" +
                "    </ns2:message>\n" +
                "</requestType>\n";
        Assert.assertEquals(xml, xml1);
    }

    @Test
    public void testObject2Json2Object2Json() throws Exception {
        // object
        ResponseType responseType0 = new ResponseType();
        responseType0.setUser(userType);
        responseType0.setCode("0");
        responseType0.setDesc("Ok!");

        // toJson
        String json1 = TransformerService.object2json(responseType0);
        // toObject
        ResponseType responseType2 = (ResponseType) TransformerService.json2object(json1, ResponseType.class);
        // toJson
        String json2 = TransformerService.object2json(responseType2);

        Assert.assertEquals(json1, json2);
        String json = "{\n" +
                "  \"user\" : {\n" +
                "    \"login\" : \"nikita\",\n" +
                "    \"password\" : \"password22\"\n" +
                "  },\n" +
                "  \"code\" : \"0\",\n" +
                "  \"desc\" : \"Ok!\"\n" +
                "}";
        Assert.assertEquals(json.replaceAll("\r", "\n"), json1.replaceAll("\r\n", "\n"));
    }

    @Test
    public void testPerformanceTest() throws Exception {
        final String xmlResponse = new String(Files.readAllBytes(Paths.get("src/test/resources/performance.xml")));
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 150; i++) {
            RequestType requestType = TransformerService.xmlRequestType2object(xmlResponse);
        }
        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;

        System.out.println("Performance test result time = " + resultTime);
        Assert.assertTrue(resultTime < (1000));
    }
}
