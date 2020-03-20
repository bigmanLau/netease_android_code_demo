package com.example.javapoetbutterknife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.annotation.ARouter;
import com.example.annotation.BindView;
import com.example.annotation.HelloWorld;
import com.example.library.ButterKnife;

@ARouter(path="/app/MainActivity")
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tv.setText("大法师打发");
    }

    @HelloWorld
    public void hello(){

    }
}
