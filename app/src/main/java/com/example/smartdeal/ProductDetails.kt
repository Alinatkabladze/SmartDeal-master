package com.example.smartdeal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_details.*


class ProductDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details)
        setSupportActionBar(toolbar)

        val title=intent.getStringExtra("title")
        val photoUrl=intent.getStringExtra("photo_url")

        product_name.text=title
        Picasso.get().load(photoUrl).into(photo)

       /* availability.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("$title Offer is Valid")
                .setPositiveButton("OK") { dialog, which -> }.create().show()

                }*/
        btnShareToOtherApps.setOnClickListener{
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,title)
            intent.type="text/plain"

            startActivity(Intent.createChooser(intent,"Share"))

        }

        }


}
