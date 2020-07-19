package jp.jaxa.iss.kibo.rpc.sampleapk;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

public class BarCodeManager {

    private BarCode[] BarCodeArray;
    private int size;
    private double wp_distance;

    public BarCodeManager(int size, double d){
        this.size=size;
        this.wp_distance=d;
        if (BarCodeArray==null){
            BarCodeArray=new BarCode[size];
        }
    }

    public void add(int index,BarCode tBarCode){
        BarCodeArray[index]=tBarCode;

    }

    public BarCode getBarCode(int index){
        return BarCodeArray[index];
    }

    public void add(BarCode tBarCode){
        for (int i=0;i< size;i++){
            if (BarCodeArray[i]==null){
                BarCodeArray[i]=tBarCode;
                break;
            }
        }
    }

    public void compute(){
        for (int i=0;i< size;i++){
            if (BarCodeArray[i]!=null){
                BarCodeArray[i].compute(wp_distance);
            }
        }
    }


    public void Dump(){
        for (int i=0;i< size;i++){
            if (BarCodeArray[i]!=null){
                System.out.printf(" wp_pos_x %f", BarCodeArray[i].wp_pos_x);
                System.out.printf(" wp_pos_y %f", BarCodeArray[i].wp_pos_y);
                System.out.printf(" wp_pos_z %f", BarCodeArray[i].wp_pos_z);
                System.out.printf(" wp_qua_x %f", BarCodeArray[i].wp_qua_x);
                System.out.printf(" wp_qua_y %f", BarCodeArray[i].wp_qua_y);
                System.out.printf(" wp_qua_z %f", BarCodeArray[i].wp_qua_z);
                System.out.printf(" wp_qua_w %f \n", BarCodeArray[i].wp_qua_w);
            }
        }
    }

}
