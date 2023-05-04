import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SocialNetworks implements ISocialNetwork {

    private Graph graph;
    private int nNodes;


    public SocialNetworks(boolean load) {
        if(load) {
            loadGraphFromDataSet("socfb-American75.mtx");
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
    public int getShortestPathUnweighted(int user1, int user2) {

        // Initialize a flag to represent is there a path between user1 and user2
        boolean found = false;
        // Initialize a set to keep track of visited nodes
        HashSet<Integer> visited = new HashSet<>();

        // Initialize a queue to store nodes to visit
        ArrayList<Integer> nodeQueue = new ArrayList<>();

        // Add the starting node (user1) to the queue and mark it as visited
        nodeQueue.add(user1);
        visited.add(user1);

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

                // If the removed node is the target node (user2), exit the loop
                if (removed == user2) {
                    found = true;
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

        // If there is no path between two users, the distance is infinity
        if (!found) {
            return Integer.MAX_VALUE;
        }

        // Else, return the shortest distance between the two nodes
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

    /**
     * This method reads a dataset containing fake user interests and returns a Map
     * where the key is the user ID and the value is a List of the user's interest IDs.
     * @param filepath - the path to the file containing the dataset
     * @return A HashMap where the key is the user ID and the value is a List of the user's interest IDs
     */
    @Override
    public Map<Integer, List<Integer>> loadUserInterests(String filepath) {
        // Create an empty HashMap to store the user IDs and their interests
        Map<Integer, List<Integer>> userInterests = new HashMap<>();

        // Create a new File object based on the file path provided
        File file = new File(filepath);
        try {
            // Create a new Scanner object to read the contents of the file
            Scanner scanner = new Scanner(file);
            // Loop through each line in the file
            while (scanner.hasNextLine()) {
                // Read the line and split it into two parts: the user ID and a list of interests
                String line = scanner.nextLine();
                line.trim();
                String[] parts = line.split("=");

                // Parse the user ID as an integer
                int userId = Integer.parseInt(parts[0].trim());

                // Parse the list of interests as an array of strings, then convert each interest
                // string to an integer and add it to a list
                String interestsString = parts[1].replace("[", "").replace("]", "").trim();

                if (interestsString.length() == 0) {
                    userInterests.put(userId, new ArrayList<>());
                    continue;
                }
                String[] interestsItems = interestsString.split(",");

                List<Integer> interests = new ArrayList<>();

                for (String interest : interestsItems) {
                    interests.add(Integer.parseInt(interest.trim()));

                }

                // Add the user ID and list of interests to the HashMap
                userInterests.put(userId, interests);
            }
        } catch (FileNotFoundException e) {
            // If the file cannot be found, throw a runtime exception
            throw new RuntimeException(e);
        }
        // Return the HashMap containing the user IDs and their interests
        return userInterests;
    }

    /**
     * This method clusters users by their interests, returning a Map where the keys are interest IDs and the
     * values are Lists of user IDs that have that interest.
     * @param interestMap - a Map where the keys are user IDs and the values are Lists of interest IDs
     * @return a Map where the keys are interest IDs and the values are Lists of user IDs that have that interest
     */
    @Override
    public Map<Integer, List<Integer>> clusterUserByInterest(Map<Integer, List<Integer>> interestMap) {
        // Create an empty HashMap to store the clustered users
        Map<Integer, List<Integer>> clusteredUsers = new HashMap<>();

        // Loop through each user ID in the interestMap
        for (int userId : interestMap.keySet()) {
            // Get the list of interests associated with the user
            List<Integer> interests = interestMap.get(userId);

            // Loop through each interest in the user's list of interests
            for (int interest : interests) {
                // If the clusteredUsers map doesn't yet contain the interest, add it with an empty list
                if (! clusteredUsers.containsKey(interest)) {
                    clusteredUsers.put(interest, new ArrayList<>());
                }

                // Add the user ID to the list of users associated with this interest
                clusteredUsers.get(interest).add(userId);
            }
        }
        // Return the HashMap containing the clustered users
        return clusteredUsers;
    }

    /**
     * This method returns a list of users in a specific interest cluster. If the given interest ID exists in the
     *  interestMap, the method returns the list of user IDs associated with that interest. Otherwise, it returns an
     *  empty list.
     * @param interestID - the ID of the interest cluster to retrieve
     * @param interestMap - a Map where the keys are user IDs and the values are Lists of interest IDs
     * @return a List of user IDs in the specified interest cluster, or an empty list if the cluster does not exist
     */

    @Override
    public List<Integer> getUsersInterestCluster(int interestID, Map<Integer, List<Integer>> interestMap) {
        // If the interestMap contains the given interestID, return the associated list of user IDs
        if (interestMap.containsKey(interestID)) {
            return interestMap.get(interestID);
        }

        // If the interestID does not exist in the interestMap, return an empty list
        return Collections.emptyList();
    }

    /**
     * This method recommends users in a specific interest cluster to a given user, sorted by distance. The method first
     * retrieves a list of user IDs associated with the specified interest cluster. For each user in the list, the method
     * calculates the shortest unweighted path between that user and the given user, and stores the distance in a map.
     * The method then sorts the list of users by the distances stored in the map, and returns the sorted list.
     * @param interestId - the ID of the interest cluster to recommend users from
     * @param userId - the ID of the user to recommend users to
     * @param interestMap - a Map where the keys are user IDs and the values are Lists of interest IDs
     * @return a List of user IDs in the specified interest cluster, sorted by distance from the given user
     */
    @Override
    public List<Integer> recommendationByInterest(int interestId, int userId, Map<Integer, List<Integer>> interestMap) {
        // Get a list of user IDs associated with the specified interest cluster
        List<Integer> usersWithSameInterest = interestMap.get(interestId);

        // Create a map to store the shortest unweighted path between each user and the given user
        Map<Integer, Integer> distanceByUser = new HashMap<>();
        for (int user : usersWithSameInterest) {
            int distance = getShortestPathUnweighted(userId, user);
            distanceByUser.put(user, distance);


        }

        // Sort the list of users by the distances stored in the map
        Collections.sort(usersWithSameInterest, Comparator.comparingInt(distanceByUser::get));

        // Return the sorted list of users
        return usersWithSameInterest;
    }

    /**
     * @param filepath
     * @return
     */
    @Override
    public Map<Integer, List<Integer>> loadPosts(String filepath) {
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
