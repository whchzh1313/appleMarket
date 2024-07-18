package com.example.applemarket

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.applemarket.databinding.ActivityDetailBinding
import com.example.applemarket.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import kotlin.math.log


class DetailActivity : AppCompatActivity() {

    lateinit var detailBinding: ActivityDetailBinding
    lateinit var detailContent: ListData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        detailContent = intent.getParcelableExtra<ListData>("detailData")!!
        detailBinding.detailImage.setImageResource(detailContent!!.thumb)
        detailBinding.detailWriter.text = detailContent.writer
        detailBinding.detailPlace.text = detailContent.place
        detailBinding.detailTitle.text = detailContent.title
        detailBinding.detailDesc.text = detailContent.desc
        // 데이터 포멧 변경
        val decimal = DecimalFormat("#,###")
        val testNum = detailContent.price
        val priceFormat = decimal.format(testNum)
        detailBinding.detailPrice.text = "${priceFormat}원"

        println(detailContent)

        detailBinding.detailPrev.setOnClickListener {
            finish()
        }
        detailBinding.detailLike.setOnClickListener {
            val data = listData.find { it.idx == detailContent.idx } ?: return@setOnClickListener
            when (data.heart) {
                true -> {
                    Snackbar.make(it, "관심 목록에 추가 되 었 습 니다.", Snackbar.LENGTH_SHORT).show()
                    data.heart = false
                    data.like--
                    detailBinding.detailLike.setImageResource(R.drawable.heart_off)
                }
                false -> {
                    Snackbar.make(it, "관심 목록에 추가 되었습니다.", Snackbar.LENGTH_SHORT).show()
                    data.heart = true
                    data.like++
                    detailBinding.detailLike.setImageResource(R.drawable.heart_on)
                }
            }
        }
        fun likeChange () {
            when (listData.find { it.idx == detailContent.idx }!!.heart) {
                true -> {
                    detailBinding.detailLike.setImageResource(R.drawable.heart_on)
                }
                false -> {
                    detailBinding.detailLike.setImageResource(R.drawable.heart_off)
                }
            }
        }
        likeChange()
    }
}