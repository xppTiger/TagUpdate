import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Haiping on 2018/11/30
 */
public class TagUpdate {

    public static  void load(){
        //HashMap<String, PageRankNode> videos = new HashMap<>();
        ConcurrentHashMap<String, PageRankNode> videosCon = new ConcurrentHashMap<>();
        HashMap<String, PageRankNode> users = new HashMap<>();

        File videoTagFile = new File(Properties.videoTagFile);
        File videoSimilarityFile = new File(Properties.videoSimilarityFile);
        File videoUserScoreFile = new File(Properties.videoUserScoreFile);
        BufferedReader reader = null;

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Properties.threadPoolSize);

        long startTime = System.currentTimeMillis();    //starting time of loading similarity and video-tag

        // Load video-similarity information
        // Table desc: video1Id | video2Id | similarity
        try {
            String tempString;
            String[] tempList;
            String videoIdLeft;
            String videoIdRight;
            double linkValue;
            int line = 1;

            reader = new BufferedReader(new FileReader(videoSimilarityFile));
            while ((tempString = reader.readLine()) != null) {
                tempList = tempString.split(",");
                //Position 0: video1Id
                videoIdLeft = tempList[0];
                //Position 1: video2Id
                videoIdRight = tempList[1];
                //Position 2: similarity
                linkValue = Double.valueOf(tempList[2]);
                PageRankNode videoLeft;
                PageRankNode videoRight;

                // one core
//                if(Math.abs(linkValue) > Properties.EPSILON){
//                    if(videosCon.containsKey(videoIdLeft)){
//                        videoLeft = videosCon.get(videoIdLeft);
//                    }else{
//                        videoLeft = new PageRankNode(videoIdLeft, null, null, null, null, true);
//                        videosCon.put(videoIdLeft, videoLeft);
//                    }
//                    if(videosCon.containsKey(videoIdRight)){
//                        videoRight = videosCon.get(videoIdRight);
//                    }else{
//                        videoRight = new PageRankNode(videoIdRight, null, null, null, null, true);
//                        videosCon.put(videoIdRight, videoRight);
//                    }
//                    videoLeft.addNeighbor(videoRight, linkValue);
//                    videoRight.addNeighbor(videoLeft, linkValue);
//                }

                // multiple cores
                final String keyLeft = videoIdLeft;
                final String keyRight = videoIdRight;
                final double value = linkValue;

                fixedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        PageRankNode vLeft;
                        PageRankNode vRight;
                        if(Math.abs(value)>Properties.EPSILON){
                            if(videosCon.containsKey(keyLeft)){
                                vLeft = videosCon.get(keyLeft);
                            }else{
                                vLeft = new PageRankNode(keyLeft, null, null, null, true);
                                videosCon.put(keyLeft, vLeft);
                            }
                            if(videosCon.containsKey(keyRight)){
                                vRight = videosCon.get(keyRight);
                            }else{
                                vRight = new PageRankNode(keyRight, null, null, null, true);
                                videosCon.put(keyRight, vRight);
                            }
                            vLeft.addNeighbor(vRight, value);
                            vRight.addNeighbor(vLeft, value);
                        }
                    }
                });
                line++;
            }
            fixedThreadPool.shutdown();
            while(!fixedThreadPool.isTerminated()){
                Thread.sleep(5*1000);
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

        long endTime = System.currentTimeMillis();    //ending time of loading similarity
        System.out.println();
        System.out.println("Step 1: Loading time of similarity: " + (endTime - startTime) + "ms");
        System.out.println("======================================");
        System.out.println();

        startTime = System.currentTimeMillis();         //starting time of video tag update

        // Load category and keyword information
        // Table desc: videoId | category | keyword1 | keyword2 | ...
        try {
            String tempString;
            String[] tempList;
            PageRankNode video;
            String videoId;
            int line = 1;

            reader = new BufferedReader(new FileReader(videoTagFile));
            while ((tempString = reader.readLine()) != null) {
                tempList = tempString.split(",");
                //Position 0: ID
                video = videosCon.get(tempList[0]);
                //Position 1: category
                if(tempList[1].equals("") == false){
                    video.addToOldTagCategory(tempList[1], Properties.categoryInitialValue);
                    video.addToNewTagCategory(tempList[1], Properties.categoryInitialValue);
                }
                //Position 2-n: keyword
                for(int i=2; i<tempList.length; i++){
                    video.addToOldTagKeyword(tempList[i], Properties.keywordInitialValue);
                    video.addToNewTagKeyword(tempList[i], Properties.keywordInitialValue);
                }
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

        endTime = System.currentTimeMillis();    //ending time of loading video-tag
        System.out.println();
        System.out.println("Step 2: Loading time of video-tag: " + (endTime - startTime) + "ms");
        System.out.println("======================================");
        System.out.println();

        startTime = System.currentTimeMillis();         //starting time of video tag update
        // update video tags
        HashMap<String, PageRankNode> videos = new HashMap<>();
        for(Map.Entry<String, PageRankNode> entry: videosCon.entrySet()){
            videos.put(entry.getKey(), entry.getValue());
        }
        TagUpdateByPageRank videoTagUpdate = new TagUpdateByPageRank(videos, Properties.videoTagUpdateIterationTimes);
        videoTagUpdate.iterateNodes();
        showNodes(videos, 10);
        endTime = System.currentTimeMillis();           //ending time of video tag update
        System.out.println();
        System.out.println("Step 3: Video tag update time: " + (endTime - startTime) + "ms");
        System.out.println("======================================");
        System.out.println();

        for(Map.Entry<String, PageRankNode> entry: videos.entrySet()){
            ((PageRankNode) entry.getValue()).clearNeighbor();
        }

        startTime = System.currentTimeMillis();         //starting time of loading video-user watch
        // Load video~user watch information
        // Table desc: videoId | userId | score
        try {
            String tempString;
            String[] tempList;
            PageRankNode video;
            PageRankNode user;
            String videoId;
            String userId;
            double linkValue;
            int line = 1;

            reader = new BufferedReader(new FileReader(videoUserScoreFile));
            while ((tempString = reader.readLine()) != null) {
                tempList = tempString.split(",");
                videoId = tempList[0];
                userId = tempList[1];
                linkValue = Double.valueOf(tempList[2]);
                // no need to add this line if the videoId is not in videos
                if(Math.abs(linkValue) > Properties.EPSILON){
                    if(videos.containsKey(videoId)){
                        video = videos.get(videoId);
                        if(users.containsKey(userId)){
                            user = users.get(userId);
                        }else{
                            user = new PageRankNode(userId, null, null, null, false);
                            users.put(userId, user);
                        }
                        video.addNeighbor(user, linkValue);
                        user.addNeighbor(video, linkValue);
                    }
                }
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

        endTime = System.currentTimeMillis();           //ending time of loading video-user watch
        System.out.println();
        System.out.println("Step 4: Loading time of video-user watch: " + (endTime - startTime) + "ms");
        System.out.println("======================================");
        System.out.println();

        startTime = System.currentTimeMillis();         //starting time of user tag update
        // update user tags
        TagUpdateByPageRank userTagUpdate = new TagUpdateByPageRank(videos, Properties.userTagUpdateIterationTimes);
        userTagUpdate.iterateNodes();
        showNodes(users, 10);
        endTime = System.currentTimeMillis();           //ending time of user tag update
        System.out.println();
        System.out.println("Step 5: User tag update time: " + (endTime - startTime) + "ms");
        System.out.println("======================================");
        System.out.println();

    }

    public static void showNodes(HashMap<String, PageRankNode> nodes, int showNum){

        int num = 1;
        for(Map.Entry<String, PageRankNode> entry: nodes.entrySet())
        {
            PageRankNode node = entry.getValue();
            System.out.println("nodeSeq "+num);
            System.out.println("node "+node.getNodeId()+" old category: "+node.oldTagCategoryToString());
            System.out.println("node "+node.getNodeId()+" old keyword: "+node.oldTagKeywordToString());
            System.out.println("node "+node.getNodeId()+" new category: "+node.newTagCategoryToString());
            System.out.println("node "+node.getNodeId()+" new keyword: "+node.newTagKeywordToString());
            num += 1;
            if(num > showNum){
                break;
            }
        }
    }
}
