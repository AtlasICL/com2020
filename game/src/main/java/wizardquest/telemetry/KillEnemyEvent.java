package wizardquest.telemetry;

import java.time.Instant;

import wizardquest.entity.EntityEnum;
import wizardquest.gamemanager.EncounterEnum;
import wizardquest.settings.DifficultyEnum;

public class KillEnemyEvent extends EncounterEvent {
    private final EntityEnum enemy_type;

    /**
     * Constructor for KillEnemyEvent.
     *
     * @param userID        the ID of the user who is playing the game when the
     *                      event is
     *                      constructed.
     * @param sessionID     the ID of the session the user is currently playing. See
     *                      TelemetryListenerInterface for information about
     *                      sessions.
     * @param timeStamp     the time the event was constructed.
     * @param encounterName the name of the encounter a player is fighting.
     * @param difficulty    the difficulty used for the player's session.
     * @param stageNumber   the stage player has completed.
     * @param enemyType     type of enemy encounter.
     */
    public KillEnemyEvent(String userID, int sessionID,
            Instant timeStamp, EncounterEnum encounterName, DifficultyEnum difficulty, int stageNumber,
            EntityEnum enemyType) {
        super(userID, sessionID, timeStamp, "KillEnemy", encounterName, difficulty, stageNumber);
        this.enemy_type = enemyType;
    }

    /**
     * Gets stored enemy type killed.
     *
     * @return enemy type killed.
     */
    public EntityEnum getEnemy_type() {
        return this.enemy_type;
    }
}
