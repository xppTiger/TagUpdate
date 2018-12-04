import java.util.ArrayList;


/**
 * Created by Haiping on 2018/11/28
 */
public class PageRankNode {

    private String nodeId;
    private ArrayList<PageRankNode> neighborList;
    private ArrayList<Double> linkValueList;
    private double totalLinkValue;
    public double[] oldTagValue;
    public double[] newTagValue;

    public PageRankNode(String Id, ArrayList<PageRankNode> neighborList, ArrayList<Double> linkValueList, double[] tagValue){
        if((tagValue != null)&&(tagValue.length != Properties.numTag)){
            System.out.println("Mismatched tag value length");
            return;
        }
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

        this.oldTagValue = new double[Properties.numTag];
        this.newTagValue = new double[Properties.numTag];
        for(int i=0; i<Properties.numTag;i++){
            this.oldTagValue[i] = tagValue[i];
            this.newTagValue[i] = tagValue[i];
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

    public void initialize(){
        for(int i=0; i<Properties.numTag; i++){
            oldTagValue[i] = newTagValue[i];
            newTagValue[i] = 0;
        }
    }

    public void updateNeighborTag(){
        int neighborListLength = neighborList.size();
        for(int i = 0; i<neighborListLength; i++){
            PageRankNode prNode = neighborList.get(i);
            for(int j=0; j<Properties.numTag; j++){
                prNode.newTagValue[j] += oldTagValue[j] * linkValueList.get(i) / totalLinkValue;
            }
        }
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public ArrayList<PageRankNode> getNeighborList() {
        return neighborList;
    }

    public void setNeighborList(ArrayList<PageRankNode> neighborList) {
        this.neighborList = neighborList;
    }

    public ArrayList<Double> getLinkValueList() {
        return linkValueList;
    }

    public void setLinkValueList(ArrayList<Double> linkValueList) {
        this.linkValueList = linkValueList;
    }

    public double getTotalLinkValue() {
        return totalLinkValue;
    }

    public void setTotalLinkValue(double totalLinkValue) {
        this.totalLinkValue = totalLinkValue;
    }

    public double[] getOldTagValue() {
        return oldTagValue;
    }

    public void setOldTagValue(double[] oldTagValue) {
        this.oldTagValue = oldTagValue;
    }

    public double[] getNewTagValue() {
        return newTagValue;
    }

    public void setNewTagValue(double[] newTagValue) {
        this.newTagValue = newTagValue;
    }
}
