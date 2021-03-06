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
                mediaPlayer!!.setDataSource("http://dl.mp3xd.eu/xd/c26Yh6pmvi24/Red+hot+chilli+peppers+by.mp3") // setup song from http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                mediaPlayer!!.prepare()
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
            if (mediaPlayer!!.isPlaying) {
                val sb = v as SeekBar
                val playPositionInMillisecconds = mediaFileLengthInMilliseconds / 100 * sb.progress
                mediaPlayer!!.seekTo(playPositionInMillisecconds)
            }
        }
        return false
    }

    override fun onCompletion(mp: MediaPlayer) {
        buttonPlayPause!!.setImageResource(R.drawable.ic_play_circle_filled_black_24dp)
    }

    override fun onBufferingUpdate(mp: MediaPlayer, percent: Int) {
        seekBarProgress!!.secondaryProgress = percent
    }

    private fun primarySeekBarProgressUpdater() {
        seekBarProgress!!.progress = mediaPlayer!!.currentPosition / 1000
        if (mediaPlayer!!.isPlaying) {
            val notification = Runnable { primarySeekBarProgressUpdater() }
            handler.postDelayed(notification, 1000)
        }
    }

    private fun initView() {
        buttonPlayPause = findViewById(R.id.ButtonTestPlayPause) as ImageButton
        buttonPlayPause!!.setOnClickListener(this)

        seekBarProgress = findViewById(R.id.SeekBarTestPlay) as SeekBar
        seekBarProgress!!.max = 99
        seekBarProgress!!.setOnTouchListener(this)


        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnBufferingUpdateListener(this)
        mediaPlayer!!.setOnCompletionListener(this)
    }
}
