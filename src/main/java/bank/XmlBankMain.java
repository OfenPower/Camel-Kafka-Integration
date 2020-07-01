package bank;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

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
		
	}

	// Antwort Xml Klasse
	public static class CreditResponse {
		public double monthlyPremiums;
		public int durationInMonths;
		public double grantedCredit;
		public double interestRatePerMonth;
	}


	public static void main(String[] args) {
		
		// Startet den Broker, welcher den CreditRequest des Clients verarbeitet und an die
		// Banken weiterleitet
		XmlBankRoute xmlBankRoute = new XmlBankRoute();

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

class XmlBankRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		// übersetzte Message aus Kafka lesen und verarbeiten
		from("kafka:bank02?brokers=localhost:9092&groupId=xmlBank")
			.process(new XmlBankProcessor())
		.to("kafka:loan-response?brokers=localhost:9092");
	}
	
}

class XmlBankProcessor implements Processor {
	
    static int creditDuration = 10*12; // 10 years
    static double interestRate = 0.05; // 5%
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		// XML Message holen und in Objekt parsen
		String xmlString = exchange.getIn().getBody(String.class);
		XmlMapper xmlMapper = new XmlMapper();
		CreditRequest xmlLoanRequest = xmlMapper.readValue(xmlString, CreditRequest.class);
		
		// XML Response erzeugen und Response berechnen
		CreditResponse xmlResponse = new CreditResponse();
		xmlResponse.durationInMonths = creditDuration;
		xmlResponse.grantedCredit = xmlLoanRequest.creditRequest;
		xmlResponse.interestRatePerMonth = interestRate;
		xmlResponse.monthlyPremiums = xmlLoanRequest.creditRequest / xmlResponse.durationInMonths;
		xmlResponse.monthlyPremiums += xmlResponse.monthlyPremiums * xmlResponse.interestRatePerMonth;
		
		// XML Antwortobjekt zum String parsen und als Antwortmessage weiterleiten
		String xmlResponseString = xmlMapper.writeValueAsString(new CreditResponse());
		System.out.println("Send Response: " + xmlResponseString);
		exchange.getIn().setBody(xmlResponseString);
		exchange.getIn().setHeader("type", "xml");
		
		// Correlation Key fürs Aggregate
		int corrId = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    DataOutputStream dos = new DataOutputStream(bos);
	    dos.writeInt(corrId);
	    dos.flush();
	    byte[] bytes = bos.toByteArray();
	    exchange.getIn().setHeader("corrId", bytes);
		
		
		
	}
}

// Klasse in die der Xml String deserialisiert wird
/*
<CreditRequest>
	<creditRequest>23623.0</creditRequest>
	<currentCapital>2.62346423E8</currentCapital>
	<monthlyIncome>236234.0</monthlyIncome>
	<creditScore>10</creditScore>
</CreditRequest>
*/

