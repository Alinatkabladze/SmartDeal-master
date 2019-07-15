package com.example.smartdeal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.util.Log.d
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.room.Room
import com.example.smartdeal.database.AppDatabase
import com.example.smartdeal.database.ProductFromDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_woman.*
import kotlinx.android.synthetic.main.menu.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    lateinit var providers:List<AuthUI.IdpConfig>
    val My_Request_Code:Int=7117


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TOKEN_UPDATE",task.result!!.token)

                }

            }

        doAsync {

            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            ).build()

            db.productDao().insertAll(ProductFromDatabase(null, "Socks - one dozen",50 ))
            val products = db.productDao().getAll()

            uiThread {

                d("daniel", "products size? ${products.size} ${products[0].title}")

            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, MainFragment())
            .commit()

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.actionHome -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, MainFragment())
                        .commit()
                }
           /*     R.id.actionProfile-> {
                    supportParentActivityIntent.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment())
                        .commit()
                }*/
                R.id.actionAbout->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AboutFragment())
                        .commit()

                }

                R.id.actionCommunity-> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, CommunityFragment())
                        .commit()

                }
                R.id.actionAdmin-> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AdminFragment())
                        .commit()
           }
            }
            it.isChecked=true
            drawerLayout.closeDrawers()
           true

        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        providers= Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )
        shopSignInOptions()
       btn_sign_out.setOnClickListener{
            AuthUI.getInstance().signOut(this@MainActivity)
                .addOnCompleteListener{
                    btn_sign_out.isEnabled=false
                    shopSignInOptions()
                }
                .addOnFailureListener {
                    e-> Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==My_Request_Code){
            val response=IdpResponse.fromResultIntent(data)
            if(resultCode== Activity.RESULT_OK){
                val user= FirebaseAuth.getInstance().currentUser
                Toast.makeText(this,""+user!!.email,Toast.LENGTH_SHORT).show()
                btn_sign_out.isEnabled=true
            }
            else {
                Toast.makeText(this,""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun shopSignInOptions() {

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.MyTheme)
            .build(),My_Request_Code)

    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        drawerLayout.openDrawer(GravityCompat.START)
        return true
    }
}
