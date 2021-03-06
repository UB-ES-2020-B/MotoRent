package ub.es.motorent.app.view

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.google.common.collect.Maps
import kotlinx.android.synthetic.main.settings_activity.*
import ub.es.motorent.R
import ub.es.motorent.app.presenter.SettingsPresenter


class SettingsActivity : AppCompatActivity() {

    private lateinit var presenter: SettingsPresenter
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "fluxControl"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        supportActionBar?.hide()

        presenter = SettingsPresenter(this);

        val changePersInf : Button = findViewById(R.id.changePersonalInfBtn)
        val changeBankInf : Button = findViewById(R.id.changeBankInfBtn)

        val logOutButton : Button = findViewById(R.id.logoutBtn)
        val autoLog : CheckBox = findViewById(R.id.automaticLoginCheck)

        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        val editor = sharedPref.edit()

        val buttonSendMail:Button = findViewById(R.id.sendMailButton)

        if(sharedPref.contains("autoLog")){
            autoLog.isChecked = sharedPref.getBoolean("autoLog", true)
        }else {
            autoLog.isChecked = true
            editor.putBoolean("autoLog", true)
            editor.apply()
        }

        autoLog.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
               //marcar que l'user vol guardar la contrasenya
                //marcar a prefferense i posar el autologin a login activity comprovant preference
                editor.putBoolean("autoLog", true)
                editor.apply()

            }else{
                //marcar a preference que no la vol guardar i posar la contra i el mail sempre per
                //iniciar la sessió
                editor.putBoolean("autoLog", false)
                editor.apply()
            }

        }

        logOutButton.setOnClickListener(){
            presenter.logOutAccount()

            val i = Intent(this, WelcomeActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.putExtra("EXIT", true)
            startActivity(i)
            finish()

        }

        callNumberButton.setOnClickListener() {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:123456789")
            startActivity(callIntent)
        }

        changePersInf.setOnClickListener(){
            val intentI = Intent(this, ComplementaryFormActivity::class.java)
            startActivity(intentI)
        }

        changeBankInf.setOnClickListener(){
            val intentI = Intent(this, CreditCardsActivity::class.java)
            startActivity(intentI)
        }
        buttonSendMail.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf("motorent@gmail.com")
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Assistencia a Motorent")
            //intent.putExtra(Intent.EXTRA_TEXT, "Body of the content here...")
            intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com")
            intent.type = "text/html"
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send mail"))
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intentI = Intent(this, MapsActivity::class.java)
        startActivity(intentI)
        finish()
    }

}