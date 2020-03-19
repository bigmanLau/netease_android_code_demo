package com.kotlin.bigman.injectview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kotlin.bigman.library.RecyclerItemClickListener;
import com.kotlin.bigman.library.annotation.ContentView;
import com.kotlin.bigman.library.annotation.InjectView;
import com.kotlin.bigman.library.annotation.OnClick;
import com.kotlin.bigman.library.annotation.OnItemClick;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private List<Fruit> fruitList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFruits();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FruitAdapter adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "show" + position, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @OnItemClick(R.id.recycler_view)
    public void OnRecycleItemClick(View view,int position){
        Toast.makeText(MainActivity.this, "OnRecycleItemClick"+position, Toast.LENGTH_SHORT).show();
    }

    private void initFruits() {
        fruitList=new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Fruit apple = new Fruit("Apple", R.mipmap.ic_launcher);
            fruitList.add(apple);
            Fruit banana = new Fruit("Banana", R.mipmap.ic_launcher);
            fruitList.add(banana);
            Fruit orange = new Fruit("Orange", R.mipmap.ic_launcher);
            fruitList.add(orange);
            Fruit watermelon = new Fruit("Watermelon", R.mipmap.ic_launcher);
            fruitList.add(watermelon);
            Fruit pear = new Fruit("Pear", R.mipmap.ic_launcher);
            fruitList.add(pear);
            Fruit grape = new Fruit("Grape", R.mipmap.ic_launcher);
            fruitList.add(grape);
            Fruit pineapple = new Fruit("Pineapple", R.mipmap.ic_launcher);
            fruitList.add(pineapple);
            Fruit strawberry = new Fruit("Strawberry", R.mipmap.ic_launcher);
            fruitList.add(strawberry);
            Fruit cherry = new Fruit("Cherry", R.mipmap.ic_launcher);
            fruitList.add(cherry);
            Fruit mango = new Fruit("Mango", R.mipmap.ic_launcher);
            fruitList.add(mango);

        }


    }

    @OnClick(R.id.btn)
    public void show(View view){
        Log.d("show","234141");
        Toast.makeText(this, "show", Toast.LENGTH_SHORT).show();

    }
}
