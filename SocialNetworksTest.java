import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SocialNetworksTest {

    @Test
    public void loadGraphFromDataSet() {
        SocialNetworks socialNetwork = new SocialNetworks(false);
        int nodeCount = socialNetwork.loadGraphFromDataSet("socfb-American75.mtx");
        assertEquals(6386, nodeCount);
    }

    @Test
    public void getShortestPathUnweightedTest() {
        SocialNetworks socialNetwork = new SocialNetworks(true);
        int shortestPathUnweighted = socialNetwork.getShortestPathUnweighted(123, 456);
        assertEquals(8, shortestPathUnweighted);

    }

    @Test
    public void recommendationByDistance() {
        SocialNetworks socialNetwork = new SocialNetworks(true);
        List<Integer> recommendation = socialNetwork.recommendationByDistance(3, 1234);
        assertEquals(438, recommendation.size());
    }

    @Test
    public void loadUserInterestsTest() {
        SocialNetworks socialNetwork = new SocialNetworks(true);
        Map<Integer, List<Integer>> interestsByUser = socialNetwork.loadUserInterests("interests.txt");
        assertEquals(6386, interestsByUser.size());


    }

    @Test
    public void clusterUserByInterest() {
        SocialNetworks socialNetwork = new SocialNetworks(true);
        Map<Integer, List<Integer>> interestsByUser = socialNetwork.loadUserInterests("interests.txt");
        Map<Integer, List<Integer>> clusteredUsers = socialNetwork.clusterUserByInterest(interestsByUser);
        assertEquals(20, clusteredUsers.size());

    }

    @Test
    public void getUsersInterestCluster() {
        SocialNetworks socialNetwork = new SocialNetworks(true);
        Map<Integer, List<Integer>> interestsByUser = socialNetwork.loadUserInterests("interests.txt");
        Map<Integer, List<Integer>> clusteredUsers = socialNetwork.clusterUserByInterest(interestsByUser);
        assertEquals(630, socialNetwork.getUsersInterestCluster(0, clusteredUsers).size());
        assertEquals(652, socialNetwork.getUsersInterestCluster(2, clusteredUsers).size());
        assertEquals(616, socialNetwork.getUsersInterestCluster(3, clusteredUsers).size());
        assertEquals(616, socialNetwork.getUsersInterestCluster(19, clusteredUsers).size());

    }

    @Test
    public void recommendationByInterest() {
        SocialNetworks socialNetwork = new SocialNetworks(true);
        Map<Integer, List<Integer>> interestsByUser = socialNetwork.loadUserInterests("interests.txt");
        List<Integer> recommendToUser1 = socialNetwork.recommendationByInterest(3, 123, interestsByUser);
        assertEquals(4, recommendToUser1.size());
        List<Integer> expectedList2 = Arrays.asList(1, 9, 15, 2);
        assertEquals(expectedList2, recommendToUser1);


    }

    @Test
    public void loadPosts() {
    }

    @Test
    public void clusterUserByPost() {
    }

    @Test
    public void recommendPost() {
    }
}