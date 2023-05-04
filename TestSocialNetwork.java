import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestSocialNetwork {
  @Test
  public void testLoadGraphFromDataSet() {
    SocialNetwork socialNetwork = new SocialNetwork(false);
    int nodeCount = socialNetwork.loadGraphFromDataSet("src/socfb-American75.mtx");
    assertEquals(6386, nodeCount);
  }

  @Test
  public void testGetShortestPathUnweighted() {
    SocialNetwork socialNetwork = new SocialNetwork(true);
    User user1 = new User(123);
    User user2 = new User(456);
    int shortestPathUnweighted = socialNetwork.getShortestPathUnweighted(user1, user2);
    assertEquals(8, shortestPathUnweighted);
  }

  @Test
  public void testRecommendationByDistance() {
    SocialNetwork socialNetwork = new SocialNetwork(true);
    List<Integer> recommendation = socialNetwork.recommendationByDistance(3, 1234);
    assertEquals(438, recommendation.size());
  }
}
