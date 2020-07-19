package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.graphics.Bitmap;
/**import com.google.zxing.BinaryBitmap;
 import com.google.zxing.LuminanceSource;
 import com.google.zxing.MultiFormatReader;
 import com.google.zxing.RGBLuminanceSource;
 import com.google.zxing.Reader;
 import com.google.zxing.common.HybridBinarizer;*/
import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.Kinematics;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import gov.nasa.arc.astrobee.types.Vec3d;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;
import android.util.Log;

import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;
import  org.opencv.calib3d.Calib3d;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;
import static org.opencv.aruco.Aruco.estimatePoseSingleMarkers;
import static org.opencv.calib3d.Calib3d.calibrateCamera;
import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_32FC1;
import static org.opencv.core.CvType.CV_32FC3;


/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */


public class YourService extends KiboRpcService {
    private static final boolean student_version = true;
    private static final boolean safe_version = true;
    private static final String teamName = "FuhSingRobotic";
    collisionDetect m_collisionDetect;
    BarCodeManager m_BarCodeManager;
    P3 m_P3;
    @Override
    protected void runPlan1() {
        try {
            m_collisionDetect=new collisionDetect();
            m_P3=new P3();
            api.judgeSendStart();

            /// The starting position is fixed. The coordinates are;
            /// Position (x, y, z) = (10.95 -3.75 4.85)
            /// Orientation (x, y, z, w) = (0 0 0.707 -0.707)
            //// moveToWrapper("m_p1_1",10.95,-5.7,4.90, 0.0, 0.0, 0.0, 1);
            //        P1_1 = (11.5, -5.7, 4.5) (0, 0, 0, 1)     on Kiz  x-30
            //        P1-2 (11, -6, 5.55) (0, -0.7071068, 0, 0.7071068)  z - 30
            //        P1-3 (11, -5.5, 4.33) (0, 0.7071068, 0, 0.7071068)  z + 30
            //        P2-1 (10.30, -7.5, 4.7) (0, 0, 1, 0)  on Kiz x + 30
            //        P2-2 (11.5, -8, 5) (0, 0, 0, 1) x - 30
            //        P2-3 (11, -7.7, 5.55) (0, -0.7071068, 0, 0.7071068) on Kiz z - 30


            //        x1	y1	z1	x2	y2	z2
            //        1	10.75	-4.9	4.8	10.95	-4.7	5
            //        2	10.75	-6.5	3.9	11.95	-6.4	5.9
            //        3	9.95	-7.2	3.9	10.85	-7.1	5.9
            //        4	10.1	-8.6	5.4	11.1	-8.3	5.9
            //        5	11.45	-9	4.1	11.95	-8.5	5.1
            //        6	9.95	-9.1	4.6	10.45	-8.6	5.6
            //        7	10.95	-8.4	4.9	11.15	-8.2	5.1
            //        8	11.05	-8.9	4.2	11.25	-8.7	4.4
            //        9	10.45	-9.1	4.6	10.65	-8.9	4.8
            //        KIZ	10.25	-9.75	4.2	11.65	-3	5.6


            if (student_version) {
                //// moveToWrapper("m_0", 10.50, -5.7, 4.50, 0, 0, 0, 1);
                String p1_1 = moveToQR("p1_1", 11.4, -5.7, 4.5, 0, 0, 0, 1, 0);
                m_P3.set(0, p1_1);


                String p1_3 = moveToQR("p1_3", 11, -5.5, 4.43, 0, 0.7071068, 0, 0.7071068, 2);
                m_P3.set(2, p1_3);


                String p1_2 = moveToQR("p1_2", 11, -6, 5.45, 0, -0.7071068, 0, 0.7071068, 1);
                m_P3.set(1, p1_2);
                /// moveToWrapper("m_4",11.00,-7.70,5.20, 0.0, -0.7071068,0,0.7071068);
                if (safe_version) {
                    moveToWrapper("m_1", 10.50, -6.1, 4.90, 0.0, -0.7071068, 0, 0.7071068);
                    moveToWrapper("m_2", 10.50, -6.80, 4.90, 0.0, -0.7071068, 0, 0.7071068);
                    moveToWrapper("m_3", 11.00, -6.80, 4.90, 0.0, -0.7071068, 0, 0.7071068);
                }
                else {
                    moveToWrapper("m_1", 10.50, -6.5, 4.90, 0.0, -0.7071068, 0, 0.7071068);
                }
                String p2_3 = moveToQR("p2_3", 11.2, -7.7, 5.45, 0, -0.7071068, 0, 0.7071068, 5);
                //// String p2_3 = moveToQR("p2_3", 11, -7.7, 5.45, 0, -0.7071068, 0, 0.7071068, 5);
                m_P3.set(5, p2_3);


                String p2_2 = moveToQR("p2_2", 11.4, -8, 5, 0, 0, 0, 1, 4);
                m_P3.set(4, p2_2);
                if (safe_version) {
                    moveToWrapper("m_6", 11.40, -7.5, 4.7, 0, 0, 1, 0);
                }
                //  String p2_1 = moveToQR("p2_1",10.41,-7.5,4.7,0,0,1,0,3);
                String p2_1 = moveToQR("p2_1", 10.40, -7.5, 4.7, 0, 0, 1, 0, 3);
                m_P3.set(3, p2_1);
                moveToWrapper("m_7", 10.70, -7.5, 5.1, 0, 0, 0, 0);
                moveToWrapper("m_8", 10.70, -9.4, 5.1, 0, 0, 0, 0);
            }
            else {
                m_BarCodeManager= new BarCodeManager(6, 0.10);
                m_BarCodeManager.add(0,new BarCode("P1_1",11.5, -5.7, 4.5,0.0, 0.0, 0.0, 1.0,0));
                m_BarCodeManager.add(1,new BarCode("P1_2",11, -6, 5.55,0, -0.7071068, 0, 0.7071068,1));
                m_BarCodeManager.add(2,new BarCode("P1_3",11, -5.5, 4.33,0, 0.7071068, 0, 0.7071068,2));
                m_BarCodeManager.add(3,new BarCode("P2_1",10.30, -7.5, 4.7,0, 0, 1, 0,3));
                m_BarCodeManager.add(4,new BarCode("P2_2",11.5, -8, 5,0, 0, 0, 1,4));
                m_BarCodeManager.add(5,new BarCode("P3_3",11, -7.7, 5.55,0, -0.7071068, 0, 0.7071068,5));
                m_BarCodeManager.compute();
                /// m_BarCodeManager.Dump();
                ///moveToWrapper("m_0", 10.50, -5.7, 4.50, 0, 0, 0, 1);
                moveToBarCode(0);
                moveToBarCode(2);
                moveToBarCode(1);
                if (safe_version) {
                moveToWrapper("m_1",10.50,-6.1,4.90, 0.0, -0.7071068, 0, 0.7071068);
                moveToWrapper("m_2",10.50,-6.80,4.90, 0.0, -0.7071068, 0, 0.7071068);
                moveToWrapper("m_3",11.00,-6.80,4.90, 0.0, -0.7071068,0,0.7071068);
                }
                else {
                    moveToWrapper("m_1", 10.50, -6.5, 4.90, 0.0, -0.7071068, 0, 0.7071068);
                }
                moveToBarCode(5);
                moveToBarCode(4);
                if (safe_version) {
                    moveToWrapper("m_6", m_BarCodeManager.getBarCode(3).prePos(m_BarCodeManager.getBarCode(4).getWPPoint()), m_BarCodeManager.getBarCode(3).getWPQuaternion());
                }
                moveToBarCode(3);
                moveToWrapper("m_7",10.70,-7.5,5.1,0,0,0,0);
                moveToWrapper("m_8",10.70,-9.4,5.1,0,0,0,0);
            }
            moveToWrapper("p3_",m_P3.pos_x,m_P3.pos_y,m_P3.pos_z,m_P3.qua_x,m_P3.qua_y,m_P3.qua_z,Math.sqrt(1 - (m_P3.qua_x * m_P3.qua_x) - (m_P3.qua_y * m_P3.qua_y) - (m_P3.qua_z * m_P3.qua_z)));

            /// Approaching the target point
            Point deltaPoint=arriveP3();
            /// Robotic plus
            /// omega minus
            /// Robotic plus safe 30 not safe 30
            /// omega  plus 25
            /// moveToWrapper("Laser_",m_P3.pos_x-deltaPoint.getX(),m_P3.pos_y-deltaPoint.getY(),m_P3.pos_z-deltaPoint.getZ(),m_P3.qua_x,m_P3.qua_y,m_P3.qua_z,Math.sqrt(1 - (m_P3.qua_x * m_P3.qua_x) - (m_P3.qua_y * m_P3.qua_y) - (m_P3.qua_z * m_P3.qua_z)));
            moveToWrapper("Laser_",m_P3.pos_x+deltaPoint.getX(),-9.4,m_P3.pos_z+deltaPoint.getZ(),m_P3.qua_x,m_P3.qua_y,m_P3.qua_z,Math.sqrt(1 - (m_P3.qua_x * m_P3.qua_x) - (m_P3.qua_y * m_P3.qua_y) - (m_P3.qua_z * m_P3.qua_z)));

            api.laserControl(true);
            api.laserControl(true);
            api.judgeSendFinishSimulation();
        } catch (Exception e) {
            // This will catch any exception, because they are all descended from Exception
            Log.d(teamName+"_runPlan1 Exception ",e.getMessage());
        }
    }

