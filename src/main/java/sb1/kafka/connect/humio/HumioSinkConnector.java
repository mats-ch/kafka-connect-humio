package sb1.kafka.connect.humio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HumioSinkConnector extends SinkConnector {
  private static Logger log = LoggerFactory.getLogger(HumioSinkConnector.class);
  private Map<String, String> configProperties;

  @Override
  public void start(Map<String, String> settings) {
    try {
      configProperties = settings;
      new HumioConnectorConfig(settings);
    } catch (Exception e) {
      throw new ConnectException("Bad configuration for HumioConnectorConfig", e);
    }
  }

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    ArrayList<Map<String, String>> configs = new ArrayList<>();
    for (int i = 0; i < maxTasks; i++) {
      Map<String, String> config = new HashMap<>();
      config.putAll(configProperties);
      configs.add(config);
    }
    return configs;
  }

  @Override
  public void stop() {
    // do nothing.
  }

  @Override
  public ConfigDef config() {
    return HumioConnectorConfig.config();
  }

  @Override
  public Class<? extends Task> taskClass() {
    return HumioSinkTask.class;
  }

  @Override
  public String version() {
    return "0.1";
  }
}
