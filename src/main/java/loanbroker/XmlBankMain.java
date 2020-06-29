package loanbroker;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlBankMain {

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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

class XmlBankRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		// übersetzte Message aus Kafka lesen und verarbeiten
		from("kafka:bank02?brokers=localhost:9092&groupId=groupA")
			.process(new XmlBankProcessor());
		//.to("kafka:loan-response?brokers=localhost:9092");
	}
	
}

class XmlBankProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		String xmlString = exchange.getIn().getBody(String.class);
		
		System.out.println(xmlString);
		
		XmlMapper xmlMapper = new XmlMapper();
		CreditRequest xmlLoanRequest = xmlMapper.readValue(xmlString, CreditRequest.class);
		
		
		System.out.println("XML BANK VALUES");
		System.out.println(xmlLoanRequest.creditRequest);
		System.out.println(xmlLoanRequest.currentCapital);
		System.out.println(xmlLoanRequest.monthlyIncome);
		System.out.println(xmlLoanRequest.creditScore);
		System.out.println(xmlLoanRequest.monthlyPremiums);
		System.out.println(xmlLoanRequest.durationInMonths);
		System.out.println(xmlLoanRequest.grantedCredit);
		System.out.println(xmlLoanRequest.interestRatePerMonth);
		
		
		
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
class CreditRequest {
	public double creditRequest;
	public double currentCapital;
	public double monthlyIncome;
	public int creditScore;
	
	public double monthlyPremiums;
	public int durationInMonths;
	public double grantedCredit;
	public double interestRatePerMonth;
}

