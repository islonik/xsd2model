package org.examples.xsd2model.services;

import org.examples.xsd2model.model.UserType;
import org.examples.xsd2model.model.request.Message;
import org.examples.xsd2model.model.request.RequestType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by nikilipa on 8/20/16.
 */
public class JaxbServicesTest {

    private JaxbServices jaxbServices;
    private UserType userType;

    @Before
    public void setUp() throws JAXBException {
        jaxbServices = new JaxbServices();

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
        String xml1 = jaxbServices.object2xml(requestType1);
        // toObject
        RequestType requestType2 = jaxbServices.xml2object(xml1);
        // toXml
        String xml2 = jaxbServices.object2xml(requestType2);
        // toXml using different method
        RequestType requestType3 = jaxbServices.xml2object(xml1);
        String xml3 = jaxbServices.object2xml2(requestType3);

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns2:requestType xmlns=\"http://xsd2model.org/examples/xsd2model/model/core\" xmlns:ns2=\"http://xsd2model.org/examples/xsd2model/model/request\">\n" +
                "    <ns2:user>\n" +
                "        <login>nikita</login>\n" +
                "        <password>password22</password>\n" +
                "    </ns2:user>\n" +
                "    <ns2:message>\n" +
                "        <ns2:log>log-1234567</ns2:log>\n" +
                "        <ns2:command>mkdir test</ns2:command>\n" +
                "    </ns2:message>\n" +
                "</ns2:requestType>\n";
        Assert.assertEquals(xml, xml1);
        Assert.assertEquals(xml1, xml2);
        Assert.assertEquals(xml2, xml3);
    }

    @Test
    public void testPerformanceTest() throws Exception {
        final String xmlResponse = new String(Files.readAllBytes(Paths.get("src/test/resources/performance.xml")));
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 150; i++) {
            RequestType requestType = jaxbServices.xml2object(xmlResponse);
            Assert.assertNotNull(requestType.getUser());
            Assert.assertNotNull(requestType.getUser().getLogin());
        }
        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;

        System.out.println("Performance test result time = " + resultTime);
        Assert.assertTrue(resultTime < (1000));
    }

}
