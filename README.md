Event-Driven Architecture Example -Loan Broker- by David Siepen and Daniel Bach
-------------------------------------------------------------------------------
Developed with 
- Apache Kafka (Messaging System) and 
- Apache Camel (Integration Framework)

-------------------------------------------------------------------------------

Steps for launching the applications:

1. Start Kafka Zookeeper:
  .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

2. Start Kafka Server:
  .\bin\windows\kafka-server-start.bat .\config\server.properties

3. Create the following Kafka-Topics
.) loan-request
.) bank01
.) bank02
.) bank03
.) loan-response
.) broker-response

with this command:
  .\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic <name-of-the-topic>

4. Launch every main:
- ClearTextBankMain
- JsonBankMain
- XmlBankMain
- LoanBrokerMain
- ClientMain

5. Enter a Loan Request in the ClientMain GUI. There are currently no input-checks enabled, please only use integer-/double-values

6. Done. The Loan Broker will now contact the banks and check their offers. Finally the client will receive the best offer from the Broker.



