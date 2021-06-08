package com.example.activity6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.slice.Slice;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity6.api.ApiUtilities;
import com.example.activity6.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView text_total_confirm,text_total_active,text_total_recovered,text_total_death,text_total_tests;
    TextView text_today_confirm,text_today_recovered,text_today_death;
    TextView text_date;
    private PieChart pieChart;

    private List<CountryData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        initial();

        ApiUtilities.getAPiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for (int i = 0; i<list.size(); i++){
                            if (list.get(i).getCountry().equals("Philippines")){
                                int confirm = Integer.parseInt(list.get(i).getCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int recovered = Integer.parseInt(list.get(i).getRecovered());
                                int death = Integer.parseInt(list.get(i).getDeaths());

                                text_total_confirm.setText(NumberFormat.getInstance().format(confirm));
                                text_total_active.setText(NumberFormat.getInstance().format(active));
                                text_total_recovered.setText(NumberFormat.getInstance().format(recovered));
                                text_total_death.setText(NumberFormat.getInstance().format(death));


                                text_today_death.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                text_today_confirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                                text_today_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                                text_total_tests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                                setText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Confirm",confirm, Color.parseColor("#FFEB3B")));
                                pieChart.addPieSlice(new PieModel("Active",active, Color.parseColor("#E8447C")));
                                pieChart.addPieSlice(new PieModel("Recovered",recovered, Color.parseColor("#03A9F4")));
                                pieChart.addPieSlice(new PieModel("Death",death, Color.parseColor("#673AB7")));
                                

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setText(String updated) {

        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        long milliseconds = Long.parseLong(updated);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        text_date.setText("Updated at "+format.format(calendar.getTime()));
    }

    private void initial(){
        text_total_confirm = findViewById(R.id.txt_total_confirm);
        text_total_recovered = findViewById(R.id.txt_total_recovered);
        text_total_active = findViewById(R.id.txt_total_active);
        text_total_death = findViewById(R.id.txt_total_death);
        text_total_tests = findViewById(R.id.txt_total_tests);
        text_today_confirm = findViewById(R.id.txt_today_confirm);
        text_today_recovered = findViewById(R.id.txt_today_recovered);
        text_today_death = findViewById(R.id.txt_today_death);
        pieChart = findViewById(R.id.pieChart);
        text_date = findViewById(R.id.txt_date);



    }
}