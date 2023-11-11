package com.orders.aydivedics.ui.login;

import static android.content.Context.MODE_PRIVATE;

import static com.orders.aydivedics.Utility.BASE_URL;
import static com.orders.aydivedics.Utility.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.SharedPreferences;
import android.util.Patterns;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orders.aydivedics.MySingleton;
import com.orders.aydivedics.Utility;
import com.orders.aydivedics.data.LoginRepository;
import com.orders.aydivedics.data.Result;
import com.orders.aydivedics.data.model.LoggedInUser;
import com.orders.aydivedics.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;


    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job

        try {
          networkCall(username,password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private void networkCall(String username, String password) throws JSONException {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"appLogin.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Toast.makeText(Utility.activity,response,Toast.LENGTH_LONG).show();
                            try {
                                JSONObject userDetails = new JSONObject(response);
//                                Toast.makeText(Utility.activity, userDetails.getString("status"), Toast.LENGTH_SHORT).show();
                                String status = userDetails.getString("status");
                                if (status.equals("success")){
                                    String userId = userDetails.getString("userId");
                                    String displayName = userDetails.getString("displayName");
                                    String userType = userDetails.getString("userType");
                                    LoggedInUser loggedInUser = new LoggedInUser(userId,displayName,userType);

                                    SharedPreferences sp= activity.getSharedPreferences("Login", MODE_PRIVATE);
                                    SharedPreferences.Editor Ed=sp.edit();
                                    Ed.putString("username",username );
                                    Ed.putString("password",password);
                                    Ed.putString("userId",userId);
                                    Ed.putString("userType",userType);
                                    Ed.commit();

                                    Result<LoggedInUser> result = loginRepository.login(username, password);

                                    if (result instanceof Result.Success) {
                                        LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                                        loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                                    } else {
                                        loginResult.setValue(new LoginResult(R.string.login_failed));
                                    }
                                }
                                else{
                                    Toast.makeText(Utility.activity, "failed login", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Utility.activity,error.toString(),Toast.LENGTH_LONG).show();

                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("username",username);
                    params.put("password",password);
                    return params;
                }

            };

            MySingleton.getInstance(Utility.activity).addToRequestQueue(stringRequest);


    }
}