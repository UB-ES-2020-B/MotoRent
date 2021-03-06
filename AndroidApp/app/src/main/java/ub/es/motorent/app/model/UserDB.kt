package ub.es.motorent.app.model

import android.util.Log


data class UserInfo (
    var id: Int? = null,
    var id_bank_data: Int? = null,
    var national_id_document: String? = null,
    var mail: String?,
    var google_token: String?,
    var role: Int?,
    var country: String? = null,
    var name: String? = null,
    var surname: String? = null
)
data class UserJson (
    var user: UserInfo
)
data class UserList (
    var users : List<UserInfo>
)

object UserDB {

    fun getUsers() {
        val apiService = RestApiService()
        apiService.getUsers() {
            Log.i(TAG, it.toString())
        }
        return@getUsers
    }

    fun getUserByIdOrGoogleToken(id: Int? = null, google_token: String? = null, onResult: (UserInfo?) -> Unit) {
        val apiService = RestApiService()
        apiService.getUserByIdOrGoogleToken(id, google_token) {
            Log.i(TAG, it.toString())
            if (it != null) {
                onResult(it.user)
            } else {
                onResult(null)
            }
        }
    }

    fun registerUser(email: String?, gToken: String, role: Int = 0, onRegistered: (UserInfo?) -> Unit) {
        val apiService = RestApiService()
        apiService.addUser(email, gToken, role) {
            Log.i(TAG, it.toString())
            if (it?.user?.id == null){
                getUserByIdOrGoogleToken(google_token = gToken){user ->
                    onRegistered(user)
                }
            } else {
                onRegistered(it.user)
            }
        }
    }

    fun updateUserInfoInDataBase(id: Int, email: String? = null, google_token: String? = null,
                                 role: Int? = null, name: String?, surname: String?, country: String?,
                                 id_bank_data: Int? = null, national_id_document: String? = null, onResult: (UserJson?) -> Unit){
        val apiService = RestApiService()
        apiService.updateUser(id, name, surname, national_id_document, country, email, google_token,
                              role, id_bank_data ) {
            //Log.i(TAG, it.toString())
            onResult(it)
        }
    }

    fun deleteUser(id: Int) {
        val apiService = RestApiService()
        apiService.deleteUser(id) {
            Log.i(TAG, it.toString())
        }
    }

    private const val TAG = "Retrofit UserDB"

}