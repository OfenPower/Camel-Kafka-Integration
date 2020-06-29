package loanbroker;

import org.apache.camel.Exchange;

public class Normalizer {
	public void normalizeJson(Exchange exchange) {
		System.out.println("normalizeJson");
	}
	
	public void normalizeXml(Exchange exchange) {
		System.out.println("normalizeXml");
	}
	
	public void normalizeClearText(Exchange exchange) {
		System.out.println("normalizeClearText");
	}
}
