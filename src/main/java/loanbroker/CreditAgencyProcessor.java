package loanbroker;

import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreditAgencyProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		// json String lesen und Credit Score anhand der eingegebenen LoanRequest Werte
		// ermitteln
		String jsonString = exchange.getIn().getBody(String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(jsonString);

		// Werte aus json auslesen
		double creditRequest = node.get("creditRequest").asDouble();
		double currentCapital = node.get("currentCapital").asDouble();
		double monthlyIncome = node.get("monthlyIncome").asDouble();

		// Score zwischen 1-10 vergeben
		int score;
		// Wenn mehr Kapital als CreditRequest vorhanden ist => Beste Wertung
		if (currentCapital >= creditRequest) {
			score = 10;
		}
		// Wenn kein/negatives Kapital vorhanden ist => Schlechteste Wertung
		if (currentCapital <= 0) {
			score = 1;
		}
		// Wenn man nach mehr als 10 Jahren Einkommen noch nicht an den CreditRequest
		// rankäme => Schlehctere Wertung
		if (currentCapital + (monthlyIncome * 12 * 10) < creditRequest) {
			score = 3;
		}

		String ssn = exchange.getIn().getHeader(Constants.PROPERTY_SSN, String.class);
		Random rand = new Random();
		// int score = (int) (rand.nextDouble() * 600 + 300);
		// int hlength = (int) (rand.nextDouble() * 19 + 1);

	}

}