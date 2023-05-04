import java.util.List;

public interface ISocialNetwork {
  int loadGraphFromDataSet(String filePath);

  // 1. based on connected users
  // 2. based on distance
  // 3. based on liked posts

  // Returns the shortest unweighted path between two users
  int getShortestPathUnweighted(User user1, User user2);
  // BFS

  // Returns a sort list of recommended users based on distance from getShortestPathWeighted().
  // For example, return all recommendations < 3 connections away
  List<Integer> recommendationByDistance(int distance, int userId);
}
