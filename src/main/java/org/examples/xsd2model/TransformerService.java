package org.examples.xsd2model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.xml.bind.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.function.Supplier;

/**
 * Created by Nikita on 4/1/2016.
 */
public class TransformerService {

    /**
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
    **/

    // or
    private static final ObjectMapper mapper = ((Supplier<ObjectMapper>) () -> {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper;
    }).get();

    public static String object2json(Object object) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static Object json2object(String json, Class className) throws IOException {
        return mapper.readValue(json, className);
    }

    public static boolean isJson(String json) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String object2xml(Object object) {
        final StringWriter stringWriter = new StringWriter();
        JAXB.marshal(object, stringWriter);
        return stringWriter.toString();
    }

    public static Object xml2object(String xml, Class className) throws Exception {
        JAXBContext context = JAXBContext.newInstance(className);

        Reader reader = new StringReader(xml);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader xmlReader = factory.createXMLStreamReader(reader);

        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller.unmarshal(xmlReader, className).getValue();
    }

}
