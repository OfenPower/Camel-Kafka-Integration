package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.camel.support.SimpleRegistry;

public class RuleBasedBankProcessor implements Processor {

	private static int correlationId = 0;
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		// json String lesen und mit Credit Score die Banken ermitteln, an die 
		// die Message dynamisch weitergeleitet werden soll
		String jsonString = exchange.getIn().getBody(String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(jsonString);
		ObjectNode obj = (ObjectNode) node;

		// CreditScore aus json auslesen
		int creditScore = node.get("creditScore").asInt();
		
		// json Liste erzeugen
		ArrayNode arrayNode = obj.putArray("bankList");

		// Es gibt testweise nur drei Banken bank01, bank02, bank03
		// Die Bank "bank01" erh�lt jede Anfrage, unabh�ngig vom Credit Score
		arrayNode.add("bank01");

		// bank02 interessiert sich f�r CreditRequests mit einem CreditScore von 5 oder besser
		if(creditScore >= 5) {
			arrayNode.add("bank02");
		}
		// bank03 interessiert sich f�r nur f�r die besten Requests mit dem h�chsten CreditScore
		if(creditScore == 10) {
			arrayNode.add("bank03");
		}
		
		// Correlation Id f�rs sp�tere Aggregate in den Body hinzuf�gen
		obj.put("correlationId", correlationId); // fix
		
		System.out.println(node.toPrettyString());
		
		// json mit Bankenliste rausschicken
		exchange.getIn().setBody(node.toString());

		ExpectedResponseDatabase.getInstance().map.put(correlationId,arrayNode.size());
		System.out.println("Added <"+correlationId + "," + arrayNode.size() + "> to the database.");
		correlationId++;
	}

}
