package my.wf.samlib.model.dto;

public class VersionInfoDto {
    private String versionNumber;
    private int buildNumber;
    private String buildDate;

    public VersionInfoDto(String versionNumber, int buildNumber, String buildDate) {
        this.versionNumber = versionNumber;
        this.buildNumber = buildNumber;
        this.buildDate = buildDate;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public String getBuildDate() {
        return buildDate;
    }
}
