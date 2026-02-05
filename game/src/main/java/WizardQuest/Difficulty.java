package WizardQuest;

public enum Difficulty {
    EASY("Easy"),
    NORMAL("Normal"), 
    HARD("Hard");

    private final String telemetryName;

    private Difficulty(String telemetryName){
        this.telemetryName = telemetryName;
    }

    public String getTelemetryName(){
        return this.telemetryName;
    }
}
