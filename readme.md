# Kafka Connect plugin for Humio HEC

Plugin for kafka connect that reads from kafka topics and sends data to the Humio HEC endpoint.
Will add the raw message and add topic as an extra field and "Kafka Connect" as source.

## Building

mvn clean compile assembly:single

## Usage

Supports the configuration options "humio.url" and "humio.token".

Example:

	{
	  "name": "humio-sink",
	  "config" : {
	    "connector.class": "sb1.kafka.connect.humio.HumioSinkConnector",
	    "tasks.max": 1,
	    "topics": "test",
	    "errors.log.enable": true,
	    "humio.url": "http://127.0.0.1:8080/api/v1/ingest/hec",
	    "humio.token": "hx5jfCXkcFq7VZzvNZwXG4pmnMkhbwl2OazkmBlEfsIe"
	  }
	}

## Tuning

The plugin will batch all polled messages as a single HEC request.
For larger or smaller batches, set the "consumer.max.poll.records", "consumer.max.poll.interval.ms" or "consumer.fetch.max.bytes" configuration options in Kafka Connect.
