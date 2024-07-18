package com.example.applemarket

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val layoutManager by lazy {
        LinearLayoutManager(this)
    }
    override fun `onCreate`(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 바인딩
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listDataInit()


        // 리싸이클러 뷰 디바이더
        val listDivider = DividerItemDecoration(applicationContext, VERTICAL)
        binding.listView.addItemDecoration(listDivider)
        // 리싸이클러 뷰
        val adapter = ListAdapter(listData)
        binding.listView.adapter = adapter
        binding.listView.layoutManager = layoutManager
        // 리싸이클러 뷰 클릭 이벤트
        adapter.itemClick = object : ListAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("detailData", listData[position])
                startActivity(intent)
            }
        }
        adapter.itemLongClick = object : ListAdapter.ItemLongClick {
            @SuppressLint("NotifyDataSetChanged")
            override fun onLongClick(view: View, position: Int) {
                // Toast.makeText(this@MainActivity, "position = $position \n this = $this", Toast.LENGTH_SHORT).show()

                val quitDialogBuilder = AlertDialog.Builder(this@MainActivity)
                val quitDialog = layoutInflater.inflate(R.layout.delete_dialog, null)
                quitDialogBuilder.setView(quitDialog)

                val dialogCreate = quitDialogBuilder.create()
                dialogCreate.show()

                // 버튼 클릭시 이벤트
                val btnQuit = quitDialog.findViewById<Button>(R.id.btnQuit)
                btnQuit.setOnClickListener() {

                    // 데이터 삭제
                    if (position < listData.size) {
                        listData.removeAt(position)
                        adapter.notifyItemRemoved(position)
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        // 실행 할 코드
                        binding.listView.adapter?.notifyDataSetChanged()
                    }, 500)
                    dialogCreate.dismiss()

                }
                val btnBack = quitDialog.findViewById<Button>(R.id.btnBack)
                btnBack.setOnClickListener() {
                    dialogCreate.dismiss()
                }
            }
        }
        createNotificationChannel()
        binding.alarm.setOnClickListener {
            showNotification()
        }

        // 클릭시 스크롤 맨 위로
        binding.scrollTopMove.setOnClickListener(){
            binding.listView.smoothScrollToPosition(0)
        }
        // 스크롤 이벤트 작동
        binding.listView.addOnScrollListener(onScrollListener)
        // 뒤로가기 버튼 클릭시 콜백
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private val appleNotificationID = 1
    private val channelID = "appleMarket"

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 안드로이드 8.0
            val channel = NotificationChannel(
                channelID,
                "appleMarket channer",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "description text of this channel."
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("애플마켓 텍스트입니다.")
            .setContentText("알람이 재대로 작동했나요?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                    // 알림 권한이 없다면, 사용자에게 권한 요청
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    }
                    startActivity(intent)
                }
            }
            return
        }
        NotificationManagerCompat.from(this).notify(appleNotificationID, builder.build())
    }

    // 뒤로가기시 종료 다이얼로그 출력
//    override fun onBackPressed() {
//        val quitDialogBuilder = AlertDialog.Builder(this)
//        val quitDialog = layoutInflater.inflate(R.layout.quit_dialog, null)
//        quitDialogBuilder.setView(quitDialog)
//
//        val dialogCreate = quitDialogBuilder.create()
//        dialogCreate.show()
//
//        // 버튼 클릭시 이벤트
//        val btnQuit = quitDialog.findViewById<Button>(R.id.btnQuit)
//        btnQuit.setOnClickListener() {
//            finish()
//        }
//        val btnBack = quitDialog.findViewById<Button>(R.id.btnBack)
//        btnBack.setOnClickListener() {
//            dialogCreate.dismiss()
//        }
//    }
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val quitDialogBuilder = AlertDialog.Builder(this@MainActivity)
            val quitDialog = layoutInflater.inflate(R.layout.quit_dialog, null)
            quitDialogBuilder.setView(quitDialog)

            val dialogCreate = quitDialogBuilder.create()
            dialogCreate.show()

            // 버튼 클릭시 이벤트
            val btnQuit = quitDialog.findViewById<Button>(R.id.btnQuit)
            btnQuit.setOnClickListener() {
                finish()
            }
            val btnBack = quitDialog.findViewById<Button>(R.id.btnBack)
            btnBack.setOnClickListener() {
                dialogCreate.dismiss()
            }
        }
    }


    // 스크롤 내릴시 이벤트
    private var lastScrollY = 0
    private val onScrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (layoutManager.findFirstVisibleItemPosition() > 0) { // 스크롤이 맨 위에 있음
                binding.scrollTopMove.animate().alpha(1f).setDuration(300).start()
            } else { // 스크롤이 아래로 내려감
                binding.scrollTopMove.animate().alpha(0f).setDuration(300).start()
            }

            lastScrollY = recyclerView.scrollY
        }
    }
    // 종료시 스크롤 이벤트 삭제
    override fun onDestroy() {
        super.onDestroy()
        binding.listView.removeOnScrollListener(onScrollListener)
    }

    // 클릭시 좋아요 갱신
    @SuppressLint("NotifyDataSetChanged")
    public override fun onResume() {
        super.onResume()
        binding.listView.adapter?.notifyDataSetChanged()
    }

}
