import java.util.ArrayList;

public class ExampleEventsGenerator {
    
    private ArrayList<String> userIDs;
    private ArrayList<Integer> sessionIDs;

    private ExampleEventsGenerator() {
        userIDs = new ArrayList<>();
        sessionIDs = new ArrayList<>();
    }

    public static void main(String[] args) {
        ExampleEventsGenerator generator = new ExampleEventsGenerator();

        int totalUsers = 40;
        generator.generateUserIDs(totalUsers);
        generator.generateSessionIDs(totalUsers * 2);
    }

    private void generateUserIDs(int total) {
        for (int i = 0; i < total; i++) {
            String userIDString = Integer.toString(100000 + i);
            userIDs.add(userIDString);
        }
    }

    private void generateSessionIDs(int total) {
        for (int i = 0; i < total; i++) {
            sessionIDs.add(i + 100000);
        }
    }

    
}
