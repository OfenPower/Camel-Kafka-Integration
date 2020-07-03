package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/*
 * Content Enricher-Processor, welche die canonical .json Datei um das 
 * Feld "CreditScore" erweitert
 */
public class CreditAgencyProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		// json String lesen und Credit Score anhand der eingegebenen LoanRequest Werte
		// ermitteln
		String jsonString = exchange.getIn().getBody(String.class);
		
		// Werte aus json auslesen
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(jsonString);
		ObjectNode obj = (ObjectNode) node;
		double creditRequest = node.get("creditRequest").asDouble();
		double currentCapital = node.get("currentCapital").asDouble();
		double monthlyIncome = node.get("monthlyIncome").asDouble();

		// Credit Score zwischen 1 (schlecht) und 10 (perfekt) vergeben
		int score = 1;
		// Wenn mehr Kapital als CreditRequest vorhanden ist => Beste Wertung
		if (currentCapital >= creditRequest) {
			score = 10;
		}
		// Wenn kein/negatives Kapital vorhanden ist => Schlechteste Wertung
		else if (currentCapital <= 0) {
			score = 1;
		}
		// Wenn man nach 10 Jahren Einkommen noch nicht an den CreditRequest
		// rankäme => Schlechtere Wertung
		else if (currentCapital + (monthlyIncome * 12 * 10) < creditRequest) {
			score = 3;
		}
		// Wenn man nach 5 Jahren Einkommen noch nicht an den CreditRequest
		// rankäme => Mittlere Wertung
		else if (currentCapital + (monthlyIncome * 12 * 5) < creditRequest) {
			score = 5;
		}
		// Falls man in unter 5 Jahren den Kredit abbezahlen könnte => Bessere Wertung
		else {
			score = 7;
		}

		// Score als neues json Feld an die Message dranhängen
		obj.put("creditScore", score);

		// Message mit Credit Score rausschicken
		exchange.getIn().setBody(node.toString());
	}

}