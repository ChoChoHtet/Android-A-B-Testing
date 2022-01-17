package com.android.ab_testing_example

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),DummyAdapter.ClickListener {
    companion object {
        private const val BUTTON_LAYOUT = "button_layout"
    }

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var adapter: DummyAdapter
    private var layoutStatus:Boolean= true
    private val dummyData = listOf<Container>(
        Container(1,"Open Top (OP)"),
        Container(2,"Open Top (OP)"),
        Container(3,"Open Top (OP)"),
        Container(4,"Open Top (OP)"),
        Container(5,"Open Top (OP)"),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAnalytics = Firebase.analytics
        //  firebaseAnalytics.setUserId("1")
        firebaseAnalytics.setUserProperty("name", "cho cho")
        firebaseAnalytics.logEvent("launch_app", null)
        //  firebaseAnalytics.setUserProperty()
        FirebaseInstallations.getInstance().getToken(true)
            .addOnCompleteListener {task->
                if (task.isSuccessful){
                    Log.d("Installations", "Installation auth token: " + task.result?.token)
                }else{
                    Log.e("Installations", "Unable to get Installation auth token")
                }
            }
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        adapter = DummyAdapter(this)
//        recycler_container.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_container.adapter = adapter
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    layoutStatus=remoteConfig[BUTTON_LAYOUT].asBoolean()
                    Log.d("Fetch data", "${task.result}}")
                    if (remoteConfig[BUTTON_LAYOUT].asBoolean()) {
                        button2.visibility = View.VISIBLE
                        button.visibility = View.GONE
                        recycler_container.layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        adapter.submitList(dummyData)
                    } else {
                        button2.visibility = View.GONE
                        button.visibility = View.VISIBLE
                        adapter.submitList(dummyData)
                        recycler_container.layoutManager = GridLayoutManager(this,3)

                    }
                } else {
                    Log.d("Fetch data", " fetch data failed ${task.exception?.localizedMessage}")
                }

            }


        button.setOnClickListener {
          //  val bundle = Bundle()
          //  bundle.putString("layout", "horizontal")
            firebaseAnalytics.logEvent("button_layout_horizontal", null)
        }
        button2.setOnClickListener {
            //val bundle = Bundle()
           // bundle.putString("layout", "vertical")
            firebaseAnalytics.logEvent("button_layout_vertical", null)
        }
    }

    override fun onItemClickListener(container: Container) {
      //  val bundle = Bundle()
      //  bundle.putString("layout", if (layoutStatus) "horizontal" else "vertical")
        val event =  if (layoutStatus) "item_click_horizontal" else "item_click_vertical"
        firebaseAnalytics.logEvent(event, null)
    }
}