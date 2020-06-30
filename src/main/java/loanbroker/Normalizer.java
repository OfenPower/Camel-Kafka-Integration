package loanbroker;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import bank.XmlBankMain.CreditResponse;

public class Normalizer {
	public void normalizeJson(Exchange exchange) {
		System.out.println("normalizeJson");
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
			// TODO Auto-generated catch block
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
        
        // normalisierte json Datei weiterleiten
        exchange.getIn().setBody(node.toString());
		
	}
	
	public void normalizeClearText(Exchange exchange) {
		System.out.println("normalizeClearText");
	}
}
