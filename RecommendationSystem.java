import java.util.*;

public class RecommendationSystem {

    // Sample user-item ratings
    static Map<String, Map<String, Integer>> data = new HashMap<>();

    public static void main(String[] args) {

        // Sample data
        addRating("User1", "ItemA", 5);
        addRating("User1", "ItemB", 3);

        addRating("User2", "ItemA", 4);
        addRating("User2", "ItemC", 5);

        addRating("User3", "ItemB", 4);
        addRating("User3", "ItemC", 5);

        // Recommend for User1
        recommend("User1");
    }

    static void addRating(String user, String item, int rating) {
        data.putIfAbsent(user, new HashMap<>());
        data.get(user).put(item, rating);
    }

    static void recommend(String targetUser) {
        Map<String, Integer> targetRatings = data.get(targetUser);

        Map<String, Double> scores = new HashMap<>();
        Map<String, Double> similaritySum = new HashMap<>();

        for (String otherUser : data.keySet()) {
            if (otherUser.equals(targetUser)) continue;

            double similarity = cosineSimilarity(targetRatings, data.get(otherUser));

            for (String item : data.get(otherUser).keySet()) {
                if (!targetRatings.containsKey(item)) {

                    scores.put(item,
                        scores.getOrDefault(item, 0.0) +
                        similarity * data.get(otherUser).get(item));

                    similaritySum.put(item,
                        similaritySum.getOrDefault(item, 0.0) + similarity);
                }
            }
        }

        System.out.println("Recommendations for " + targetUser + ":");

        for (String item : scores.keySet()) {
            double score = scores.get(item) / similaritySum.get(item);
            System.out.println(item + " -> Predicted Rating: " + score);
        }
    }

    // Cosine similarity
    static double cosineSimilarity(Map<String, Integer> u1, Map<String, Integer> u2) {
        double dot = 0, mag1 = 0, mag2 = 0;

        for (String item : u1.keySet()) {
            if (u2.containsKey(item)) {
                dot += u1.get(item) * u2.get(item);
            }
            mag1 += Math.pow(u1.get(item), 2);
        }

        for (int val : u2.values()) {
            mag2 += Math.pow(val, 2);
        }

        if (mag1 == 0 || mag2 == 0) return 0;

        return dot / (Math.sqrt(mag1) * Math.sqrt(mag2));
    }
}