package globo.com.br.globoshop

import android.app.Activity
import android.content.Intent
import android.databinding.BindingAdapter
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.visa.checkout.Environment
import com.visa.checkout.VisaCheckoutSdk
import com.visa.checkout.VisaCheckoutSdkInitListener
import com.visa.checkout.VisaPaymentSummary
import io.clappr.player.Player
import io.clappr.player.base.Callback
import io.clappr.player.base.Event
import io.clappr.player.base.Options
import io.clappr.player.log.Logger


class MainActivity : AppCompatActivity(), View.OnClickListener {


    private val mHandler = Handler()
    private var title: String = ""
    private var desc: String = ""
    private var buttonPlayPause: ImageButton? = null

    private val runnable = Runnable {
        findViewById(R.id.product).visibility = View.VISIBLE
        (findViewById(R.id.title) as TextView).text = title
        (findViewById(R.id.value) as TextView).text = desc

    }
    var database = FirebaseDatabase.getInstance()
    var mDatabase: DatabaseReference? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDatabase = database.reference
        // Attach a listener to read the data at our posts reference
        mDatabase?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val video = dataSnapshot.children.first().getValue(Content::class.java)
                Log.d("pegou", "pegou:" + video.video)
                title = video.produtos.first().musica.nome
                desc = video.produtos.first().musica.valor.toString()
                showProduct()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

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



        buttonPlayPause = findViewById(R.id.buttonPlayPause) as ImageButton

        buttonPlayPause!!.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp)

        val imageButtonPlayPauseListener = View.OnClickListener {
            if (player!!.state == Player.State.PLAYING ) {
                player!!.pause()
                buttonPlayPause!!.setImageResource(R.drawable.ic_play_circle_filled_black_24dp)
            } else {
                player!!.play()
                buttonPlayPause!!.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp)
            }
        }
        buttonPlayPause!!.setOnClickListener(imageButtonPlayPauseListener);

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, player)
        fragmentTransaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == VISA_CHECKOUT_REQUEST_CODE) {
            Log.d(TAG, "Result got back from Visa Checkout SDK")
            var msg: String = "Compra realizada com sucesso!"
            val bundle = Bundle()

            if (resultCode == Activity.RESULT_OK && data != null) {
                val paymentSummary = data.getParcelableExtra<VisaPaymentSummary>(VisaCheckoutSdk.INTENT_PAYMENT_SUMMARY)
                if (paymentSummary != null) {
                    bundle.putParcelable(VisaCheckoutSdk.INTENT_PAYMENT_SUMMARY, paymentSummary)
                    bundle.putString("msg", msg)
                    findViewById(R.id.btnCallNext).visibility = View.VISIBLE
                    findViewById(R.id.btnCallNext).setOnClickListener(this)
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

    fun showProduct() {
        mHandler.postDelayed(runnable, 3000)
    }

    override fun onClick(v: View?) {
        startActivity(Intent(this@MainActivity, PurchasesActivity::class.java))
    }
}
