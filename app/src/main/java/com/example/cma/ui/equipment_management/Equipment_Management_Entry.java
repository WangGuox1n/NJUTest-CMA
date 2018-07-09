package com.example.cma.ui.equipment_management;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.cma.R;
import com.example.cma.model.equipment_management.EquipmentApplication;
import com.example.cma.model.equipment_management.EquipmentReceive;

public class Equipment_Management_Entry extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_entry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout equipment_layout = (LinearLayout)findViewById(R.id.equipment_layout);
        LinearLayout equipment_receive_layout = (LinearLayout)findViewById(R.id.equipment_receive_layout);
        LinearLayout equipment_application_layout = (LinearLayout)findViewById(R.id.equipment_application_layout);
        LinearLayout equipment_use_layout = (LinearLayout)findViewById(R.id.equipment_use_layout);
        LinearLayout equipment_maintenance_layout = (LinearLayout)findViewById(R.id.equipment_maintenance_layout);
        equipment_layout.setOnClickListener(this);
        equipment_receive_layout.setOnClickListener(this);
        equipment_application_layout.setOnClickListener(this);
        equipment_use_layout.setOnClickListener(this);
        equipment_maintenance_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.equipment_layout:
                startActivity(new Intent(Equipment_Management_Entry.this,Equipment_Main.class));
                break;
            case R.id.equipment_receive_layout:
                startActivity(new Intent(Equipment_Management_Entry.this,EquipmentReceive_Main.class));
                break;
            case R.id.equipment_application_layout:
                startActivity(new Intent(Equipment_Management_Entry.this,EquipmentApplication_Main.class));
                break;
            case R.id.equipment_use_layout:
                startActivity(new Intent(Equipment_Management_Entry.this,EquipmentUse_Main.class));
                break;
            case R.id.equipment_maintenance_layout:
                startActivity(new Intent(Equipment_Management_Entry.this,EquipmentMaintenance_Main.class));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
