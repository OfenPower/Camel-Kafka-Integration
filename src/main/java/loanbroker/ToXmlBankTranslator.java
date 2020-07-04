package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/*
 * Translator-Processor, welcher die eingehende .json Message für die Xml-Bank in eine .xml umwandelt.
 * Die Bankenliste wird hier entfernt
 */
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
    
    <CreditRequest>
		<creditRequest>123.4</creditRequest>
		<currentCapital>123.4</currentCapital>
		<monthlyIncome>123.4</monthlyIncome>
		<creditScore>5</creditScore>
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
        
        // Json zu Xml konvertieren
        String xmlString = JsonToXml.jsonToXml(inputObject.toString());
        
		// XML Message rausschicken
		exchange.getIn().setBody(xmlString);
		
	}

}
