package com.example.cma.ui.training_management;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.cma.R;
import com.example.cma.model.training_management.TrainingApplication;
import com.example.cma.ui.staff_management.StaffFile_Main;
import com.example.cma.ui.staff_management.StaffManagement_Main;
import com.example.cma.ui.staff_management.StaffTraining_main;
import com.example.cma.ui.staff_management.Staff_Entry;

public class TrainingApplication_Entry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_application__entry);

        //在Activity代码中使用Toolbar对象替换ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        Button button1=(Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TrainingApplication_Entry.this,AnnualPlan_Main.class);
                startActivity(intent);
            }
        });

        Button button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TrainingApplication_Entry.this, TrainingApplication_Main.class);
                startActivity(intent);
            }
        });


        /*Button button3=(Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Staff_Entry.this,StaffTraining_main.class);
                startActivity(intent);
            }
        });*/
    }
}
