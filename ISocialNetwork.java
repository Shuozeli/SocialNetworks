import java.util.List;
import java.time.Instant;
import java.util.Map;

public interface ISocialNetwork {
    /**
     * Create a graph representation of the dataset.
     * @param filePath
     * @return
     */
    public int loadGraphFromDataSet(String filePath);

    /**
     * Returns the shortest unweighted path between two users
     * @param user1
     * @param user2
     * @return
     */
    public int getShortestPathUnweighted(int user1, int user2);

    /**
     *  Returns a sort list of recommended users based on distance from
     *  getShortestPathWeighted(). For example, return all recommendations < 3
     *  connections away
     * @param distance
     * @param userId
     * @return
     */
    public List<Integer> recommendationByDistance(int distance, int userId);

    /**
     * read user's interest from the dataset (fake data)
     * @param filepath
     * @return
     */
    public Map<Integer, List<Integer>> loadUserInterests(String filepath);

    /**
     * map from interest ID to a list of users with this specific interest
     * @param interestMap
     * @return
     */
    public Map<Integer, List<Integer>> clusterUserByInterest(Map<Integer, List<Integer>> interestMap);


    /**
     * Returns a list of users in a specific interest cluster
     * @return
     */
    public List<Integer> getUsersInterestCluster(int interestID);

    /**
     * Recommend users in a speicfic interest cluster. Sort by distance
     * @param interestId
     * @param userId
     * @return
     */
    public List<Integer> recommendationByInterest(int interestId, int userId);

    /**
     *
     * @param filepath
     * @return
     */
    public Map<Integer, List<LikedPost>> loadPosts(String filepath);

    /**
     * map from InterestId to a list of users
     * @param interestMap
     * @return
     */
    public Map<Integer, List<LikedPost>> clusterUserByPost(Map<Integer, List<LikedPost>> posts);

    /**
     * posts that my friends recently liked
     * @param timeFrame
     * @param userId
     * @return
     */
    public List<Integer> recommendPost(int userId, Instant earliest,
                                        Map<Integer, List<LikedPost>> likedPosts);
}
