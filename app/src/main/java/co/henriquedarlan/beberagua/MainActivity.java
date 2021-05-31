package co.henriquedarlan.beberagua;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // atributos
    private Button      btnNotify;
    private EditText    editMinutes;
    private TimePicker timePicker;

    private int        hour;
    private int        minute;
    private int        interval;

    private boolean activated = false;

    // Instaciamento da classe SharePreferences
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    btnNotify = findViewById(R.id.btn_notify);
    editMinutes = findViewById(R.id.edit_txt_number_interval);
    timePicker = findViewById(R.id.time_picker);

        timePicker.setIs24HourView(true);

        // Colocando nome do banco e definindo a visibilidade do banco de dados
        preferences = getSharedPreferences("db", Context.MODE_PRIVATE);

        activated = preferences.getBoolean("activated", false);

    if(activated){
        btnNotify.setText(R.string.pause);
        int color = ContextCompat.getColor( this,R.color.black);
        btnNotify.setBackgroundTintList(ColorStateList.valueOf(color));

        int interval = preferences.getInt("interval",0);
        int hour = preferences.getInt("hour",timePicker.getCurrentHour());
        int minute = preferences.getInt("minute", timePicker.getCurrentMinute());

        editMinutes.setText(String.valueOf(interval));
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);


    }


    }

    public void notifyClick(View view){
        String sInterval = editMinutes.getText().toString();

        // Tratamento para campos em branco
        if (sInterval.isEmpty()){
            Toast.makeText(this, R.string.error_msg, Toast.LENGTH_LONG).show();
            return;
        }
        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
        interval =Integer.parseInt(sInterval);

        // Logica para mudar a cor do button
        if(!activated){
            btnNotify.setText(R.string.pause);
            int color = ContextCompat.getColor(this, android.R.color.black);
            btnNotify.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.black));
            activated = true;

            //Salvar os dados
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activated", true);
            editor.putInt("interval", interval);
            editor.putInt("hour", hour);
            editor.putInt("minute", minute);
            editor.apply();

        } else {
            btnNotify.setText(R.string.notify);
            int color = ContextCompat.getColor(this, R.color.colorAccent);
            btnNotify.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
            activated = false;

            // remover dados
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activated", false);
            editor.remove("interval");
            editor.remove("hour");
            editor.remove("minute");
            editor.apply();
        }

        Log.d("teste", "hora: " + hour + "minuto: " + minute + "intervalo: " + interval );
    }
}