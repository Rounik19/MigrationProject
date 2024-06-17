package in.rounik.project;


public class PathMapping {
    private String providerJsonPath;
    private String sprinklrJsonPath;

    public PathMapping() {
    }

    public PathMapping(String providerJsonPath, String sprinklrJsonPath) {
        this.providerJsonPath = providerJsonPath;
        this.sprinklrJsonPath = sprinklrJsonPath;
    }

    public String getProviderJsonPath() {
        return providerJsonPath;
    }

    public void setProviderJsonPath(String providerJsonPath) {
        this.providerJsonPath = providerJsonPath;
    }

    public String getSprinklrJsonPath() {
        return sprinklrJsonPath;
    }

    public void setSprinklrJsonPath(String sprinklrJsonPath) {
        this.sprinklrJsonPath = sprinklrJsonPath;
    }

}
