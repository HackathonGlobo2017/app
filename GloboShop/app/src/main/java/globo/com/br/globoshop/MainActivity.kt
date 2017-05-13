package globo.com.br.globoshop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.visa.checkout.Environment
import com.visa.checkout.VisaCheckoutSdk
import com.visa.checkout.VisaCheckoutSdkInitListener
import com.visa.checkout.VisaPaymentSummary
import io.clappr.player.Player
import io.clappr.player.base.Callback
import io.clappr.player.base.Event
import io.clappr.player.base.Options
import io.clappr.player.log.Logger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VisaCheckoutSdk.init(applicationContext, Environment.SANDBOX,
                "6CTXIFZ3UZWM2DXI2Q9M21UxOpwAB1yOztxO3Q9PjogfEXBNk", "SYSTEMDEFAULT",
                VisaCheckoutSdkInitListener { code, message -> Log.v(TAG, "Code:$code  Message:$message") })

        findViewById(R.id.visaCheckoutButton).setOnClickListener {
            val intent = VisaCheckoutSdk.getCheckoutIntent(this@MainActivity,
                    ConfigureVisaPaymentInfo.getPurchaseInfo())
            startActivityForResult(intent, VISA_CHECKOUT_REQUEST_CODE)
        }
        val player = Player()
        player.configure(Options(source = "http://clappr.io/highline.mp4", autoPlay = true))
        player.on(Event.WILL_PLAY.value, Callback.wrap { Logger.info("Will Play", "App") })
        player.on(Event.PLAYING.value, Callback.wrap { Logger.info("Playing", "App") })
        player.on(Event.DID_COMPLETE.value, Callback.wrap { Logger.info("Completed", "App") })
        player.on(Event.BUFFER_UPDATE.value, Callback.wrap { bundle: Bundle? -> Logger.info("Buffer update: " + bundle?.getDouble("percentage"), "App") })

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, player)
        fragmentTransaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == VISA_CHECKOUT_REQUEST_CODE) {
            Log.d(TAG, "Result got back from Visa Checkout SDK")
            var msg: String? = null
            val bundle = Bundle()

            if (resultCode == Activity.RESULT_OK && data != null) {
                val paymentSummary = data.getParcelableExtra<VisaPaymentSummary>(VisaCheckoutSdk.INTENT_PAYMENT_SUMMARY)
                if (paymentSummary != null) {
                    msg = "Purchase Success!"
                    bundle.putParcelable(VisaCheckoutSdk.INTENT_PAYMENT_SUMMARY, paymentSummary)

                    finish()

                    // start the next activity
                    val intent = Intent(this, MainActivity::class.java)
                    bundle.putString("msg", msg)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                msg = "User Canceled, Result Code : " + resultCode
            } else if (resultCode == VisaCheckoutSdk.ResultCode.RESULT_SDK_NOT_INITIALIZED) {
                msg = "Sdk not initialized  failed, Result Code : " + resultCode
            } else if (resultCode == VisaCheckoutSdk.ResultCode.RESULT_INITIALIZED_FAILED) {
                msg = "VisaPaymentInfo validation failed, Result Code : " + resultCode
            } else {
                msg = "Purchase failed!"
            }

            Log.d(TAG, msg)

            // show the failure reason
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    companion object {
        val VISA_CHECKOUT_REQUEST_CODE = 10102
        private val TAG = MainActivity::class.java.simpleName
    }
}