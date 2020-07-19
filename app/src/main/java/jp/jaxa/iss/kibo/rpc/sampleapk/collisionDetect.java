package jp.jaxa.iss.kibo.rpc.sampleapk;
import android.util.Log;

import gov.nasa.arc.astrobee.types.Point;
public class collisionDetect {
    private kozBlockManager tManager;
    private kizBlock tkizBlock;

    public collisionDetect(){
        tkizBlock=new kizBlock(10.25,	-9.75,	4.2,	11.65,	-3,	5.6);
        tManager=new kozBlockManager(9);
        tManager.add(new kozBlock(10.75,	-4.9,	4.8,	10.95,	-4.7,	5));
        tManager.add(new kozBlock(10.75,	-4.9,	4.8,	10.95,	-4.7,	5));
        tManager.add(new kozBlock(10.75,	-6.5,	3.9,	11.95,	-6.4,	5.9));
        tManager.add(new kozBlock(9.95,	-7.2,	3.9,	10.85,	-7.1,	5.9));
        tManager.add(new kozBlock(10.1,	-8.6,	5.4,	11.1,	-8.3,	5.9));
        tManager.add(new kozBlock(11.45,	-9,	4.1,	11.95,	-8.5,	5.1));
        tManager.add(new kozBlock(9.95,	-9.1,	4.6,	10.45,	-8.6,	5.6));
        tManager.add(new kozBlock(10.95,	-8.4,	4.9,	11.15,	-8.2,	5.1));
        tManager.add(new kozBlock(11.05,	-8.9,	4.2,	11.25,	-8.7,	4.4));
        tManager.add(new kozBlock(10.45,	-9.1,	4.6,	10.65,	-8.9,	4.8));
    }
    public Point check(Point currentPoint){
        Point resetPoint;
        Point resultPoint=null;
        resetPoint=tManager.check(currentPoint);
        if (resetPoint==null){
            Log.d("FuhSing_moveToWrapper_collisionDetect_Check: Still in Safe from KOZ moving",   "_currentPoint_" + currentPoint.toString());
            ///System.out.println("Still in Safe from KOZ moving");
        }else{
            Log.d("FuhSing_moveToWrapper_collisionDetect_Check: Not Safe from KOZ",  "_currentPoint_" + currentPoint.toString()+"_resetPoint_"+resetPoint.toString());
            resultPoint = resetPoint;
            ///System.out.println("Not Safe from KOZ reset to "+resetPoint.toString());
        }
        if ( resultPoint==null) {
            resetPoint = tkizBlock.checkBoundary(currentPoint);
        }else{
            resetPoint = tkizBlock.checkBoundary(resultPoint);
        }
        if (resetPoint==null){
            ///System.out.println("Still in Safe from KIZ moving");
            Log.d("FuhSing_moveToWrapper_collisionDetect_Check: Still in Safe from KIZ moving",  "_currentPoint_" + currentPoint.toString());
        }else{
            ///System.out.println("Not Safe from KOZ reset to "+resetPoint.toString());
            Log.d("FuhSing_moveToWrapper_collisionDetect_Check: Not Safe from KIZ",  "_currentPoint_" + currentPoint.toString()+"_resetPoint_"+resetPoint.toString());
            resultPoint = resetPoint;
        }
        return resultPoint;

    }
}