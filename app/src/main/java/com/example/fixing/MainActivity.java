package com.example.fixing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_ABBR = "com.example.fixing.EXTRA_ABBR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_USD:
                navigateToFixingActivity("usd");
                break;
            case R.id.button_GBP:
                navigateToFixingActivity("gbp");
                break;
            case R.id.button_CHF:
                navigateToFixingActivity("chf");
                break;
            case R.id.button_RUB:
                navigateToFixingActivity("rub");
                break;
            case R.id.button_RON:
                navigateToFixingActivity("ron");
                break;
            case R.id.button_NOK:
                navigateToFixingActivity("nok");
                break;
            case R.id.button_DKK:
                navigateToFixingActivity("dkk");
                break;
            case R.id.button_TRY:
                navigateToFixingActivity("try");
                break;
            case R.id.button_PLN:
                navigateToFixingActivity("pln");
                break;
            case R.id.button_SEK:
                navigateToFixingActivity("sek");
                break;
            case R.id.button_CZK:
                navigateToFixingActivity("czk");
                break;
            case R.id.button_AUD:
                navigateToFixingActivity("aud");
                break;
            case R.id.button_CAD:
                navigateToFixingActivity("cad");
                break;
            case R.id.button_HUF:
                navigateToFixingActivity("huf");
                break;
            case R.id.button_ILS:
                navigateToFixingActivity("ils");
                break;
            default:
                Toast.makeText(MainActivity.this, "Button not found", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void navigateToFixingActivity(String extra) {
        Intent intent = new Intent(this, FixingActivity.class);
        intent.putExtra(EXTRA_ABBR, extra);
        startActivity(intent);
    }
}
