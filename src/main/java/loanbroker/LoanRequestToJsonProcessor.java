package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/*
 * Translator-Processor, welcher ein Objekt vom Typ "LoanRequestMessage" in
 * eine .json Datei (String) umwandelt
 */
public class LoanRequestToJsonProcessor implements Processor {
	
    @Override
    public void process(Exchange exchange) throws Exception {
    	
    	// Income-Message als LoanRequestMessage Objekt auslesen 
        LoanRequestMessage request = exchange.getIn().getBody(LoanRequestMessage.class);

        // .json Datei mit den Werten der Message erzeugen
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.createObjectNode();
        ObjectNode obj = (ObjectNode)node;
        obj.put("creditRequest",request.getCreditRequest());
        obj.put("currentCapital",request.getCurrentCapital());
        obj.put("monthlyIncome",request.getMonthlyIncome());
        
        // Alten Body der Message durch die erzeugte .json Datei ersetzen
        exchange.getIn().setBody(node.toString());
    }
}
 