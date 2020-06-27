package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ToXmlBankTranslator implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		// Json Message auslesen
		String jsonString = exchange.getIn().getBody(String.class);
		
		// XML Mapper zum translaten verwenden
		ObjectMapper objectMapper = new XmlMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		String xml = objectMapper.writeValueAsString(jsonString);
		
		// XML Message rausschicken
		exchange.getIn().setBody(xml);
		
	}

}
