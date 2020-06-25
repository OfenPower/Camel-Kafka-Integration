package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class LoanRequestToStringProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		System.out.println("LoanRequestToStringProcessor.Process()");

		// LoanRequestMessage Objekt auslesen
		LoanRequestMessage body = exchange.getIn().getBody(LoanRequestMessage.class);

		System.out.println("Received Credit Request: " + body.getCreditRequest());
		System.out.println("Received Current Capital: " + body.getCurrentCapital());
		System.out.println("Received Monthly Income: " + body.getMonthlyIncome());

		// Message so verändern, dass die Message alle LoanRequest Werte zeilenweise als
		// String enthält
		exchange.getIn()
				.setBody("Received Credit Request: " + body.getCreditRequest() + "\n" + "Received Current Capital: "
						+ body.getCurrentCapital() + "\n" + "Received Monthly Income: " + body.getMonthlyIncome()
						+ "\n");

	}

}
