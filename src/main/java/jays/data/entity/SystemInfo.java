package jays.data.entity;

public class SystemInfo {
    private String systemVersion;
    private String systemName;
    private String theme;

    public SystemInfo(String systemVersion, String systemName, String theme) {
        this.systemVersion = systemVersion;
        this.systemName = systemName;
        this.theme = theme;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getTheme() {
        return theme;
    }


}
