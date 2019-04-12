package sb1.kafka.connect.humio;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.apache.kafka.connect.errors.ConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONObject;
import com.google.common.collect.Lists; 

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class HumioSinkTask extends SinkTask {
  private static Logger log = LoggerFactory.getLogger(HumioSinkTask.class);

  HumioConnectorConfig config;
  URL url;

  @Override
  public void start(Map<String, String> settings) {
    log.info("Starting Humio sink!");
    this.config = new HumioConnectorConfig(settings);
    try {
      this.url = new URL(this.config.getHumioUrl());
    } catch (MalformedURLException ex) {
        log.error("Url error: {}", ex);
    }
  }

  @Override
  public void put(Collection<SinkRecord> records) {
    List recordsArray = new ArrayList();
    for (SinkRecord sr : records) {
      JSONObject event = new JSONObject();
      JSONObject fields = new JSONObject();
      event.put("event", sr.value()); 
      event.put("source", "Kafka Connect");
      fields.put("topic", sr.topic());
      event.put("fields", fields);
      recordsArray.add(event.toString());
    }
    if (recordsArray.size() > 0 ) {
      try {
        HttpURLConnection r = (HttpURLConnection) this.url.openConnection();
        r.setRequestMethod("POST");
        r.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
        r.setRequestProperty("Authorization", "Bearer: " + this.config.getHumioToken());
        r.setDoOutput(true);
        r.setDoInput(true);
        OutputStreamWriter osw = new OutputStreamWriter(r.getOutputStream());
        osw.write(String.join("\n", recordsArray));
        osw.flush();
        if (r.getResponseCode() > 299) {
          log.error("Error from Humio. status={}", r.getResponseCode());
          throw new ConnectException("Error from Humio.");
        }
      } catch (IOException ex) {
          throw new ConnectException("POST request error: {}", ex);
      }
    }
  }

  @Override
  public void flush(Map<TopicPartition, OffsetAndMetadata> map) {

  }

  @Override
  public void stop() {

  }

  @Override
  public String version() {
    return "1.0";
  }
}
