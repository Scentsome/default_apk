package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!OpenCVLoader.initDebug())
            Log.e("OpenCV","Unable to load OpenCV");
        else
            Log.e("OpenCV","OpenCV loaded");
    }
}

