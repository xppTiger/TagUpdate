import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Haiping on 2018/11/30
 */

public class TagUpdateByPageRank {



    public HashMap<String, PageRankNode> nodes;

    public TagUpdateByPageRank(HashMap<String, PageRankNode> nodes){
        if(nodes != null){
            this.nodes = nodes;
        }else{
            this.nodes = new LinkedHashMap<>();
        }
    }

    public void addNode(PageRankNode node){
        nodes.put(node.getNodeId(), node);
    }

    public void initialize(){
        for(Map.Entry<String, PageRankNode> entry: nodes.entrySet()){
            ((PageRankNode) entry.getValue()).initialize();
        }
    }

    public void update(){
        for(Map.Entry<String, PageRankNode> entry: nodes.entrySet()){
            ((PageRankNode) entry.getValue()).updateNeighborTag();
        }
    }

    public void getOldTag(){
        for(Map.Entry<String, PageRankNode> entry: nodes.entrySet())
        {
            PageRankNode node = entry.getValue();
            System.out.println("node "+node.getNodeId()+", "+node.oldTagToString());
        }
        System.out.println("===================================");
    }

    public void getNewTag(){
        for(Map.Entry<String, PageRankNode> entry: nodes.entrySet())
        {
            PageRankNode node = entry.getValue();
            System.out.println("user "+node.getNodeId()+", "+node.newTagToString());
        }
        System.out.println("===================================");
    }

    public void iterate(){
        // Sol 1: set iteration times
        // Step 1: Initialize
        // Step 2: updateNeighborTag N times
        for(int i = 0; i<Properties.iterationTimes; i++){
            initialize();
            update();
            // 两种方案：
            // 采用全量数据：可以参考用户全部的视频情况；数据稳定；
            // 采用增量数据：仅参考当天数据；数据不稳定、不全面；但是迭代快
        }
        // Step 3: check the old and the new tags
        getOldTag();
        getNewTag();
    }

}
