package loanbroker;

import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

/*
 * Deserializer für eine LoanRequestMessage.
 * Mit dieser Klasse deserialisiert Kafka ein LoanRequestMessage Objekt aus einem byte[] Array
 */
public class LoanRequestMessageDeserializer implements Deserializer<LoanRequestMessage> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub

	}

	@Override
	public LoanRequestMessage deserialize(String topic, byte[] bytes) {
		LoanRequestMessage data = SerializationUtils.deserialize(bytes);
		return data;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
