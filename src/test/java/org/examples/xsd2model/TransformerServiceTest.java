package org.examples.xsd2model;

import org.examples.xsd2model.common.UserType;
import org.examples.xsd2model.request.RequestType;
import org.examples.xsd2model.response.ResponseType;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

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
        requestType1.setCommand("mkdir test");

        // toXml
        String xml1 = TransformerService.object2xml(requestType1);
        // toObject
        RequestType requestType2 = (RequestType) TransformerService.xml2object(xml1, RequestType.class);
        // toXml
        String xml2 = TransformerService.object2xml(requestType2);

        Assert.assertEquals(xml1, xml2);
        System.out.println(xml1);
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
        System.out.println(json1);
    }
}
