package globo.com.br.globoshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.visa.checkout.Environment;
import com.visa.checkout.VisaCheckoutSdk;
import com.visa.checkout.VisaCheckoutSdkInitListener;
import com.visa.checkout.VisaPaymentSummary;

public class MainActivity extends AppCompatActivity {
    public final static int VISA_CHECKOUT_REQUEST_CODE = 10102;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VisaCheckoutSdk.init(getApplicationContext(), Environment.SANDBOX,
                "6CTXIFZ3UZWM2DXI2Q9M21UxOpwAB1yOztxO3Q9PjogfEXBNk", "SYSTEMDEFAULT",
                new VisaCheckoutSdkInitListener() {
                    @Override
                    public void status(int code, String message) {
                        Log.v(TAG, "Code:" + code + "  Message:" + message);
                    }
                });

        findViewById(R.id.visaCheckoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = VisaCheckoutSdk.getCheckoutIntent(MainActivity.this,
                        ConfigureVisaPaymentInfo.getPurchaseInfo());
                startActivityForResult(intent, VISA_CHECKOUT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VISA_CHECKOUT_REQUEST_CODE) {
            Log.d(TAG, "Result got back from Visa Checkout SDK");
            String msg = null;
            Bundle bundle = new Bundle();

            if (resultCode == RESULT_OK && data != null) {
                VisaPaymentSummary paymentSummary =
                        data.getParcelableExtra(VisaCheckoutSdk.INTENT_PAYMENT_SUMMARY);
                if (paymentSummary != null) {
                    msg = "Purchase Success!";
                    bundle.putParcelable(VisaCheckoutSdk.INTENT_PAYMENT_SUMMARY, paymentSummary);

                    finish();

                    // start the next activity
                    Intent intent = new Intent(this, MainActivity.class);
                    bundle.putString("msg", msg);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            } else if (resultCode == RESULT_CANCELED) {
                msg = "User Canceled, Result Code : " + resultCode;
            } else if (resultCode == VisaCheckoutSdk.ResultCode.RESULT_SDK_NOT_INITIALIZED) {
                msg = "Sdk not initialized  failed, Result Code : " + resultCode;
            } else if (resultCode == VisaCheckoutSdk.ResultCode.RESULT_INITIALIZED_FAILED) {
                msg = "VisaPaymentInfo validation failed, Result Code : " + resultCode;
            } else {
                msg = "Purchase failed!";
            }

            Log.d(TAG, msg);

            // show the failure reason
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
