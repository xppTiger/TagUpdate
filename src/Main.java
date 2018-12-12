
public class Main {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();    //获取开始时间
        TagUpdate.load();
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("Running time: " + (endTime - startTime) + "ms");    //输出程序运行时间

    }

}
