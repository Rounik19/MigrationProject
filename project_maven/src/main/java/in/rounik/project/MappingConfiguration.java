package in.rounik.project;

import java.util.List;
import org.springframework.data.annotation.Id;

public class MappingConfiguration {
    @Id
    private String id;
    private List<PathMapping> fieldMappings;

    public MappingConfiguration() {
    }

    public MappingConfiguration(List<PathMapping> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

    public String getId() {
        return id;
    }

    public List<PathMapping> getFieldMappings() {
        return fieldMappings;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFieldMappings(List<PathMapping> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

}
