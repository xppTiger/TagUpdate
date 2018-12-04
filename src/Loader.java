import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Haiping on 2018/11/30
 */
public class Loader {


    public static  void load(){
        HashMap<String, PageRankNode> users = new HashMap<>();
        HashMap<String, PageRankNode> videos = new HashMap<>();

        File file = new File(Properties.fileName);
        BufferedReader reader = null;

        try {
            String tempString;
            String[] tempList;
            String userId;
            String videoId;
            double linkValue;
            int line = 1;

            reader = new BufferedReader(new FileReader(file));
            while ((tempString = reader.readLine()) != null) {
                tempList = tempString.split(",");
                //System.out.println("line "+line+": userId="+tempList[0]+", videoId="+tempList[1]+", linkValue="+tempList[2]);
                userId = tempList[0];
                videoId = tempList[1];
                linkValue = Double.valueOf(tempList[2]);
                PageRankNode user;
                PageRankNode video;

                //double[] userTagValue = {Math.random()};
                double[] userTagValue = new double[1];
                if(userId.equals("device1")){
                    userTagValue[0] = 2.0;
                }else if(userId.equals("device2")){
                    userTagValue[0] = 3.0;
                }else{
                    userTagValue[0] = 1.0;
                }
                //double[] videoTagValue = {Math.random()};
                double[] videoTagValue = {1};

                // load user, video information
                if(users.containsKey(userId)){
                    user = users.get(userId);
                }else{
                    user = new PageRankNode(userId, null, null, userTagValue);
                    users.put(userId, user);
                }
                if(videos.containsKey(videoId)){
                    video = videos.get(videoId);
                }else{
                    video = new PageRankNode(videoId, null, null, videoTagValue);
                    videos.put(videoId, video);
                }
               user.addNeighbor(video,linkValue);
                video.addNeighbor(user, linkValue);

                line++;
            }
            reader.close();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        TagUpdateByPageRank pr = new TagUpdateByPageRank(users, videos);
        pr.iterate();

    }

}
