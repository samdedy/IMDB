package id.sam.imdb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import id.sam.imdb.service.APIClient;
import id.sam.imdb.service.APIInterfacesRest;
import id.sam.imdb.model.TodayWeather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    TextView txtLokasi, txtDeg, txtWind, txtSunset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLokasi = findViewById(R.id.txtLokasi);
        txtDeg = findViewById(R.id.txtDeg);
        txtWind = findViewById(R.id.txtWind);
        txtSunset = findViewById(R.id.txtSunset);
        img = findViewById(R.id.img);

        callForecastbyCity("jakarta");
    }

    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;
    public void callForecastbyCity(String kota){

        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        Call<TodayWeather> call3 = apiInterface.getForecastByCity(kota,"4fd84305fb0f6c588a1f00991b3a73b5");
        call3.enqueue(new Callback<TodayWeather>() {
            @Override
            public void onResponse(Call<TodayWeather> call, Response<TodayWeather> response) {
                progressDialog.dismiss();
                TodayWeather dataWeather = response.body();
                //Toast.makeText(LoginActivity.this,userList.getToken().toString(),Toast.LENGTH_LONG).show();
                if (dataWeather !=null) {
                    //     txtKota.setText(dataWeather.getName());
                    //     txtTemperature.setText(new DecimalFormat("##.##").format(dataWeather.getMain().getTemp()-273.15));
                    txtDeg.setText("Sun / "+String.valueOf(dataWeather.getWind().getDeg())+" degree");
                    txtWind.setText("Wind "+String.valueOf(dataWeather.getWind().getSpeed()));
                    txtSunset.setText("Sunset "+String.valueOf(dataWeather.getSys().getSunset()));
                    String image = "http://openweathermap.org/img/wn/"+ dataWeather.getWeather().get(0).getIcon()+"@2x.png";
                    Picasso.get().load(image).into(img);

                }else{

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TodayWeather> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Maaf koneksi bermasalah",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}