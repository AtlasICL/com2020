package wizardquest.telemetry;

import java.time.Instant;

import wizardquest.gamemanager.EncounterEnum;
import wizardquest.settings.DifficultyEnum;

public abstract class EncounterEvent extends SessionEvent {
    private final int stage_number;
    private final EncounterEnum encounter_name;
    private final DifficultyEnum difficulty;

    /**
     * Constructor for the encounter telemetry event. Produces a telemetry event storing
     * common data.
     *
     * @param userID        the ID of the user who is playing the game when the event is
     *                      constructed.
     * @param sessionID     the ID of the session the user is currently playing. See
     *                      TelemetryListenerInterface for information about sessions.
     * @param timeStamp     the time the event was constructed.
     * @param telemetryName name of the type of encounter event.
     * @param encounterName the name of the encounter a player is fighting.
     * @param stageNumber   the current stage player is attempting.
     * @param difficulty    the difficulty used for the player's session.
     */
    public EncounterEvent(String userID, int sessionID, Instant timeStamp, String telemetryName, EncounterEnum encounterName,
            DifficultyEnum difficulty, int stageNumber) {
        super(userID, sessionID, timeStamp, telemetryName);
        this.encounter_name = encounterName;
        this.difficulty = difficulty;
        this.stage_number = stageNumber;
    }

    /**
     * Gets the name for the encounter this event was generated on.
     * @return the encounter's name.
     */
    public EncounterEnum getEncounter_name(){
        return this.encounter_name;
    }

    /**
     * Gets the stage number the player is on when this event was created.
     * @return the stage number stored in the event.
     */
    public int getStage_number() {
        return this.stage_number;
    }

    /**
     * Gets the difficulty setting for the run that generated this event.
     * @return the difficulty setting.
     */
    public DifficultyEnum getDifficulty(){
        return this.difficulty;
    }
}
