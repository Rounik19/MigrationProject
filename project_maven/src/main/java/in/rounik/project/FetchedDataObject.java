package in.rounik.project;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "FetchedData")
public class FetchedDataObject {
    @Id
    private String id;
    private Date fetchTime;
    private Date lastModifiedTime;
    private String fetchedData;

    public FetchedDataObject() {
    }

    public FetchedDataObject(String id, Date fetchTime, Date lastModifiedTime, String fetchedData) {
        this.id = id;
        this.fetchTime = fetchTime;
        this.lastModifiedTime = lastModifiedTime;
        this.fetchedData = fetchedData;
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

    public String getFetchedData() {
        return fetchedData;
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

    public void setFetchedData(String finalData) {
        this.fetchedData = finalData;
    }
}