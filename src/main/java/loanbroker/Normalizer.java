package loanbroker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.camel.Exchange;

public class Normalizer {
	public void normalizeJson(Exchange exchange) {
	}
	
	public void normalizeXml(Exchange exchange) {
		System.out.println("normalizeXml");
	}
	
	public void normalizeClearText(Exchange exchange) {

		String msg = exchange.getIn().getBody(String.class);
		String fields[] = msg.split(",");

		double monthlyPremiums = Double.parseDouble(fields[0].split(" ")[1]);
		int durationInMonths = Integer.parseInt(fields[1].split(" ")[1]);
		double grantedCredit = Double.parseDouble(fields[2].split(" ")[1]);
		double interestRatePerMonth = Double.parseDouble(fields[3].split(" ")[1]);

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("monthlyPremiums",monthlyPremiums);
		node.put("durationInMonths", durationInMonths);
		node.put("grantedCredit",grantedCredit);
		node.put("interestRatePerMonth",interestRatePerMonth);
		exchange.getIn().setBody(node.toString());
		System.out.println("Normalized ClearTextResponse to: " + node.toString());
	}
}
