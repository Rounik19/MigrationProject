package in.rounik.project;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ApiFetchConfig {
    private String Hostname;
    private String Path;
    private String Params;
    private String Headers;
    private HttpMethod Method;
    private Map<String, String> QueryParams;
    private Map<String, String> RequestHeaders;
    private String RequestBody;
    private AuthType AuthType;
    private String AuthToken;
    private int MaxRetries;
    private int RetryDelay;

    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    public enum AuthType {
        NONE, BASIC, BEARER
    }
}
