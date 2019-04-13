package sb1.kafka.connect.humio;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;

import java.util.Map;


public class HumioConnectorConfig extends AbstractConfig {
  public static final String HUMIO_URL = "humio.url";
  public static final String HUMIO_URL_DOC = "Humio HEC endpoint url";
  public static final String HUMIO_TOKEN = "humio.token";
  public static final String HUMIO_TOKEN_DOC = "Humio ingest token";
  public static final String HUMIO_RETRY_ON_ERROR = "humio.retry.on.http.error";
  public static final String HUMIO_RETRY_ON_ERROR_DOC = "Retry request if server returns an error";

  public HumioConnectorConfig(ConfigDef config, Map<String, String> originals) {
    super(config, originals, true);
  }

  public HumioConnectorConfig(Map<String, String> parsedConfig) {
    this(config(), parsedConfig);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(HUMIO_URL, Type.STRING, Importance.HIGH, HUMIO_URL_DOC)
        .define(HUMIO_TOKEN, Type.STRING, Importance.HIGH, HUMIO_TOKEN_DOC)
        .define(HUMIO_RETRY_ON_ERROR, Type.BOOLEAN, false, Importance.HIGH, HUMIO_RETRY_ON_ERROR_DOC);
  }

  public String getHumioUrl() {
    return this.getString(HUMIO_URL);
  }

  public String getHumioToken() {
    return this.getString(HUMIO_TOKEN);
  }

  public Boolean getHumioRetryOnError() {
    return this.getBoolean(HUMIO_RETRY_ON_ERROR);
  }
}
