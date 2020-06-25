package loanbroker;

import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class BankProcessor implements Processor {
	private final String bankName;
	private final double primeRate;

	public BankProcessor(String name) {
		bankName = name;
		primeRate = 3.5;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		String ssn = exchange.getIn().getHeader(Constants.PROPERTY_SSN, String.class);
		Integer historyLength = exchange.getIn().getHeader(Constants.PROPERTY_HISTORYLENGTH, Integer.class);
		Random rand = new Random();
		double rate = primeRate + (double) (historyLength / 12) / 10 + (rand.nextDouble() * 10) / 10;

		// set reply details as headers
		exchange.getOut().setHeader(Constants.PROPERTY_BANK, bankName);
		exchange.getOut().setHeader(Constants.PROPERTY_SSN, ssn);
		exchange.getOut().setHeader(Constants.PROPERTY_RATE, rate);
	}

}