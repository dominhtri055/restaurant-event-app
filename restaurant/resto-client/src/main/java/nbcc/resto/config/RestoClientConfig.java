package nbcc.resto.config;

public interface RestoClientConfig {
    String getBaseUrl();
    String getPort();

    default String getBaseAddress() {
        return getBaseUrl() + ":" + getPort();
    }
}
