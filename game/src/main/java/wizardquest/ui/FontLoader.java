package wizardquest.ui;

import java.io.File;

import javafx.scene.text.Font;

public final class FontLoader {

    private FontLoader() {
    }

    public static void loadFonts() {
        try {
            Font bold = Font.loadFont(
                    new File("src/main/java/wizardquest/resources/JetBrainsMono-Bold.ttf")
                            .toURI().toString(),
                    14
            );

            Font medium = Font.loadFont(
                    new File("src/main/java/wizardquest/resources/JetBrainsMono-Medium.ttf")
                            .toURI().toString(),
                    14
            );

            System.out.println("Bold font: " + bold);
            System.out.println("Medium font: " + medium);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}