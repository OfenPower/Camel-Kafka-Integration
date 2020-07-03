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
	// zu einem LoanRequest und kommt beim sp�teren Aggregator zum Einsatz.
	// Bspw. geh�ren alle Messages mit id=0 zum gleichen LoanRequest , w�hrend alle Messages mit
	// id=1 zu einem anderen LoanRequest geh�ren.
	// 
	// Die correlationId wird f�r jeden neuen LoanRequest inkrementiert
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
		
		// json Liste f�r Banken erzeugen
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
		obj.put("correlationId", correlationId);
		
		// Debugstring
		System.out.println(node.toPrettyString());
		
		// json mit Bankenliste rausschicken
		exchange.getIn().setBody(node.toString());
		
		// correlationId in eine "Datenbank" eintragen. Die Datenbank wird sp�ter vom 
		// Aggregator abgefragt, um herauszufinden auf wieviele Messages der Aggregator
		// warten muss
		ExpectedResponseDatabase.getInstance().map.put(correlationId,arrayNode.size());
		System.out.println("Added <"+correlationId + "," + arrayNode.size() + "> to the database.");
		correlationId++;
	}

}
