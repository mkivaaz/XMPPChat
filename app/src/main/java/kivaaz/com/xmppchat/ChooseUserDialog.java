package kivaaz.com.xmppchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Muguntan on 10/7/2017.
 */

public class ChooseUserDialog extends DialogFragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.choose_receiver,container,false);
        getDialog().setTitle("Enter Receiver's Username");

        final EditText Username = rootView.findViewById(R.id.usernameET);

        Button LoginBtn = rootView.findViewById(R.id.StartBtn);
        Button ClearBtn = rootView.findViewById(R.id.clearBtn);
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat.receiver = Username.getText().toString();
                MyXMPP.Receiver = Username.getText().toString();
                dismiss();
            }
        });


        return rootView;
    }


}
