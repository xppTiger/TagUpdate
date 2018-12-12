import java.util.*;


/**
 * Created by Haiping on 2018/11/28
 */
public class PageRankNode {

    private String nodeId;
    private ArrayList<PageRankNode> neighborList;
    private ArrayList<Double> linkValueList;
    private double totalLinkValue;
    private boolean lpaPreSet;
    private boolean lpa;
    public HashMap<String, Double> oldTagCategory;
    public HashMap<String, Double> newTagCategory;
    public HashMap<String, Double> oldTagKeyword;
    public HashMap<String, Double> newTagKeyword;

    public PageRankNode(String Id, ArrayList<PageRankNode> neighborList, ArrayList<Double> linkValueList, HashMap<String, Double> category, HashMap<String, Double> keyword, boolean lpa){

        boolean categorySet = false;
        boolean keywordSet = false;
        lpaPreSet = false;

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

        if(category != null){
            this.oldTagCategory = category;
            this.newTagCategory = category;
            categorySet= true;
        }else{
            this.oldTagCategory = new LinkedHashMap<>();
            this.newTagCategory = new LinkedHashMap<>();
        }
        if(keyword != null){
            this.oldTagKeyword = keyword;
            this.newTagKeyword = keyword;
            keywordSet = true;
        }else{
            this.oldTagKeyword = new LinkedHashMap<>();
            this.newTagKeyword = new LinkedHashMap<>();
        }
        lpaPreSet = categorySet || keywordSet;
        this.lpa = lpa;
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
                //System.out.println("duplicated user-video pair: "+nodeId+", "+neighbor.getNodeId()+", linkValue: "+linkValue);
            }
        }
        if(existed == false){
            this.neighborList.add(neighbor);
            this.linkValueList.add(linkValue);
            this.totalLinkValue += linkValue;
        }
    }

    public void clearNeighbor(){
        neighborList = new ArrayList<PageRankNode>();
        totalLinkValue = 0;
    }

    public void addToOldTagCategory(String categoryName, double categoryValue){
        if(this.oldTagCategory.containsKey(categoryName)) {
            System.out.println("exsited category name in oldTagCategory");
        }
         this.oldTagCategory.put(categoryName, categoryValue);
        lpaPreSet = true;
    }

    public void addToOldTagKeyword(String keywordName, double keywordValue){
        if(this.oldTagKeyword.containsKey(keywordName)){
            System.out.println("exsited keyword name in oldTagKeyword ");
        }
        this.oldTagKeyword.put(keywordName, keywordValue);
        lpaPreSet = true;
    }

    public void addToNewTagCategory(String categoryName, double categoryValue){
        if(this.newTagCategory.containsKey(categoryName)) {
            System.out.println("exsited category name in newTagCategory");
        }
        this.newTagCategory.put(categoryName, categoryValue);
    }

    public void addToNewTagKeyword(String keywordName, double keywordValue){
        if(this.newTagKeyword.containsKey(keywordName)){
            System.out.println("exsited keyword name in newTagKeyword ");
        }
        this.newTagKeyword.put(keywordName, keywordValue);
    }

    /*
    public void updateOldTag(String tagName, double tagValue){
        if(oldTag.containsKey(tagName)){
            double oldValue = oldTag.get(tagName);
            oldTag.put(tagName, oldValue + tagValue);
        }else{
            oldTag.put(tagName, tagValue);
        }
    }
    */

    public void updateNewTagCategory(String categoryName, double categoryValue){
        if(newTagCategory.containsKey(categoryName)){
            double oldValue = newTagCategory.get(categoryName);
            newTagCategory.put(categoryName, oldValue + categoryValue);
        }else{
            newTagCategory.put(categoryName, categoryValue);
        }
    }

    public void updateNewTagKeyword(String keywordName, double keywordValue){
        if(newTagKeyword.containsKey(keywordName)){
            double oldValue = newTagKeyword.get(keywordName);
            newTagKeyword.put(keywordName, oldValue + keywordValue);
        }else{
            newTagKeyword.put(keywordName, keywordValue);
        }
    }

    //Initialize oldTag and newTag before every iteration
    public void initializeTag(){

//        for (Iterator<Map.Entry<String, Double>> it = oldTag.entrySet().iterator(); it.hasNext();){
//            Map.Entry<String, Double> item = it.next();
//            //... todo with item
//            it.remove();
//        }

        if((lpa&&lpaPreSet) == false){
            oldTagCategory = newTagCategory;
            oldTagKeyword = newTagKeyword;
        }
        newTagCategory = new LinkedHashMap<>();
        newTagKeyword = new LinkedHashMap<>();
    }

    //update all neighbors' tag, both category and keyword, in each iteration
    public void updateNeighborTag(){
        if(Math.abs(totalLinkValue) > Properties.EPSILON){
            int neighborListLength = neighborList.size();
            for(int i = 0; i<neighborListLength; i++){
                if(linkValueList.get(i) > Properties.EPSILON){
                    PageRankNode prNode = neighborList.get(i);
                    for(Map.Entry<String, Double> entry: oldTagCategory.entrySet()){
                        prNode.updateNewTagCategory(entry.getKey(), entry.getValue()*linkValueList.get(i)/totalLinkValue);
                    }
                    for(Map.Entry<String, Double> entry: oldTagKeyword.entrySet()){
                        prNode.updateNewTagKeyword(entry.getKey(), entry.getValue()*linkValueList.get(i)/totalLinkValue);
                    }
                }
            }
        }
    }

    // finalize the category, not keyword, after all iterations
    public void finalizeTag(){
        String maxKey = "";
        double maxValue = 0;
        for(Map.Entry<String, Double> entry: newTagCategory.entrySet()){
            if(entry.getValue() > maxValue){
                maxKey = entry.getKey();
                maxValue = entry.getValue();
            }
        }
        newTagCategory = new LinkedHashMap<>();
        newTagCategory.put(maxKey, Properties.categoryInitialValue);
    }


    public String oldTagCategoryToString(){
        String result = "";
        for(Map.Entry<String, Double> entry: oldTagCategory.entrySet()){
            result += entry.getKey() + ": "+entry.getValue()+", ";
        }
        return result;
    }

    public String oldTagKeywordToString(){
        String result = "";
        for(Map.Entry<String, Double> entry: oldTagKeyword.entrySet()){
            result += entry.getKey() + ": "+entry.getValue()+", ";
        }
        return result;
    }

    public String newTagCategoryToString(){
        String result = "";
        for(Map.Entry<String, Double> entry: newTagCategory.entrySet()){
            result += entry.getKey() + ": "+entry.getValue()+", ";
        }
        return result;
    }

    public String newTagKeywordToString(){
        String result = "";
        for(Map.Entry<String, Double> entry: newTagKeyword.entrySet()){
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

    public boolean isLpa() {
        return lpa;
    }

    public void setLpa(boolean lpa) {
        this.lpa = lpa;
    }
}
