package org.examples.xsd2model.services;

import org.examples.xsd2model.model.request.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Provide services for serialization and deserialization from Xml to Object and visa versa.
 * JAXBContext is thread safe and should only be created once and reused to avoid the cost of initializing the metadata multiple times.
 * Marshaller and Unmarshaller are not thread safe, but are lightweight to create and could be created per operation.
 */
public class JaxbServices {

    private final static Logger log = LoggerFactory.getLogger(JaxbServices.class);

    private JAXBContext context;

    public JaxbServices() throws JAXBException {
        context = JAXBContext.newInstance(RequestType.class);
    }

    public String object2xml(RequestType requestType) throws JAXBException, XMLStreamException {
        Writer writer = new StringWriter();

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(requestType, writer);

        return writer.toString();
    }

    // Without @XmlRootElement annotation we can use this code instead of above
    public String object2xml2(RequestType requestType) throws JAXBException, XMLStreamException {
        Writer writer = new StringWriter();

        JAXB.marshal(requestType, writer);

        return writer.toString();
    }

    public RequestType xml2object(String xml) throws JAXBException, XMLStreamException  {
        // xml to XmlStreamReader
        Reader reader = new StringReader(xml);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader xmlReader = factory.createXMLStreamReader(reader);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        JAXBElement jaxbElement = unmarshaller.unmarshal(xmlReader, RequestType.class);

        return (RequestType)jaxbElement.getValue();
    }

}
