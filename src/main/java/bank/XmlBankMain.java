package bank;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import bank.XmlBankMain.CreditRequest;
import bank.XmlBankMain.CreditResponse;

public class XmlBankMain {
	
	// Request Xml Klasse
	public static class CreditRequest {
		public double creditRequest;
		public double currentCapital;
		public double monthlyIncome;
		public int creditScore;
		public int correlationId;
		
	}

	// Antwort Xml Klasse
	public static class CreditResponse {
		public double monthlyPremiums;
		public int durationInMonths;
		public double grantedCredit;
		public double interestRatePerMonth;
		public int correlationId;
	}

	/*
	 * Startet die Json-Bank, welche auf dem Kafka-Topic "bank02" lauscht und für
	 * einen CreditRequest ein Angebot zurücksendet 
	 */
	public static void main(String[] args) {
		
		// Route zwischen LoanBroker und Bank
		XmlBankRoute xmlBankRoute = new XmlBankRoute();

		// CamelContext starten
		CamelContext ctx = new DefaultCamelContext();
		try {
			ctx.addRoutes(xmlBankRoute);
			ctx.start();
			System.in.read();
			System.out.println("Program End");
			ctx.stop();
			ctx.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

/*
 * Processor, welcher für einen CreditRequest ein Angebot berechnet
 */
class XmlBankRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		// Messages vom Topic "bank02" lesen
		from("kafka:bank02?brokers=localhost:9092&groupId=xmlBank")
		// Angebot berechnen
		.process(new XmlBankProcessor())
		// Angebot an Topic "loan-response" zurücksenden
		.to("kafka:loan-response?brokers=localhost:9092");
	}
	
}

/*
 * Processor, welcher für einen CreditRequest ein Angebot berechnet
 */
class XmlBankProcessor implements Processor {
	
    static int creditDuration = 10*12; // 10 Jahre Kreditlaufzeit
    static double interestRate = 0.05; // 5% Zinssatz (semi-gut)
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		// XML Message holen und in Objekt parsen
		String xmlString = exchange.getIn().getBody(String.class);
		System.out.println("Received the following message: " + xmlString);
		
		XmlMapper xmlMapper = new XmlMapper();
		CreditRequest xmlLoanRequest = xmlMapper.readValue(xmlString, CreditRequest.class);
		
		// XML Response erzeugen und Response berechnen
		CreditResponse xmlResponse = new CreditResponse();
		xmlResponse.durationInMonths = creditDuration;
		xmlResponse.grantedCredit = xmlLoanRequest.creditRequest;
		xmlResponse.interestRatePerMonth = interestRate;
		xmlResponse.monthlyPremiums = xmlLoanRequest.creditRequest / xmlResponse.durationInMonths;
		xmlResponse.monthlyPremiums += xmlResponse.monthlyPremiums * xmlResponse.interestRatePerMonth;
		xmlResponse.correlationId = xmlLoanRequest.correlationId;
		
		// XML Antwortobjekt zum String parsen und als Antwortmessage weiterleiten
		String xmlResponseString = xmlMapper.writeValueAsString(xmlResponse);
		System.out.println("Send the following response: " + xmlResponseString);
		
		// Angebotsmessage versenden
		exchange.getIn().setHeader("type", "xml");
		exchange.getIn().setBody(xmlResponseString);
	}
}



