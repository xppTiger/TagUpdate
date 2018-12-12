/**
 * Created by Haiping on 2018/12/12
 */
public class Utilities {

    public static double calculatePcc(double[] arrayX, double[] arrayY){

        double avgX = 0;
        double avgY= 0;

        if(arrayX.length != arrayY.length){
            System.out.println("unmatched array length");
            return 0;
        }
        for(int i=0; i<arrayX.length; i++){
            avgX += arrayX[i];
            avgY += arrayY[i];
        }
        avgX /= arrayX.length;
        avgY /= arrayY.length;

        double diffLeft = 0;
        double diffRight = 0;
        for(int i=0; i<arrayX.length; i++){

        }
        return 0;

    }

}
