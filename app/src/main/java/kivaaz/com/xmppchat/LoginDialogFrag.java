package kivaaz.com.xmppchat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by Muguntan on 10/5/2017.
 */

public class LoginDialogFrag extends DialogFragment {
    SharedPreferences sharedpreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_dialog_frag,container,false);
        getDialog().setTitle("Login");

        final EditText Username = rootView.findViewById(R.id.usernameET);
        final EditText Password = rootView.findViewById(R.id.PassET);

        RadioGroup AuthGroup = rootView.findViewById(R.id.AuthRadioGrp);




        final Button LoginBtn = rootView.findViewById(R.id.loginBtn);
        final Button SignUpBtn = rootView.findViewById(R.id.SignUpBtn);
        Button ClearBtn = rootView.findViewById(R.id.clearBtn);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = preferences.edit();

        AuthGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if ( i == R.id.LoginRB){
                    LoginBtn.setVisibility(View.VISIBLE);
                    SignUpBtn.setVisibility(View.GONE);
                    MyXMPP.AccountExists = true;
                }else {
                    LoginBtn.setVisibility(View.GONE);
                    SignUpBtn.setVisibility(View.VISIBLE);
                    MyXMPP.AccountExists = false;
                }
            }
        });

        RadioButton LoginRb = rootView.findViewById(R.id.LoginRB);
        RadioButton SignRb = rootView.findViewById(R.id.SignRB);
        LoginRb.setChecked(true);
//        LoginRb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginBtn.setVisibility(View.VISIBLE);
//                SignUpBtn.setVisibility(View.GONE);
//                MyXMPP.AccountExists = true;
//            }
//        });
//
//        SignRb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                                    LoginBtn.setVisibility(View.GONE);
//                    LoginBtn.setVisibility(View.VISIBLE);
//                    MyXMPP.AccountExists = false;
//            }
//        });

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat = new Chat();
                Chat.sender = Username.getText().toString();
                MyService.USERNAME = Username.getText().toString();
                MyService.PASSWORD = Password.getText().toString();
                MainActivity activity = (MainActivity) getActivity();
                activity.doBindService();
                editor.putString("Username",Username.getText().toString());
                editor.commit();
                dismiss();

            }
        });

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat = new Chat();
                Chat.sender = Username.getText().toString();
                MyService.USERNAME = Username.getText().toString();
                MyService.PASSWORD = Password.getText().toString();
                MainActivity activity = (MainActivity) getActivity();
                activity.doBindService();
                editor.putString("Username",Username.getText().toString());
                editor.commit();
                dismiss();
            }
        });



        ClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Username.setText("");
                Password.setText("");
            }
        });

        return rootView;


    }


}
