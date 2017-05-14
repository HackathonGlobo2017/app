package globo.com.br.globoshop

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar


class PurchasesActivity : AppCompatActivity(), View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {


    private var buttonPlayPause: ImageButton? = null
    private var seekBarProgress: SeekBar? = null
    private var mediaPlayer: MediaPlayer? = null
    private var mediaFileLengthInMilliseconds: Int = 0 // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class

    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchases)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        initView()
    }


    override fun onClick(v: View) {
        if (v.id == R.id.ButtonTestPlayPause) {
            try {
                mediaPlayer!!.setDataSource("http://www.mfiles.co.uk/mp3-downloads/frederic-chopin-piano-sonata-2-op35-3-funeral-march.mp3") // setup song from http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                mediaPlayer!!.prepare() // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
            } catch (e: Exception) {
                e.printStackTrace()
            }

            mediaFileLengthInMilliseconds = mediaPlayer!!.duration // gets the song length in milliseconds from URL

            if (!mediaPlayer!!.isPlaying) {
                mediaPlayer!!.start()
                buttonPlayPause!!.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp)
            } else {
                mediaPlayer!!.pause()
                buttonPlayPause!!.setImageResource(R.drawable.ic_play_circle_filled_black_24dp)
            }

            primarySeekBarProgressUpdater()
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (v.id == R.id.SeekBarTestPlay) {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position */
            if (mediaPlayer!!.isPlaying) {
                val sb = v as SeekBar
                val playPositionInMillisecconds = mediaFileLengthInMilliseconds / 100 * sb.progress
                mediaPlayer!!.seekTo(playPositionInMillisecconds)
            }
        }
        return false
    }

    override fun onCompletion(mp: MediaPlayer) {
        /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete */
        buttonPlayPause!!.setImageResource(R.drawable.ic_play_circle_filled_black_24dp)
    }

    override fun onBufferingUpdate(mp: MediaPlayer, percent: Int) {
        /** Method which updates the SeekBar secondary progress by current song loading from URL position */
        seekBarProgress!!.secondaryProgress = percent
    }

    private fun primarySeekBarProgressUpdater() {
        seekBarProgress!!.progress = mediaPlayer!!.currentPosition / 1000 // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer!!.isPlaying) {
            val notification = Runnable { primarySeekBarProgressUpdater() }
            handler.postDelayed(notification, 1000)
        }
    }

    private fun initView() {
        buttonPlayPause = findViewById(R.id.ButtonTestPlayPause) as ImageButton
        buttonPlayPause!!.setOnClickListener(this)

        seekBarProgress = findViewById(R.id.SeekBarTestPlay) as SeekBar
        seekBarProgress!!.max = 99 // It means 100% .0-99
        seekBarProgress!!.setOnTouchListener(this)


        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnBufferingUpdateListener(this)
        mediaPlayer!!.setOnCompletionListener(this)
    }
}
