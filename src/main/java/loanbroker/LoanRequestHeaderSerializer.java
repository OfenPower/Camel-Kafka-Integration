package loanbroker;

import org.apache.camel.component.kafka.serde.KafkaHeaderSerializer;
import org.apache.commons.lang3.SerializationUtils;

public class LoanRequestHeaderSerializer implements KafkaHeaderSerializer {

	@Override
	public byte[] serialize(String key, Object value) {

		byte[] dataInBytes = SerializationUtils.serialize((int) value);
		return dataInBytes;
	}

}
