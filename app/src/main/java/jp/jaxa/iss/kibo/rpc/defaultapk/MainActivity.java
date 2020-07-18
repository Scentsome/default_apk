package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    LoaderCallbackInterface loaderCallbackInterface = new LoaderCallbackInterface() {
        @Override
        public void onManagerConnected(int status) {

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
        if(!OpenCVLoader.initDebug()){
            boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, loaderCallbackInterface);
        }
        else{
            loaderCallbackInterface.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        test();
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {
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
            }
            catch(MalformedURLException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            decodeARTag(bitmap);

        }
    }
    void decodeQRCode(Bitmap bitmap){
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        QRCodeDetector qrCodeDetector = new QRCodeDetector();
        String result = qrCodeDetector.detectAndDecode(mat);
        Log.d("evelyn", result);
        textView.setText(result);
    }
    void decodeARTag(Bitmap bitmap)
    { //ARTag
        Mat mat = new Mat();
        //解析出來的id
        Mat ids = new Mat();
        //解析出來的corner list
        ArrayList<Mat> corner = new ArrayList<>();
        //bitmap to Mat
        Utils.bitmapToMat(bitmap, mat);
        //去掉alpha通道
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2RGB);
        //取得aruco的字典
        Dictionary dictionary = Dictionary.get(Aruco.DICT_6X6_250);
        Aruco.detectMarkers(mat, dictionary, corner, ids);
        textView.setText((ids.get(0, 0)[0])+"");

        int[] arr = {0, 0, 0, 0, 0, 0};
        for(int i = 0; i < 6; i++) {
            arr[i] = (int)(ids.get(i, 0)[0]);
        }
        textView.setText(arr[0]+", "+arr[1]+", "+ arr[2]+", "+arr[3]+", "+arr[4]+", "+ arr[5]);

        //Aruco.drawDetectedMarkers(mat, corner, ids);
        //Bitmap resultBitmap =Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        //Utils.matToBitmap(mat, resultBitmap);
        //imageView.setImageBitmap(resultBitmap);
    }
    void test(){
        DownloadImageTask task = new DownloadImageTask();
        task.execute("https://docs.opencv.org/trunk/singlemarkersoriginal.jpg");
    }
}
