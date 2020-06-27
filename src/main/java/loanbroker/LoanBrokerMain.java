package loanbroker;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class LoanBrokerMain {

	public static void main(String[] args) {
		
		// Startet den Broker, welcher den CreditRequest des Clients verarbeitet und an die
		// Banken weiterleitet
		LoanBrokerRoute loanBrokerRoute = new LoanBrokerRoute();

		CamelContext ctx = new DefaultCamelContext();
		try {
			ctx.addRoutes(loanBrokerRoute);
			ctx.start();
			System.in.read();
			System.out.println("Program End");
			ctx.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}