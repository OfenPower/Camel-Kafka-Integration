package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ToXmlBankTranslator implements Processor {

	/*
    Translates a message from the given format
    {
		"creditRequest" : 123.4,
		"currentCapital" : 123.4,
		"monthlyIncome" : 123.4,
		"creditScore" : 5,
		"bankList" : [bank01, bank02, bank03]
	}
    to the format required by the Xml Bank:
    
    <?xml version="1.0" encoding="UTF-8">
    <CreditRequest>
		<creditRequest>23623.0</creditRequest>
		<currentCapital>2.62346423E8</currentCapital>
		<monthlyIncome>236234.0</monthlyIncome>
		<creditScore>10</creditScore>
	</CreditRequest>
	
     */
	@Override
	public void process(Exchange exchange) throws Exception {
		
		// Json Message auslesen
		String jsonString = exchange.getIn().getBody(String.class);
		
		ObjectMapper mapper = new ObjectMapper();
        JsonNode inputNode = mapper.readTree(jsonString);
        ObjectNode inputObject = (ObjectNode) inputNode;
        inputObject.remove("bankList");
		
		// XML Message rausschicken
		exchange.getIn().setBody(JsonToXml.jsonToXml(inputObject.toString()));
		
	}

}
