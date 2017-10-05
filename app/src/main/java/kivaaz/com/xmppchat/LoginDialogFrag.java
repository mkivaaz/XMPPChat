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
import android.widget.TextView;

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

        final TextView Username = rootView.findViewById(R.id.usernameET);
        final TextView Password = rootView.findViewById(R.id.PassET);

        Button LoginBtn = rootView.findViewById(R.id.loginBtn);
        Button CancelBtn = rootView.findViewById(R.id.cancelBtn);
        Button ClearBtn = rootView.findViewById(R.id.clearBtn);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = preferences.edit();


        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat = new Chat();
                Chat.sender = Username.getText().toString();
                MyService.USERNAME = Username.getText().toString();
                MainActivity activity = (MainActivity) getActivity();
                activity.doBindService();
                editor.putString("Username","Username.getText().toString()");
                editor.apply();
                dismiss();
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
