package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PrintMessageProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
    	System.out.println("PrintMessageProcessor");
        System.out.println(exchange.getIn().getBody(String.class));
    }
}
