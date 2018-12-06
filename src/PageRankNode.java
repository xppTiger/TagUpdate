import java.util.*;


/**
 * Created by Haiping on 2018/11/28
 */
public class PageRankNode {

    private String nodeId;
    private ArrayList<PageRankNode> neighborList;
    private ArrayList<Double> linkValueList;
    private double totalLinkValue;
    public HashMap<String, Double> oldTag;
    public HashMap<String, Double> newTag;

    public PageRankNode(String Id, ArrayList<PageRankNode> neighborList, ArrayList<Double> linkValueList, HashMap<String, Double> tag){

        if((neighborList != null)&&(linkValueList!=null)&&(neighborList.size() != linkValueList.size())){
            System.out.println("Mismatch between pageRankNodeList and score length");
            return;
        }

        this.nodeId = Id;
        if(neighborList != null){
            this.neighborList = neighborList;
        }else{
            this.neighborList = new ArrayList<PageRankNode>();
        }
        if(this.linkValueList != null){
            this.linkValueList = linkValueList;
        }else{
            this.linkValueList = new ArrayList<Double>();
        }
        this.totalLinkValue = 0;
        for(int i=0; i<this.linkValueList.size();i++){
            this.totalLinkValue += linkValueList.get(i);
        }

        if(tag != null){
            this.oldTag = tag;
            this.newTag = tag;
        }else{
            this.oldTag = new LinkedHashMap<>();
            this.newTag = new LinkedHashMap<>();
        }
    }

    public void addNeighbor(PageRankNode  neighbor, double linkValue){

        boolean existed = false;
        for(int i=0; i<neighborList.size();i++){
            PageRankNode curNode = neighborList.get(i);
            if(curNode.getNodeId().equals(neighbor.getNodeId())){
                existed = true;
                this.totalLinkValue -= this.linkValueList.get(i);
                this.linkValueList.set(i, linkValue) ;
                this.totalLinkValue +=  linkValue;
                System.out.println("duplicated user-video pair: "+nodeId+", "+neighbor.getNodeId()+", linkValue: "+linkValue);
            }
        }
        if(existed == false){
            this.neighborList.add(neighbor);
            this.linkValueList.add(linkValue);
            this.totalLinkValue += linkValue;
        }
    }

    public void addToOldTag(String tagName, double tagValue){
        if(this.oldTag.containsKey(tagName)) {
            System.out.println("exsited tag name in oldTag");
        }
         this.oldTag.put(tagName, tagValue);
    }

    public void updateOldTag(String tagName, double tagValue){
        if(oldTag.containsKey(tagName)){
            double oldValue = oldTag.get(tagName);
            oldTag.put(tagName, oldValue + tagValue);
        }else{
            oldTag.put(tagName, tagValue);
        }
    }

    public void addToNewTag(String tagName, double tagValue){
        if(this.newTag.containsKey(tagName)){
            System.out.println("existed tag name in newTag");
        }
        this.newTag.put(tagName, tagValue);
    }

    public void updateNewTag(String tagName, double tagValue){
        if(newTag.containsKey(tagName)){
            double oldValue = newTag.get(tagName);
            newTag.put(tagName, oldValue + tagValue);
        }else{
            newTag.put(tagName, tagValue);
        }
    }

    public void initialize(){

//        for (Iterator<Map.Entry<String, Double>> it = oldTag.entrySet().iterator(); it.hasNext();){
//            Map.Entry<String, Double> item = it.next();
//            //... todo with item
//            it.remove();
//        }
        oldTag = newTag;
        newTag = new LinkedHashMap<>();
    }

    public void updateNeighborTag(){
        int neighborListLength = neighborList.size();
        for(int i = 0; i<neighborListLength; i++){
            PageRankNode prNode = neighborList.get(i);
            for(Map.Entry<String, Double> entry: oldTag.entrySet()){
                if(totalLinkValue == 0){
                    prNode.updateNewTag(entry.getKey(), 0.0);
                }else{
                    prNode.updateNewTag(entry.getKey(), entry.getValue()*linkValueList.get(i)/totalLinkValue);
                }
            }
        }
    }

    public String oldTagToString(){
        String result = "";
        for(Map.Entry<String, Double> entry: oldTag.entrySet()){
            result += entry.getKey() + ": "+entry.getValue()+", ";
        }
        return result;
    }

    public String newTagToString(){
        String result = "";
        for(Map.Entry<String, Double> entry: newTag.entrySet()){
            result += entry.getKey() + ": "+entry.getValue()+", ";
        }
        return result;
    }


    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

}
