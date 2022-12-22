package net.game.Entity.Components;

public class CheckVersion {
    private String version;
    private String currentVersion;
    CheckVersion(String version, String currentVersion){
        this.version=version;
        this.currentVersion=currentVersion;

    }
    void check() throws Exception {
        if(!version.equals(currentVersion)) throw new Exception("Error - Incorrect version.");
    }
}
