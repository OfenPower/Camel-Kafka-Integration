package loanbroker;

import java.io.Serializable;

public class LoanRequestMessage implements Serializable {

	private static final long serialVersionUID = -291855144101861552L;

	private double creditRequest;
	private double currentCapital;
	private double monthlyIncome;

	public LoanRequestMessage(double cr, double cc, double mi) {
		creditRequest = cr;
		currentCapital = cc;
		monthlyIncome = mi;
	}

	public double getCreditRequest() {
		return creditRequest;
	}

	public double getCurrentCapital() {
		return currentCapital;
	}

	public double getMonthlyIncome() {
		return monthlyIncome;
	}

}
