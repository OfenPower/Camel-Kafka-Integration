package loanbroker;

import org.apache.camel.component.kafka.serde.KafkaHeaderDeserializer;

public class LoanRequestHeaderDeserializer implements KafkaHeaderDeserializer {

	@Override
	public Object deserialize(String key, byte[] value) {
		// TODO Auto-generated method stub
		return null;
	}

}
