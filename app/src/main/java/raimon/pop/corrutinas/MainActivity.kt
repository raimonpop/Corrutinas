package raimon.pop.corrutinas

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

import kotlinx.coroutines.Dispatchers.IO

import raimon.pop.corrutinas.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val PROGRESS_MAX = 100
    private val PROGRESS_START = 0
    private val JOB_TIME = 4000

    private var job = Job()
    private var job2 = Job()

    private lateinit var binding: ActivityMainBinding

    private var progressTotal1 = 0
    private var progressTotal2 = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var progressBar1: ProgressBar = binding.pbJob1
        var progressBar2 = binding.pbJob2
        var btn1 = binding.btmStartJob1
        btn1.setText("Start")
        var btn2 = binding.btmStartJob2
        btn2.setText("Start")
        binding.btmStopJob1.setText("Reset")
        binding.btmStopJob2.setText("Reset")
        binding.btmStartJob1.setOnClickListener {

            if (job.isActive) {
                binding.btmStartJob1.setText("Stop")
                startJob(job, progressBar1, btn1)
            } else if (job.isCancelled) {
                job = Job()
                restartJob(job, progressBar1, btn1)
            }
        }
        binding.btmStopJob1.setOnClickListener {
            job.cancel()
            job = Job()
            btn1.text = "Start"
            initJob(progressBar1, job)
        }
        binding.btmStartJob2.setOnClickListener {
            if (job2.isActive) {
                binding.btmStartJob2.setText("Stop")
                startJob(job2, progressBar2, btn2)
            } else if (job2.isCancelled) {
                job2 = Job()
                restartJob(job2, progressBar2, btn2)
            }
        }
        binding.btmStopJob2.setOnClickListener {
            job2.cancel()
            job2 = Job()
            btn2.text = "Start"
            initJob(progressBar2, job)
        }
    }


    private fun initJob(progressBar: ProgressBar, job: CompletableJob) {

        progressBar.progress = 0
    }

    private fun startJob(job: CompletableJob, progressBar: ProgressBar, btn: Button) {

        if (progressBar.progress > 0) {
            resetJob(job, progressBar, btn)
        }

        CoroutineScope(IO + job).launch {
            for (i in PROGRESS_START..PROGRESS_MAX) {
                delay((JOB_TIME / PROGRESS_MAX).toLong())
                progressBar.progress = i
            }
            if (job.isCompleted) {
                Toast.makeText(this@MainActivity, "Se ha iniciado aqui", Toast.LENGTH_SHORT).show()
                initJob(progressBar, job)
            }
        }
    }

    private fun restartJob(job: CompletableJob, progressBar: ProgressBar, btn: Button) {

        CoroutineScope(IO + job).launch {
            btn.setText("Stop")
            for (i in progressBar.progress..PROGRESS_MAX) {
                delay((JOB_TIME / PROGRESS_MAX).toLong())
                progressBar.progress = i
            }
            if (job.isCompleted) {
                Toast.makeText(this@MainActivity, "Se ha iniciado aqui", Toast.LENGTH_SHORT).show()
                initJob(progressBar,job)
            }

        }
    }

    private fun resetJob(job: CompletableJob, progressBar: ProgressBar, btn: Button) {
        if (job.isActive) {
            job.cancel()
            btn.setText("Restart")

        }

    }

}