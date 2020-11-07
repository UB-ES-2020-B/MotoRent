package ub.es.motorent.app.presenter

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.app.view.FullScreenActivity
import ub.es.motorent.app.view.ResetPswdActivity

class ResetPswdPresenter (var activity: ResetPswdActivity) : FullScreenActivity(){

    private var auth: FirebaseAuth = Firebase.auth

    fun sendMail(email: String){
        if(email != ""){
            val auth = FirebaseAuth.getInstance()
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        customToast("El mail s'ha enviat correctament", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnCanceledListener { customToast("No tenim registrat el mail", Toast.LENGTH_LONG).show() }
        }else{
            customToast("Ompla el camp amb l'email", Toast.LENGTH_LONG).show()
        }
    }


}