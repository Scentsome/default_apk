package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    //Chiwei
    ImageView imageView;
    TextView textView;

    LoaderCallbackInterface loaderCallbackInterface = new LoaderCallbackInterface() {
        @Override
        public void onManagerConnected(int status) {
            test();
        }

        @Override
        public void onPackageInstall(int operation, InstallCallbackInterface callback) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        //確認OpenCV Manager 已經載入

        if(!OpenCVLoader.initDebug()){
            boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,
                    this,
                    loaderCallbackInterface);
        }
        else{
            loaderCallbackInterface.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls){
            String url = urls[0];
            Bitmap bitmap = null;
            try{
                InputStream inputStream = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

                final Bitmap finalBitmap = bitmap;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(finalBitmap);
                    }
                });
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            decodeARTag(bitmap);
        }
    }
    void decodeQRCode(Bitmap bitmap){
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap,mat);
        QRCodeDetector qrCodeDetector = new QRCodeDetector();
        String result = qrCodeDetector.detectAndDecode(mat);
        textView.setText(result);
    }

    void decodeARTag(Bitmap bitmap){
        //ARTag
        Mat mat = new Mat();
        //解析出來的id
        Mat ids = new Mat();
        //解析出來的corner list
        ArrayList<Mat> corners = new ArrayList<>();

        //Bitmap to Mat
        Utils.bitmapToMat(bitmap, mat);
        //去掉alpha通道
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2RGB);
        //取得Aruco的字典
        Dictionary dictionary = Dictionary.get(Aruco.DICT_6X6_250);
        Aruco.detectMarkers(mat,dictionary,corners,ids);

        textView.setText((ids.get(0,0)[0])+"");
        String result = "";
        for(int i =0;i<6;i++){
            result=result+(int)(ids.get(i,0)[0])+",";
        }
        result=result.substring(0,result.length()-1);


        textView.setText(result);

        Aruco.drawDetectedMarkers(mat,corners,ids);
        Bitmap resultBitmap = Bitmap.createBitmap(mat.cols(),mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat,resultBitmap);
        imageView.setImageBitmap(resultBitmap);
    }

    void test(){
        DownloadImageTask task = new DownloadImageTask();
        task.execute("https://docs.opencv.org/trunk/singlemarkersoriginal.jpg");
    }
    //private void test(){
    //    DownloadImageTask downloadImageTask = new DownloadImageTask();
    //    downloadImageTask.execute("");
    //}
}

