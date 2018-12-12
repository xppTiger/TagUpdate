import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Haiping on 2018/11/30
 */

public class TagUpdateByPageRank {

    public HashMap<String, PageRankNode> nodes;
    private int iterationTimes;


    public TagUpdateByPageRank(HashMap<String, PageRankNode> nodes, int iterationTimes){
        if(nodes != null){
            this.nodes = nodes;
        }else{
            this.nodes = new LinkedHashMap<>();
        }
        if(iterationTimes <=0 ){
            this.iterationTimes = 0;
        }else{
            this.iterationTimes = iterationTimes;
        }
    }

    public void addNode(PageRankNode node){
        nodes.put(node.getNodeId(), node);
    }

    public void initializeNodes(){
        for(Map.Entry<String, PageRankNode> entry: nodes.entrySet()){
            ((PageRankNode) entry.getValue()).initializeTag();
        }
    }

    public void updatNodes(){
        for(Map.Entry<String, PageRankNode> entry: nodes.entrySet()){
            ((PageRankNode) entry.getValue()).updateNeighborTag();
        }
    }

    public void finalizeNodes(){
        for(Map.Entry<String, PageRankNode> entry: nodes.entrySet()){
            ((PageRankNode) entry.getValue()).finalizeTag();
        }
    }

    public void getOldTag(){
        for(Map.Entry<String, PageRankNode> entry: nodes.entrySet())
        {
            PageRankNode node = entry.getValue();
            System.out.println("node "+node.getNodeId()+" old category: "+node.oldTagCategoryToString());
            System.out.println("node "+node.getNodeId()+" old keyword: "+node.oldTagKeywordToString());
        }
        System.out.println("===================================");
    }

    public void getNewTag(){
        for(Map.Entry<String, PageRankNode> entry: nodes.entrySet())
        {
            PageRankNode node = entry.getValue();
            System.out.println("node "+node.getNodeId()+" new category: "+node.newTagCategoryToString());
            System.out.println("node "+node.getNodeId()+" new keyword: "+node.newTagKeywordToString());
        }
        System.out.println("===================================");
    }

    public void iterateNodes(){
        for(int i = 0; i<iterationTimes; i++){
            initializeNodes();
            updatNodes();
        }
        finalizeNodes();
    }

}
