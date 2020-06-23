package Message;

import java.io.Serializable;

public class LoanRequestMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -291855144101861552L;
	private double creditRequest;
	private double currentCapital;
	private double monthlyIncome;

	public LoanRequestMessage(double cr, double cc, double mi) {
		creditRequest = cr;
		currentCapital = cc;
		monthlyIncome = mi;
	}

	public double getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(double monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public double getCurrentCapital() {
		return currentCapital;
	}

	public void setCurrentCapital(double currentCapital) {
		this.currentCapital = currentCapital;
	}

	public double getCreditRequest() {
		return creditRequest;
	}

	public void setCreditRequest(double creditRequest) {
		this.creditRequest = creditRequest;
	}

}
