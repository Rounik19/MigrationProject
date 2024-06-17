package in.rounik.project;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "external.api")
@Getter
@Setter
@Document(collection = "Configs")
public class ApiConfigMappingConfig {
    @Id
    private String id;
    private FolderFetchConfig folderFetchConfig;
    private ArticleFetchConfig articleFetchConfig;
    private TranslationFetchConfig translationFetchConfig;
}