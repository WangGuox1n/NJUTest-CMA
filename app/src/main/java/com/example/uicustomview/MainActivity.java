package com.example.uicustomview;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private SearchView searchView;
    private List<PersonnelFile> list = new ArrayList<>();
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        initData();
        PersonnelFileAdapter adapter = new PersonnelFileAdapter(MainActivity.this,R.layout.list_item,list);
        searchView = (SearchView)findViewById(R.id.searchView);
        //默认不弹出键盘
        searchView.setFocusable(false);
        listView = (ListView)findViewById(R.id.list_View);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,PersonnelFile_Modified.class);
                PersonnelFile personnelFile = (PersonnelFile)listView.getItemAtPosition(position);
                intent.putExtra("PersonnelFile",personnelFile);
                startActivity(intent);
            }
        });
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);

        //点击添加按钮
        Button addButton = (Button)findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PersonnelFile_Add.class);
                startActivity(intent);
            }
        });
    }

    public void initData(){
        list.add(new PersonnelFile("王国新1","计算机系","测试员","1","档案室","图片1"));
        list.add(new PersonnelFile("王国新2","计算机系","测试员","2","档案室","图片2"));
        list.add(new PersonnelFile("王国新2","计算机系","测试员","3","档案室","图片3"));
        list.add(new PersonnelFile("王国新2","计算机系","测试员","4","档案室","图片4"));
        list.add(new PersonnelFile("王国新5","计算机系","测试员","5","档案室","图片5"));
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ListAdapter adapter=listView.getAdapter();
        if(adapter instanceof Filterable){
            Filter filter=((Filterable)adapter).getFilter();
            if(newText.isEmpty()){
                filter.filter(null);
            }else{
                filter.filter(newText);
            }
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO Auto-generated method stub
        ListAdapter listAdapter=listView.getAdapter();
        if(listAdapter instanceof Filterable){
            Filter filter=((Filterable)listAdapter).getFilter();
            if(query.isEmpty()){
                filter.filter(null);
            }else{
                filter.filter(query);
            }
        }
        return false;
    }

}
