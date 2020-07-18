package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Code by Aaron Lee
        Log.d("zencher","Started");
        LoaderCallbackInterface loaderCallbackInterface = new LoaderCallbackInterface() {
            @Override
            public void onManagerConnected(int status) {

            }

            @Override
            public void onPackageInstall(int operation, InstallCallbackInterface callback) {

            }
        };
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!OpenCVLoader.initDebug()) {
            boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, loaderCallbackInterface);
        } else {
            loaderCallbackInterface.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        test();
    }

    class DownloadImageTask extends AsyncTask<String, Void, android.graphics.Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                final Bitmap fBitmap = bitmap;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView imageView = findViewById(R.id.qrCodeImageView);
                        imageView.setImageBitmap(fBitmap);
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
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            decodeArTag(bitmap);
        }
    }

    void decodeQrCode(Bitmap bitmap){
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap,mat);
        QRCodeDetector qrCodeDetector = new QRCodeDetector();
        String result = qrCodeDetector.detectAndDecode(mat);
        TextView textView = findViewById(R.id.textView);
        textView.setText(result);
    }

    void decodeArTag(Bitmap bitmap){
        //要解析的圖
        Mat mat = new Mat();
        //解析過的圖
        Mat ids = new Mat();
        //解析出來的 corner list
        ArrayList<Mat> corners = new ArrayList<>();

        // bitmap to mat
        Utils.bitmapToMat(bitmap,mat);
        //去掉 alpha 通道
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGBA2RGB);
        //取得 Aruco 字典
        Dictionary dictionary = Dictionary.get(Aruco.DICT_6X6_250);
        Aruco.detectMarkers(mat,dictionary, corners, ids);
        Aruco.drawDetectedMarkers(mat,corners,ids);

        int[] id = {0,0,0,0,0,0};
        for (int i = 0; i < 6; i++){
            id[i] = (int)(ids.get(i,0)[0]);
        }
        TextView textView = findViewById(R.id.textView2);
        textView.setText(id[0]+","+id[1]+","+id[2]+","+id[3]+","+id[4]+","+id[5]);

        //轉為 Bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(mat.cols(),mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat,resultBitmap);
        ImageView imageView = findViewById(R.id.qrCodeImageView);
        imageView.setImageBitmap(resultBitmap);
    }

    void test(){
        DownloadImageTask task = new DownloadImageTask();
        //task.execute("https://cf.shopee.tw/file/d625f03f0b858bec3bb813e10f8253ee"); //QR Code
        task.execute("https://docs.opencv.org/trunk/singlemarkersoriginal.jpg"); //AR TAG
        Log.d("zencher","Catched Image");
    }
}