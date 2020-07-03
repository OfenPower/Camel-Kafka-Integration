package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/*
 * Content-Enricher Processor, welcher mit einem Credit Score eine Liste
 * von Banken ermittelt, an die der Broker den LoanRequest weiterleiten kann
 */
public class RuleBasedBankProcessor implements Processor {
	
	// Die unique correlationId dient zur globalen Kennzeichnung der Antwort-Messages von Banken
	// zu einem LoanRequest und kommt beim späteren Aggregator zum Einsatz.
	// Bspw. gehören alle Messages mit id=0 zum gleichen LoanRequest , während alle Messages mit
	// id=1 zu einem anderen LoanRequest gehören.
	// 
	// Die correlationId wird für jeden neuen LoanRequest inkrementiert
	private static int correlationId = 0;
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		// json String lesen und mit Credit Score die Banken ermitteln, an die 
		// die Message dynamisch weitergeleitet werden soll
		String jsonString = exchange.getIn().getBody(String.class);
		
		// CreditScore aus json auslesen
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(jsonString);
		ObjectNode obj = (ObjectNode) node;
		int creditScore = node.get("creditScore").asInt();
		
		// json Liste für Banken erzeugen
		ArrayNode arrayNode = obj.putArray("bankList");

		// Es gibt testweise nur drei Banken bank01, bank02, bank03
		// Die Bank "bank01" erhält jede Anfrage, unabhängig vom Credit Score
		arrayNode.add("bank01");

		// bank02 interessiert sich für CreditRequests mit einem CreditScore von 5 oder besser
		if(creditScore >= 5) {
			arrayNode.add("bank02");
		}
		// bank03 interessiert sich für nur für die besten Requests mit dem höchsten CreditScore
		if(creditScore == 10) {
			arrayNode.add("bank03");
		}
		
		// Correlation Id fürs spätere Aggregate in den Body hinzufügen
		obj.put("correlationId", correlationId);
		
		// Debugstring
		System.out.println(node.toPrettyString());
		
		// json mit Bankenliste rausschicken
		exchange.getIn().setBody(node.toString());
		
		// correlationId in eine "Datenbank" eintragen. Die Datenbank wird später vom 
		// Aggregator abgefragt, um herauszufinden auf wieviele Messages der Aggregator
		// warten muss
		ExpectedResponseDatabase.getInstance().map.put(correlationId,arrayNode.size());
		System.out.println("Added <"+correlationId + "," + arrayNode.size() + "> to the database.");
		correlationId++;
	}

}
