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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    ImageView imageView;
    TextView txtView;
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
        txtView = findViewById(R.id.textView2);
        if (!OpenCVLoader.initDebug()) {
            boolean success =
                    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,
                            this, loaderCallbackInterface);
        } else {
            loaderCallbackInterface.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }


    }

    class DownLoadImageText extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
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
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            decodeARTag(bitmap);
        }
    }



    void decodeARTag(Bitmap bitmap){
        Mat mat = new Mat();
        Mat ids = new Mat();
        ArrayList<Mat> list= new ArrayList<>();
        Utils.bitmapToMat(bitmap,mat);
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGBA2RGB);
        Dictionary dictionary = Dictionary.get(Aruco.DICT_6X6_250);
        Aruco.detectMarkers(mat,dictionary, list, ids);
        txtView.setText(ids.get(0,0)[0]+"");
        String result = "";
        for (int i=0; i<5; i++){
            result = result+(int)(ids.get(i,0)[0])+",";
        }

        txtView.setText(result);

        Aruco.drawDetectedMarkers(mat,list,ids);
        Bitmap resultBitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat,resultBitmap);
        imageView.setImageBitmap(resultBitmap);

    }

    void test() {
        DownLoadImageText task = new DownLoadImageText();
        task.execute("https://docs.opencv.org/trunk/singlemarkersrejected.jpg");
    }
}

