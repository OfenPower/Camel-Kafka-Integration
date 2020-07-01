package loanbroker;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class LoanBrokerMain {

	public static void main(String[] args) {
		
		// Startet den Broker, welcher den CreditRequest des Clients verarbeitet und an die
		// Banken weiterleitet
		LoanBrokerRoute loanBrokerRoute = new LoanBrokerRoute();
		BrokerBankRoute brokerBankRoute = new BrokerBankRoute();
		LoanBrokerResponseRoute brokerLoanResponseRoute = new LoanBrokerResponseRoute();
		
		CamelContext ctx = new DefaultCamelContext();
		ctx.setTracing(true);
		try {
			ctx.addRoutes(loanBrokerRoute);
			ctx.addRoutes(brokerBankRoute);
			ctx.addRoutes(brokerLoanResponseRoute);
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