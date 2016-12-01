package newbie.irhamdz.todolistsimple;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText editDate, editTime, editTask, editTaskDesc;
    ImageButton calendarButton, timeButton;
    String edtDate, item;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createDatabase();

        getSupportActionBar().setTitle("New Task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //element
        Spinner spinner = (Spinner) findViewById(R.id.spinner_category);
        editDate = (EditText) findViewById(R.id.enter_date);
        editTime = (EditText) findViewById(R.id.enter_time);
        editTask = (EditText) findViewById(R.id.enter_task_title);
        editTaskDesc = (EditText) findViewById(R.id.enter_description);

        timeButton = (ImageButton) findViewById(R.id.icon_time);
        calendarButton = (ImageButton) findViewById(R.id.icon_calendar);


        editTime.setVisibility(View.INVISIBLE);
        timeButton.setVisibility(View.INVISIBLE);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Default");
        categories.add("Personal");
        categories.add("Homework");
        categories.add("Project");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(TaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                          /*Your code   to get date and time  */
                        selectedmonth = selectedmonth + 1;
                        editDate.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();

                editTime.setVisibility(View.VISIBLE);
                timeButton.setVisibility(View.VISIBLE);
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(TaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        editDate.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();

                editTime.setVisibility(View.VISIBLE);
                timeButton.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            /*case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;*/

            case R.id.action_done:
                insertIntoDB();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void tes(View view) {
        edtDate = editDate.getText().toString();
        TextView textTes = (TextView) findViewById(R.id.tes_text);
        textTes.setText(item);
    }

    protected void createDatabase() {
        db = openOrCreateDatabase("TaskDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tasks(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, task_name VARCHAR,task_description VARCHAR,date DATE,category VARCHAR);");
    }

    protected void insertIntoDB() {
        String task_name = editTask.getText().toString().trim();
        String task_description = editTaskDesc.getText().toString().trim();
        String date = editDate.getText().toString().trim();
        String category = item.trim();
        if (task_name.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill task title", Toast.LENGTH_LONG).show();
            return;
        } else if (task_description.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill task description", Toast.LENGTH_LONG).show();
            return;
        }

        String query = "INSERT INTO tasks (task_name,task_description,date,category) VALUES('" + task_name + "', '" + task_description + "','" + date + "','" + category + "');";
        db.execSQL(query);
        Toast.makeText(getApplicationContext(), "Task " + task_name + " Saved Successfully", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

}
