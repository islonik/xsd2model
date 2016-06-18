package org.examples.xsd2model.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.examples.xsd2model.model.request.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final static Logger log = LoggerFactory.getLogger(TransformerService.class);

    // I don't like static initialization
    private static final ObjectMapper mapper = ((Supplier<ObjectMapper>) () -> {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper;
    }).get();

    /**
     * Such kind of preparation could significantly improve performance.
     */
    private static final Unmarshaller requestTypeUnmarshaller = ((Supplier<Unmarshaller>) () -> {
        try {
            JAXBContext context = JAXBContext.newInstance(RequestType.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return unmarshaller;
        } catch(JAXBException jaxbe) {
            log.error(jaxbe.getMessage(), jaxbe);
            System.exit(-1); // -1 like Exception; 0 like Ok; 1 like Error
            return null;
        }
    }).get();


    public static String object2json(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static Object json2object(String json, Class className) throws IOException {
        return mapper.readValue(json, className);
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

    public static RequestType xmlRequestType2object(String xml) throws Exception {
        Reader reader = new StringReader(xml);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader xmlReader = factory.createXMLStreamReader(reader);

        return requestTypeUnmarshaller.unmarshal(xmlReader, RequestType.class).getValue();
    }

}
