import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocialNetwork implements ISocialNetwork {
    private Graph graph; 
    /**
    * Read posts from a text file and create a list of Post objects 
    * @param filepath
    * @return a mapping from post id to a map of LikedPost objects  
    */
    
    // 0=[428 & 2023-01-02T17:03:49.461645Z, 1726 - 2023-01-17T06:21:49.464347Z, 4236 - 2023-01-17T00:21:49.464393Z, 2524 - 2022-11-10T06:03:49.464396Z, 4937 - 2023-03-10T05:15:49.464397Z, 4196 - 2023-04-29T11:36:49.464399Z, 1918 - 2023-02-25T22:30:49.464401Z,
    @Override
    public Map<Integer, List<LikedPost>> loadPosts(String filepath) {
        Map<Integer, List<LikedPost>> res = new HashMap<>(); 
        File f = new File(filepath); 
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line; 
            do {
                
                line = br.readLine(); 
                line = line.substring(0, line.length()-1);
                String[] kv = line.split("=[");
                int postId = Integer.valueOf(kv[0]); 
                res.put(postId, new ArrayList<>());
                
                String[] likes = kv[1].split(",");
                for (String like : likes) {
                    String[] tokens = like.split("&");
                    int userId = Integer.valueOf(tokens[0]);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime localDateTime = LocalDateTime.parse(tokens[1], formatter);
                    Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
                    res.get(postId).add(new LikedPost(postId, userId, instant));
                }
            } while (line != null); 
            br.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        return res;
    }
    
    
    
    /**
     * map from all posts to posts each user liked 
     * @param posts map returned from loadPosts 
     * @return a mapping from user to a list of posts (represented by postIds) they liked;  
     *         the list is ordered by the timestamp that this user liked the post 
     */

    @Override
    public Map<Integer, List<LikedPost>> clusterUserByPost(Map<Integer, List<LikedPost>> posts) {
        Map<Integer, List<LikedPost>> res = new HashMap<>(); 
        for (Map.Entry<Integer, List<LikedPost>> post: posts.entrySet()) {
            List<LikedPost> likes = post.getValue(); 
            for (LikedPost like : likes) {
               int userId = like.getUserId(); 
               if (!res.containsKey(userId)) { // create list if user does not exist 
                   res.put(userId, new ArrayList<LikedPost>());
               } 
               res.get(userId).add(like); // add LikedPost object to existing class
            }
        }
        for (Map.Entry<Integer, List<LikedPost>> likes : res.entrySet()) {
            Collections.sort(likes.getValue()); // sort each user's liked posts by reversed time order 
        }
        return res;
    }

    /**
     * posts that my friends recently liked within the given timeFrame 
     * @param timeFrame: only get friends' activities within this timeframe 
     * @param userId: 
     * @return a list of post IDs ordered by the timeStamp of friends liking them 
     */
    @Override
    public List<Integer> recommendPost(int userId, Instant earliest, 
                                        Map<Integer, List<LikedPost>> likedPosts) {
        List<LikedPost> posts = new ArrayList<>(); 
        int[] neighbors = this.graph.neighbors(userId); 
        for (int neigh : neighbors) {
            List<LikedPost> likes = likedPosts.get(neigh); 
            for (LikedPost like : likes) {
                if (like.getTimestamp().compareTo(earliest) > 0) { // only get posts within timeframe 
                    posts.add(like);
                } else {
                    break; 
                }
            }
        }
        
        Collections.sort(posts);                    // sort posts by reversed time of liking 
        List<Integer> res = new ArrayList<>(); 
        for (LikedPost post : posts) {              // only add post ids into result 
            res.add(post.getPostId()); 
        }
        return res;
    }

    @Override
    public int loadGraphFromDataSet(String filePath) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getShortestPathUnweighted(int user1, int user2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Integer> recommendationByDistance(int distance, int userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Integer, List<Integer>> loadUserInterests(String filepath) {
        // TODO Auto-generated method stub
        return null;
    }

    
    @Override
    public Map<Integer, List<Integer>> clusterUserByInterest(Map<Integer, List<Integer>> interestMap) {
        // TODO Auto-generated method stub
        return null;
    }

    
    @Override
    public List<Integer> getUsersInterestCluster(int interestID) {
        // TODO Auto-generated method stub
        return null;
    }

    
    @Override
    public List<Integer> recommendationByInterest(int interestId, int userId) {
        // TODO Auto-generated method stub
        return null;
    }

}
