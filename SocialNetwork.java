import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class SocialNetwork implements ISocialNetwork {
  private Graph graph;
  private int nNodes;

  public SocialNetwork(boolean load) {
    if(load) {
      loadGraphFromDataSet("src/socfb-American75.mtx");
    }
  }

  /**
   * Create a graph representation of the dataset. The first line of the file
   * contains the number of nodes. Keep in mind that the vertex with id 0 is
   * not actually considered present in your final graph!
   *
   * @param filePath the path of the data
   * @return the number of entries (nodes) in the dataset (graph)
   */
  @Override
  public int loadGraphFromDataSet(String filePath) {
    // Create a File object with the given file path
    File file = new File(filePath);

    // Initialize a Scanner to read the file
    Scanner scanner = null;
    try {
      scanner = new Scanner(file);
    } catch (FileNotFoundException e) {
      // If the file is not found, return -1 indicating an error
      return -1;
    }

    // Read the number of nodes and edges from the file
    int numNodes = scanner.nextInt() + 1;
    int numEdges = scanner.nextInt();

    // Create a new instance of the GraphL class
    this.graph = new GraphL();
    this.graph.init(numNodes);

    // Iterate over the edges and add them to the graph
    for (int i = 0; i < numEdges; ++i) {
      int from = scanner.nextInt();
      int to = scanner.nextInt();

      // Skip adding the edge if the destination is 0
      if (to == 0) {
        continue;
      }

      // Read the weight of the edge and convert it to an integer
      double weight = scanner.nextDouble();

      // Add edges in both directions with the weight (multiplied by 100)
      this.graph.addEdge(from, to, (int) (weight * 100));
      this.graph.addEdge(to, from, (int) (weight * 100));
    }

    // Close the scanner
    scanner.close();

    // Count the number of nodes in the graph by checking the non-empty neighbors
    this.nNodes = 0;
    for (int i = 1; i < graph.nodeCount(); ++i) {
      if (graph.neighbors(i).length > 0) {
        this.nNodes++;
      }
    }

    // Return the number of nodes in the graph
    return this.nNodes;
  }


  @Override
  public int getShortestPathUnweighted(User user1, User user2) {
    // Get the node IDs of user1 and user2
    int nodeId1 = user1.getNodeId();
    int nodeId2 = user2.getNodeId();

    // Initialize a set to keep track of visited nodes
    HashSet<Integer> visited = new HashSet<>();

    // Initialize a queue to store nodes to visit
    ArrayList<Integer> nodeQueue = new ArrayList<>();

    // Add the starting node (nodeId1) to the queue and mark it as visited
    nodeQueue.add(nodeId1);
    visited.add(nodeId1);

    // Initialize the distance variable
    int distance = 0;

    // Perform breadth-first search to find the shortest path
    while (nodeQueue.size() > 0) {
      // Create a list to store nodes at the same distance from the starting node
      ArrayList<Integer> nodesInTheSameDistance = new ArrayList<>();

      // Process nodes at the current distance level
      while (nodeQueue.size() > 0) {
        // Remove a node from the queue
        int removed = nodeQueue.remove(0);

        // If the removed node is the target node (nodeId2), exit the loop
        if (removed == nodeId2) {
          break;
        }

        // Add the removed node to the list of nodes at the same distance
        nodesInTheSameDistance.add(removed);
      }

      // Process the nodes at the same distance level
      for (int node : nodesInTheSameDistance) {
        // Get the neighbors of the current node
        int[] neighbors = graph.neighbors(node);

        // Iterate over the neighbors
        for (int i = 0; i < neighbors.length; i++) {
          int neighbor = neighbors[i];

          // If the neighbor has not been visited, add it to the queue and mark it as visited
          if (!visited.contains(neighbor)) {
            nodeQueue.add(neighbor);
            visited.add(neighbor);
          }
        }
      }

      // Increment the distance as we move to the next level
      distance++;
    }

    // Return the shortest distance between the two nodes
    return distance;
  }


  @Override
  public List<Integer> recommendationByDistance(int dist, int userId) {
    // Initialize a list to keep track of visited nodes
    ArrayList<Integer> visited = new ArrayList<>();

    // Initialize a queue to store nodes to visit
    ArrayList<Integer> nodeQueue = new ArrayList<>();

    // Add the starting user ID to the queue and mark it as visited
    nodeQueue.add(userId);
    visited.add(userId);

    // Initialize the current distance variable
    int current_distance = 1;

    // Perform breadth-first search up to the given distance
    while (nodeQueue.size() > 0 && current_distance < dist) {
      // Create a list to store nodes at the same distance from the starting user
      ArrayList<Integer> nodesInTheSameDistance = new ArrayList<>();

      // Process nodes at the current distance level
      while (nodeQueue.size() > 0) {
        // Remove a node from the queue
        int removed = nodeQueue.remove(0);

        // Add the removed node to the list of nodes at the same distance
        nodesInTheSameDistance.add(removed);
      }

      // Process the nodes at the same distance level
      for (int node : nodesInTheSameDistance) {
        // Get the neighbors of the current node
        int[] neighbors = graph.neighbors(node);

        // Iterate over the neighbors
        for (int i = 0; i < neighbors.length; i++) {
          int neighbor = neighbors[i];

          // If the neighbor has not been visited, add it to the queue and mark it as visited
          if (!visited.contains(neighbor)) {
            nodeQueue.add(neighbor);
            visited.add(neighbor);
          }
        }
      }

      // Increment the current distance as we move to the next level
      current_distance++;
    }

    // Return the list of visited nodes within the specified distance
    return visited;
  }
}
