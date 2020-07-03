package loanbroker;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class LoanBrokerMain {

	// Startet den Loan Broker, welcher den CreditRequest des Clients verarbeitet und an die
	// Banken weiterleitet
	public static void main(String[] args) {
		
		// Route vom Client zum LoanBroker aufbauen
		LoanBrokerRoute loanBrokerRoute = new LoanBrokerRoute();
		
		// Dynamische Routen zu den Banken aufbauen
		BrokerBankRoute brokerBankRoute = new BrokerBankRoute();
		
		// Rückroute vom LoanBroker zum Client aufbauen, welche das aggregierte Angebot enthält
		LoanBrokerResponseRoute brokerLoanResponseRoute = new LoanBrokerResponseRoute();
		
		CamelContext ctx = new DefaultCamelContext();
		ctx.setTracing(true);	// Debug
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