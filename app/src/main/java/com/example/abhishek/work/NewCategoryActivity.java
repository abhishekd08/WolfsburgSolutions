package com.example.abhishek.work;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.abhishek.work.DatabaseOperations.FetchData;
import com.example.abhishek.work.Model.CategoryData;
import com.example.abhishek.work.SupportClasses.CustomEventListeners.ServerResponseListener.OnResponseReceiveListener;
import com.example.abhishek.work.SupportClasses.CustomEventListeners.ServerResponseListener.ServerResponse;
import com.example.abhishek.work.adapter.CategoryListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryListAdapter adapter;
    private FetchData fetchData;
    private ServerResponse serverResponse;

    private ArrayList<CategoryData> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        recyclerView = (RecyclerView) findViewById(R.id.new_category_activity_recycler_view_id);
        arrayList = new ArrayList<>();
        adapter = new CategoryListAdapter(NewCategoryActivity.this, arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        fetchData = new FetchData(NewCategoryActivity.this);
        serverResponse = fetchData.getServerResponseInstance();
        fetchData.getCategories();
        serverResponse.setOnResponseReceiveListener(new OnResponseReceiveListener() {
            @Override
            public void onResponseReceive(JSONObject responseJSONObject) {
                try {
                    JSONArray jsonArray = responseJSONObject.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject j = (JSONObject) jsonArray.get(i);
                        CategoryData categoryData = new CategoryData();
                        categoryData.setName(j.getString("name"));
                        categoryData.setId(j.getInt("id"));
                        arrayList.add(categoryData);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}