    @Override
    protected void runPlan2() {
        // write here your plan 2
    }

    @Override
    protected void runPlan3() {
        // write here your plan 3
    }

    private void moveToBarCode(int index){
        BarCode tBarCode;
        String  tResult;
        tBarCode=m_BarCodeManager.getBarCode(index);
        tResult=moveToQR(tBarCode.getName(),tBarCode.getWPPoint(),tBarCode.getWPQuaternion(),tBarCode.getQR());
        m_P3.set(tBarCode.getQR(),tResult);
    }

    private Point arriveP3(){
        final int LOOP_MAX = 10;
        Point deltaPoint=null;
        int arv = 0,arriveloopCounter=0;
        Log.d(teamName+"_runPlan1 ","arrived P3!!!");
        Mat arucoIDs = new Mat();
        while ((arv == 0)&& (arriveloopCounter < LOOP_MAX)) {
            try {
                Mat source = api.getMatNavCam();
                double cameraMatrixData[][]={{344.173397, 0.000000, 630.793795},{0.000000, 344.277922, 487.033834}, {0.000000, 0.000000, 1.000000}};
                double distortion_coefficientsinitData[][]={{-0.152963, 0.017530, -0.001107, -0.000210, 0.000000}};

                Mat cameraMatrix= new Mat(3,3,CV_32F);
                Mat distortion_coefficients=new Mat(1,4,CV_32F);

                for(int row=0;row<3;row++){
                    for(int col=0;col<3;col++)
                        cameraMatrix.put(row, col, cameraMatrixData[row][col]);
                }

                for(int row=0;row<1;row++){
                    for(int col=0;col<5;col++)
                        distortion_coefficients.put(row, col, distortion_coefficientsinitData[row][col]);
                }

                Log.d(teamName+"_runPlan1_NavCam Size:", source.size().toString());
                Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_5X5_250);// format ar 5cm*5cm
                Log.d(teamName+"_runPlan1_Parsing AR (dictionary)", dictionary.toString());
                List<Mat> corners = new ArrayList<>();
                Aruco.detectMarkers(source, dictionary, corners, arucoIDs);
                arv = (int) arucoIDs.get(0, 0)[0];
                Log.d(teamName+"_runPlan1_Parsing AR (Array)", arucoIDs.toString());
                Log.d(teamName+"_runPlan1_Parsing AR (corners)", corners.toString());
                Log.d(teamName+"_runPlan1_Parsing AR ",Integer.toString(arv));
                /// Log.d(teamName+"_runPlan1_Parsing AR (corner)", corners.get(1).toString());
                Mat rvecs = new Mat();
                Mat tvecs = new Mat();
                try {
                    estimatePoseSingleMarkers (corners,(float)0.05,cameraMatrix,distortion_coefficients,rvecs,tvecs);
                    /// rvecs
                    try {
                        Log.d(teamName + "_runPlan1_Parsing rvecs(0,0)[0]",Double.toString((double)rvecs.get(0, 0)[0]));
                    } catch (Exception e) {
                        // This will catch any exception, because they are all descended from Exception
                        Log.d(teamName+"_runPlan1_Parsing rvecs(0,0)[0] Exception ",e.getMessage());
                    }

                    try {
                        Log.d(teamName + "_runPlan1_Parsing rvecs(0,0)[1]",Double.toString((double)rvecs.get(0, 0)[1]));
                    } catch (Exception e) {
                        // This will catch any exception, because they are all descended from Exception
                        Log.d(teamName+"_runPlan1_Parsing rvecs(0,0)[1] Exception ",e.getMessage());
                    }

                    try {
                        Log.d(teamName + "_runPlan1_Parsing rvecs(0,0)[2]",Double.toString((double)rvecs.get(0, 0)[2]));
                    } catch (Exception e) {
                        // This will catch any exception, because they are all descended from Exception
                        Log.d(teamName+"_runPlan1_Parsing rvecs(0,0)[2] Exception ",e.getMessage());
                    }

                    try {
                        Log.d(teamName + "_runPlan1_Parsing rvecs(0,0)[3]",Double.toString((double)rvecs.get(0, 0)[3]));
                    } catch (Exception e) {
                        // This will catch any exception, because they are all descended from Exception
                        Log.d(teamName+"_runPlan1_Parsing rvecs(0,0)[3] Exception ",e.getMessage());
                    }


                    ////  tvecs
                    try {
                        Log.d(teamName + "_runPlan1_Parsing tvecs(0,0)[0]",Double.toString((double)tvecs.get(0, 0)[0]));
                    } catch (Exception e) {
                        // This will catch any exception, because they are all descended from Exception
                        Log.d(teamName+"_runPlan1_Parsing tvecs(0,0)[0] Exception ",e.getMessage());
                    }

                    try {
                        Log.d(teamName + "_runPlan1_Parsing tvecs(0,0)[1]",Double.toString((double)tvecs.get(0, 0)[1]));
                    } catch (Exception e) {
                        // This will catch any exception, because they are all descended from Exception
                        Log.d(teamName+"_runPlan1_Parsing tvecs(0,0)[1] Exception ",e.getMessage());
                    }

                    try {
                        Log.d(teamName + "_runPlan1_Parsing tvecs(0,0)[2]",Double.toString((double)tvecs.get(0, 0)[2]));
                    } catch (Exception e) {
                        // This will catch any exception, because they are all descended from Exception
                        Log.d(teamName+"_runPlan1_Parsing tvecs(0,0)[2] Exception ",e.getMessage());
                    }

                    try {
                        Log.d(teamName + "_runPlan1_Parsing tvecs(0,0)[3]",Double.toString((double)tvecs.get(0, 0)[3]));
                    } catch (Exception e) {
                        // This will catch any exception, because they are all descended from Exception
                        Log.d(teamName+"_runPlan1_Parsing tvecs(0,0)[3] Exception ",e.getMessage());
                    }

                    deltaPoint=new Point((double)tvecs.get(0, 0)[0]+0.15,0,(double)tvecs.get(0, 0)[1]+0.15);
                } catch (Exception e) {
                    // This will catch any exception, because they are all descended from Exception
                    Log.d(teamName+"_runPlan1_Parsing (corner 0 0) Exception ",e.getMessage());
                }

/*

                for(Mat corner : corners){
                    //do someting to anObject...
                    try {
                        Log.d(teamName+"_runPlan1_Parsing (corner dump) ", corner.dump());
                        double mx,x1,x2,x3,x4;
                        double wx1=0,wx2=0,hy1=0,hy2=0,tx,ty;
                        double my,y1,y2,y3,y4;
                        x1=(double)corner.get(0,0)[0];
                        y1=(double)corner.get(0,0)[1];
                        x2=(double)corner.get(0,1)[0];
                        y2=(double)corner.get(0,1)[1];
                        x3=(double)corner.get(0,2)[0];
                        y3=(double)corner.get(0,2)[1];
                        x4=(double)corner.get(0,3)[0];
                        y4=(double)corner.get(0,3)[1];
                        /// Navcam 1280*960
                        /// Kiz z 200 cm x 200 cm
                        tx=abs(x2-x1);
                        ty=abs(y2-y1);
                        if (tx>wx1) {
                            wx1=tx;
                        }
                        else {
                            if (tx>wx2) wx2=tx;
                        }
                        if (ty>hy1) {
                            hy1=ty;
                        }
                        else {
                            if (ty>hy2) hy2=ty;
                        }
                        tx=abs(x3-x2);
                        ty=abs(y3-y2);
                        if (tx>wx1) {
                            wx1=tx;
                        }
                        else {
                            if (tx>wx2) wx2=tx;
                        }
                        if (ty>hy1) {
                            hy1=ty;
                        }
                        else {
                            if (ty>hy2) hy2=ty;
                        }
                        tx=abs(x4-x3);
                        ty=abs(y4-y3);
                        if (tx>wx1) {
                            wx1=tx;
                        }
                        else {
                            if (tx>wx2) wx2=tx;
                        }
                        if (ty>hy1) {
                            hy1=ty;
                        }
                        else {
                            if (ty>hy2) hy2=ty;
                        }
                        tx=abs(x1-x4);
                        ty=abs(y1-y4);
                        if (tx>wx1) {
                            wx1=tx;
                        }
                        else {
                            if (tx>wx2) wx2=tx;
                        }
                        if (ty>hy1) {
                            hy1=ty;
                        }
                        else {
                            if (ty>hy2) hy2=ty;
                        }

                        Log.d(teamName+"_runPlan1_Parsing wx1 ",Double.toString(wx1));
                        Log.d(teamName+"_runPlan1_Parsing wx2 ",Double.toString(wx2));
                        Log.d(teamName+"_runPlan1_Parsing hy1 ",Double.toString(hy1));
                        Log.d(teamName+"_runPlan1_Parsing hy2 ",Double.toString(hy2));
                        /// TR is 15cm x 15 cm
                        mx=(((x1+x2+x3+x4)/4-640)*30*2/(wx1+wx2)+15)/100;
                        my=(((y1+y2+y3+y4)/4-480)*30*2/(hy1+hy2)+15)/100;
                        Log.d(teamName+"_runPlan1_Parsing avgx ",Double.toString((x1+x2+x3+x4)/4));
                        Log.d(teamName+"_runPlan1_Parsing avgy ",Double.toString((y1+y2+y3+y4)/4));
                        Log.d(teamName+"_runPlan1_Parsing mx ",Double.toString(mx));
                        Log.d(teamName+"_runPlan1_Parsing my ",Double.toString(my));
                        deltaPoint=new Point(mx,0,my);
                    } catch (Exception e) {
                        // This will catch any exception, because they are all descended from Exception
                        Log.d(teamName+"_runPlan1_Parsing (corner 0 0) Exception ",e.getMessage());
                    }

                }
 */
                arriveloopCounter++;


            } catch (Exception e) {
                // This will catch any exception, because they are all descended from Exception
                Log.d(teamName+"_runPlan1_Parsing Exception ",e.getMessage());
            }
        }
        api.judgeSendDiscoveredAR(Integer.toString(arv));
        return deltaPoint;
    }

    /**********************************************************************************************/

    private boolean checkArrival(Point target,Point current,boolean status){
        double errorRang;
        if (status) {
            errorRang=0.08;
        }
        else{
            errorRang=0.05;
        }
        if ((abs(target.getX()-current.getX())<errorRang) &&
                (abs(target.getY()-current.getY())<errorRang) &&
                (abs(target.getZ()-current.getZ())<errorRang)){
            return (boolean)true;
        } else {
            return (boolean)false;
        }
    }

    /**********************************************************************************************/
    private Result safemoveto(Point movepoint,Quaternion quaternion,boolean printRobotPosition){
        try {
            /// Thread.sleep(200);
            return api.moveTo(movepoint, quaternion, true);
        }catch(Exception e){
            Log.d(teamName+"_calling api.moveTo exception",  e.getMessage() + " Point_" + movepoint.toString() + " Quaternion__" + quaternion.toString());
        }
        return null;
    }

    /**********************************************************************************************/
    private Bitmap safegetBitmapNavCam(){
        try {
            Thread.sleep(200);
            return api.getBitmapNavCam();
        }catch(Exception e){
            Log.d(teamName+"_calling api.getBitmapNavCam exception",  e.getMessage());
        }
        return null;
    }

    /**********************************************************************************************/
    private void moveToWrapper(String action,double pos_x, double pos_y, double pos_z,
                               double qua_x, double qua_y, double qua_z,
                               double qua_w) {
        Point movepoint = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float) qua_x, (float) qua_y,
                (float) qua_z, (float) qua_w);
        moveToWrapper(action,movepoint,quaternion);
    }


    private boolean moveToWrapper(String action,Point movepoint,Quaternion quaternion) {
        boolean moveCorrect,arrival=false;
        try {
            final int LOOP_MAX = 20;

            Point tPoint;
            Log.d(teamName+"_moveToWrapper(Begin) MoveTo_point",action+"_0_"+movepoint.toString());
            Log.d(teamName+"_moveToWrapper(Begin) MoveTo_quaternion",action+"_0_"+quaternion.toString());
            Result result = safemoveto(movepoint, quaternion, true);
            int loopCounter = 0;
            moveCorrect = result.hasSucceeded();
            Log.d(teamName+"_moveToWrapper(Begin) after_moveTo",action+"_"+Integer.toString(loopCounter)+"_Status_"+result.getStatus().toString()+"_Msg_"+result.getMessage()+"_Success_"+String.valueOf(moveCorrect));
            while ((arrival==false) && (loopCounter < LOOP_MAX)) {
                try {
                    Log.d(teamName+"_moveToWrapper(While) MoveTo_point", action + "_" + Integer.toString(loopCounter) + "_" + movepoint.toString());
                    Log.d(teamName+"_moveToWrapper(While) quaternion", action + "_" + Integer.toString(loopCounter) + "_" + quaternion.toString());
                    result = safemoveto(movepoint, quaternion, true);
                    moveCorrect = result.hasSucceeded();
                    Log.d(teamName+"_moveToWrapper(While) after_moveTo", action + "_" + Integer.toString(loopCounter) + "__Status_" + result.getStatus().toString() + "_Msg_" + result.getMessage() + "_Success_" + String.valueOf(moveCorrect));
                    Kinematics currentKinematics = api.getTrustedRobotKinematics(20);
                    if (currentKinematics != null) {
                        Point currentPoint = currentKinematics.getPosition();
                        Quaternion currentQuaternion = currentKinematics.getOrientation();
                        Vec3d currentAngularVelocity = currentKinematics.getLinearVelocity();
                        if (checkArrival(movepoint, currentPoint, moveCorrect)) {
                            Log.d(teamName+"_moveToWrapper(Kinematics) checkArrival_Pass", action + "_" + Integer.toString(loopCounter) + "_point_" + movepoint.toString() + "_currentPoint_" + currentPoint.toString());
                            arrival = true;
                        } else {
                            try {
                                Log.d(teamName+"_moveToWrapper(Kinematics) checkArrival_Not_Pass", action + "_" + Integer.toString(loopCounter) + "_point_" + movepoint.toString() + "_currentPoint_" + currentPoint.toString());
                                tPoint = m_collisionDetect.check(currentPoint);
                                if (tPoint == null) {
                                    Log.d(teamName+"_moveToWrapper(Kinematics) No_collision", action + "_" + Integer.toString(loopCounter) + "_currentpoint_" + currentPoint.toString());
                                } else {
                                    Log.d(teamName+"_moveToWrapper(Kinematics) Collision Reset", action + "_" + Integer.toString(loopCounter) + "_originalpoint_" + movepoint.toString() + "_resetPoint_" + tPoint.toString());
                                    Result relativeresult = safemoveto(tPoint, quaternion, true);
                                    Log.d(teamName+"_moveToWrapper(Kinematics) After relativeMoveTo", action + "_" + Integer.toString(loopCounter) + "__Status_" + relativeresult.getStatus().toString() + "_Msg_" + relativeresult.getMessage() + "_Success_" + String.valueOf(relativeresult.hasSucceeded()));
                                }

                            } catch (Exception e) {
                                // This will catch any exception, because they are all descended from Exception
                                Log.d(teamName+"_moveToWrapper m_collisionDetect Exception ", e.getMessage());
                            }
                        }
                        switch (currentKinematics.getConfidence()) {
                            case GOOD:
                                Log.d(teamName+"_moveToWrapper(Kinematics) ", action + "_" + Integer.toString(loopCounter) + "_GOOD_" + currentPoint.toString());
                                Log.d(teamName+"_moveToWrapper(Kinematics) ", action + "_" + Integer.toString(loopCounter) + "_GOOD_" + currentQuaternion.toString());
                                Log.d(teamName+"_moveToWrapper(Kinematics) ", action + "_" + Integer.toString(loopCounter) + "_GOOD_" + currentAngularVelocity.toString());
                                break;
                            case LOST:
                                Log.d(teamName+"_moveToWrapper(Kinematics) ", action + "_" + Integer.toString(loopCounter) + "_LOST_" + currentPoint.toString());
                                Log.d(teamName+"_moveToWrapper(Kinematics) ", action + "_" + Integer.toString(loopCounter) + "_LOST_" + currentQuaternion.toString());
                                Log.d(teamName+"_moveToWrapper(Kinematics) ", action + "_" + Integer.toString(loopCounter) + "_LOST_" + currentAngularVelocity.toString());
                                break;
                            case POOR:
                                Log.d(teamName+"_moveToWrapper(Kinematics) ", action + "_" + Integer.toString(loopCounter) + "_POOR_" + currentPoint.toString());
                                Log.d(teamName+"_moveToWrapper(Kinematics) ", action + "_" + Integer.toString(loopCounter) + "_POOR_" + currentQuaternion.toString());
                                Log.d(teamName+"_moveToWrapper(Kinematics) ", action + "_" + Integer.toString(loopCounter) + "_POOR_" + currentAngularVelocity.toString());
                                break;
                            default:
                                Log.d(teamName+"_moveToWrapper(Kinematics) ", action + "_" + Integer.toString(loopCounter) + "_default_" + currentPoint.toString());
                                Log.d(teamName+"_moveToWrapper(Kinematics) ", action + "_" + Integer.toString(loopCounter) + "_default_" + currentQuaternion.toString());
                                Log.d(teamName+"_moveToWrapper(Kinematics) ", action + "_" + Integer.toString(loopCounter) + "_default_" + currentAngularVelocity.toString());
                        }
                    } else {
                        Log.d(teamName+"_moveToWrapper(Kinematics_Null) can't_get_currentKinematics_null", action + "_" + Integer.toString(loopCounter) + "__Status_" + result.getStatus().toString() + "_Msg_" + result.getMessage());
                    }
                    if (moveCorrect == false) {
                        Log.d(teamName+"_moveToWrapper(While)(NotCorrect) ", action + "_" + Integer.toString(loopCounter) + "__Status_" + result.getStatus().toString() + "_Msg_" + result.getMessage());
                    } else {
                        Log.d(teamName+"_moveToWrapper(While)(Correct) ", action + "_" + Integer.toString(loopCounter) + "__Status_" + result.getStatus().toString() + "_Msg_" + result.getMessage());
                    }

                    ++loopCounter;
                } catch (Exception e) {
                    // This will catch any exception, because they are all descended from Exception
                    Log.d(teamName+"_moveToWrapper(while) Exception ", e.getMessage());
                }
            }
        } catch (Exception e) {
            // This will catch any exception, because they are all descended from Exception
            Log.d(teamName+"_moveToWrapper Exception ",e.getMessage());
        }
        return arrival;
    }

    /*******************************************************************************************/

    public String scanQRImage(Bitmap bMap) {
        try {
            String contents = null;

            int width = bMap.getWidth();
            int height = bMap.getHeight();
            int pixel[] = new int[width*height];
            bMap.getPixels(pixel,0,width,0,0,width,height);
            Image barcode = new Image(width,height,"RGB4");


            barcode.setData(pixel);


            ImageScanner reader = new ImageScanner();
            reader.setConfig(Symbol.NONE, Config.ENABLE,0);
            reader.setConfig(Symbol.QRCODE,Config.ENABLE,1);


            Image barcode2 = barcode.convert("Y800");

            int result = reader.scanImage(barcode2);


            if(result != 0){
                SymbolSet symbolSet = reader.getResults();
                for(Symbol symbol : symbolSet){
                    contents = symbol.getData();
                }
            }
            return contents;
        } catch (Exception e) {
            // This will catch any exception, because they are all descended from Exception
            Log.d(teamName+"_scanQRImage Exception ",e.getMessage());
            return null;
        }
    }

    /*******************************************************************************************/

    private void relativeMoveToWrapper(double pos_x, double pos_y, double pos_z,
                                       double qua_x, double qua_y, double qua_z,
                                       double qua_w){
        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float)qua_x, (float)qua_y,
                (float)qua_z, (float)qua_w);
        relativeMoveToWrapper(point,quaternion);
    }
    private void relativeMoveToWrapper(Point point,Quaternion quaternion){
        final int LOOP_MAX = 3;
        Result result = api.relativeMoveTo(point, quaternion, true);

        int loopCounter = 0;
        while(!result.hasSucceeded() && loopCounter < LOOP_MAX){
            result = api.relativeMoveTo(point, quaternion, true);
            ++loopCounter;
        }
    }


    /**********************************************************************************************/

    public String moveToQR(String action,double pos_x, double pos_y, double pos_z,
                           double qua_x, double qua_y, double qua_z,
                           double qua_w, int QR) {
        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float)qua_x, (float)qua_y,
                (float)qua_z, (float)qua_w);
        return moveToQR(action,point,quaternion,QR);
    }
    public String moveToQR(String action,Point point,Quaternion quaternion, int QR) {
        try {
            final int BARCODE_LOOP_MAX = 6;
            int loopCounter;
            String back = null;
            while (back == null) {
                try {
                   if (moveToWrapper(action, point,quaternion)) {
                       loopCounter = 0;
                       while ((back == null) && (loopCounter < BARCODE_LOOP_MAX)) {
                           Bitmap bmap = api.getBitmapNavCam();
                           Log.d(teamName+"_moveToQR Reading_Bitmap size", Integer.toString(bmap.getByteCount()));
                           back = scanQRImage(bmap);
                           if (back == null) {
                               Log.d(teamName+"_moveToQR Scan Reading_Bitmap return NULL", "");
                           }
                           loopCounter++;
                       }
                   }
                } catch (Exception e) {
                    // This will catch any exception, because they are all descended from Exception
                    Log.d(teamName+"_moveToQR(while) Exception ", e.getMessage());
                    return null;
                }
            }
            api.judgeSendDiscoveredQR(QR, back);
            Log.d(teamName+"_moveToQR Reading_Result", QR + "  " + back);
            return back;
        } catch (Exception e) {
            // This will catch any exception, because they are all descended from Exception
            Log.d(teamName+"_moveToQR Exception ", e.getMessage());
            return null;
        }
    }

    /**********************************************************************************************/

    public String gotoqr (double pos_x, double pos_y, double pos_z,
                          double qua_x, double qua_y, double qua_z,
                          double qua_w, int QR)
    {
        String back = null;
        while (back==null) {
            moveToWrapper("gotogr",pos_x, pos_y, pos_z, qua_x, qua_y, qua_z, qua_w);
            Bitmap bmap = api.getBitmapNavCam();
            back = scanQRImage(bmap);
        }
        api.judgeSendDiscoveredQR(QR,back);
        Log.d("QR_Reading_Result",QR+"  "+back);
        return back;
    }
}
