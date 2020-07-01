package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class CorrelationIdPrintProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("CorrelationIdPrintProcessor:");
		System.out.println(exchange.getIn().getBody(String.class));
		//System.out.println(exchange.getIn().getHeader(Exchange.CORRELATION_ID));
		
//		// Correlation Id zurück zum int konvertieren
//		ByteArrayOutputStream bs = new ByteArrayOutputStream();
//		ObjectOutputStream os = new ObjectOutputStream(bs);
//		os.writeObject(exchange.getIn().getHeader("corrId"));
//		os.flush();
//		byte[] bytes = bs.toByteArray();
//		ByteBuffer bb = ByteBuffer.wrap(bytes);
//		int value = bb.getInt();
//		
//		System.out.println(value);

		//System.out.println(exchange.getIn().getHeader(KafkaConstants.KEY));
		//System.out.println(exchange.getIn().getHeader("corrId"));
		//System.out.println(exchange.getIn().getHeader("type"));
		
	}
	

}
