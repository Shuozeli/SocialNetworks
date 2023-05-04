import java.util.List;
import java.util.Map;

public class SocialNetworks implements ISocialNetwork {
    /**
     * Create a graph representation of the dataset.
     *
     * @param filePath
     * @return
     */
    @Override
    public int loadGraphFromDataSet(String filePath) {
        return 0;
    }

    /**
     * Returns the shortest unweighted path between two users
     *
     * @param user1
     * @param user2
     * @return
     */
    @Override
    public int getShortestPathUnweighted(User user1, User user2) {
        return 0;
    }

    /**
     * Returns a sort list of recommended users based on distance from
     * getShortestPathWeighted(). For example, return all recommendations < 3
     * connections away
     *
     * @param distance
     * @param userId
     * @return
     */
    @Override
    public List<Integer> recommendationByDistance(int distance, int userId) {
        return null;
    }

    /**
     * read user's interest from the dataset (fake data)
     *
     * @param filepath
     * @return
     */
    @Override
    public Map<Integer, List<Integer>> loadUserInterests(String filepath) {
        return null;
    }

    /**
     * map from interest ID to a list of users with this specific interest
     *
     * @param interestMap
     * @return
     */
    @Override
    public Map<Integer, List<Integer>> clusterUserByInterest(Map<Integer, List<Integer>> interestMap) {
        return null;
    }

    /**
     * Returns a list of users in a specific interest cluster
     *
     * @param interestID
     * @return
     */
    @Override
    public List<Integer> getUsersInterestCluster(int interestID) {
        return null;
    }

    /**
     * Recommend users in a speicfic interest cluster. Sort by distance
     *
     * @param interestId
     * @param userId
     * @return
     */
    @Override
    public List<Integer> recommendationByInterest(int interestId, int userId) {
        return null;
    }

    /**
     * @param filepath
     * @return
     */
    @Override
    public HashMap<Integer, List<Post>> loadPosts(String filepath) {
        return null;
    }

    /**
     * map from InterestId to a list of users
     *
     * @param interestMap
     * @return
     */
    @Override
    public Map<Integer, List<Integer>> clusterUserByPost(Map<Integer, List<Integer>> interestMap) {
        return null;
    }

    /**
     * posts that my friends recently liked
     *
     * @param timeFrame
     * @param userId
     * @return
     */
    @Override
    public List<Integer> recommendPost(int timeFrame, int userId) {
        return null;
    }
}
