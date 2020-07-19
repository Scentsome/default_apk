package jp.jaxa.iss.kibo.rpc.sampleapk;
import gov.nasa.arc.astrobee.types.Point;
public class kozBlockManager {

    private kozBlock[] kozBlockArray;
    private int size;

    public kozBlockManager(int size){
        this.size=size;
        if (kozBlockArray==null){
            kozBlockArray=new kozBlock[size];
        }
    }

    public void add(kozBlock tBlock){
        for (int i=0;i< size;i++){
            if (kozBlockArray[i]==null){
                kozBlockArray[i]=tBlock;
                break;
            }
        }
    }


    public Point check(Point currentPoint){
        for (int i=0;i< size;i++){
            if (kozBlockArray[i]!=null){
                if (kozBlockArray[i].checkBoundary(currentPoint)) {
                    return kozBlockArray[i].getresetPoint();
                }
                break;
            }
        }
        return null;
    }


}