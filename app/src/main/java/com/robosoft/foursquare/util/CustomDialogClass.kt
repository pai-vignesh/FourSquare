package com.robosoft.foursquare.util

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import com.robosoft.foursquare.R
import com.robosoft.foursquare.view.PlaceDetailsActivity

class CustomDialogClass(var c: Context) : Dialog(c), View.OnClickListener {
    lateinit var d: Dialog
    lateinit var close: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.review_dialog)
        close = findViewById(R.id.imageView1)
        close.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.imageView1 -> {

            }
            else -> {
            }
        }
        dismiss()
    }

    fun startVideoView(v: View){
        val intent = Intent(v.context, PlaceDetailsActivity::class.java)
        v.context.startActivity(intent)
    }
}