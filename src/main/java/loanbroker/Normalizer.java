package loanbroker;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import bank.XmlBankMain.CreditResponse;

public class Normalizer {
	public void normalizeJson(Exchange exchange) {
		// do nothing, because .json File is alright
		System.out.println("Normalized JsonResponse to: " + exchange.getIn().getBody(String.class));
	}
	
	public void normalizeXml(Exchange exchange) {
		
		// XML Message auslesen
		String xmlString = exchange.getIn().getBody(String.class);
		
		// XML in CreditResponse Objekt deserialisieren
		XmlMapper xmlMapper = new XmlMapper();
		CreditResponse xmlLoanResponse = null;
		try {
			xmlLoanResponse = xmlMapper.readValue(xmlString, CreditResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		// XML Values zu .json normalisieren
		ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.createObjectNode();
        ObjectNode obj = (ObjectNode)node;
        obj.put("monthlyPremiums",xmlLoanResponse.monthlyPremiums);
        obj.put("durationInMonths",xmlLoanResponse.durationInMonths);
        obj.put("grantedCredit",xmlLoanResponse.grantedCredit);
        obj.put("interestRatePerMonth",xmlLoanResponse.interestRatePerMonth);
        obj.put("correlationId",xmlLoanResponse.correlationId);
        
        // normalisierte json Datei weiterleiten
        exchange.getIn().setBody(node.toString());
        
        System.out.println("Normalized XmlResponse to: " + node.toString());
		
	}
	
	public void normalizeClearText(Exchange exchange) {

		String msg = exchange.getIn().getBody(String.class);
		String fields[] = msg.split(",");

		double monthlyPremiums = Double.parseDouble(fields[0].split(" ")[1]);
		int durationInMonths = Integer.parseInt(fields[1].split(" ")[1]);
		double grantedCredit = Double.parseDouble(fields[2].split(" ")[1]);
		double interestRatePerMonth = Double.parseDouble(fields[3].split(" ")[1]);
		int correlationId = Integer.parseInt(fields[4].split(" ")[1]);

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("monthlyPremiums",monthlyPremiums);
		node.put("durationInMonths", durationInMonths);
		node.put("grantedCredit",grantedCredit);
		node.put("interestRatePerMonth",interestRatePerMonth);
		node.put("correlationId", correlationId);
		exchange.getIn().setBody(node.toString());
		
		System.out.println("Normalized ClearTextResponse to: " + node.toString());
	}
}
