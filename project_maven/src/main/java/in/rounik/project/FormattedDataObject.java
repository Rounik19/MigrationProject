package in.rounik.project;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.JsonNode;

@Document(collection = "Configs")
public class FormattedDataObject {
    @Id
    private String id;
    private Date fetchTime;
    private Date lastModifiedTime;
    private JsonNode formattedData;

    public FormattedDataObject() {
    }

    public FormattedDataObject(String id, Date fetchTime, Date lastModifiedTime, JsonNode formattedData) {
        this.id = id;
        this.fetchTime = fetchTime;
        this.lastModifiedTime = lastModifiedTime;
        this.formattedData = formattedData;
    }

    public String getId() {
        return id;
    }

    public Date getFetchTime() {
        return fetchTime;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public JsonNode getFormattedData() {
        return formattedData;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFetchTime(Date fetchTime) {
        this.fetchTime = fetchTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public void setFormattedData(JsonNode fetchedDataJson) {
        this.formattedData = fetchedDataJson;
    }

}