package kivaaz.com.xmppchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Muguntan on 9/27/2017.
 */

public class Chat extends Fragment implements View.OnClickListener {

    private EditText Message;



    public static String sender  , receiver;
    private Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chat_layout,container,false);
        random =new Random();

        Message =(EditText) view.findViewById(R.id.msgET);
        msgListView = view.findViewById(R.id.chat_messages);
        ImageButton sendButton = view.findViewById(R.id.sendBtn);
        sendButton.setOnClickListener(this);

        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist = new ArrayList<ChatMessage>();
        chatAdapter = new ChatAdapter(getActivity(),chatlist);
        msgListView.setAdapter(chatAdapter);




        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            if(MainActivity.isLogin){
                if (!MainActivity.isReceiverExists) {

                    FragmentManager fragmentManager = getFragmentManager();
                    ChooseUserDialog loginDialogFrag = new ChooseUserDialog();

                    loginDialogFrag.show(fragmentManager, "Enter Receiver's Username");
                    MainActivity.isReceiverExists = true;
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    public void SendMessage(View v){
        String message = Message.getEditableText().toString();
        if (!message.equalsIgnoreCase("")){
            final ChatMessage chatMessage = new ChatMessage(sender,receiver,message,""+random.nextInt(1000),true);
            chatMessage.setMsgID();
            chatMessage.body= message;
            chatMessage.Date = CommonMethods.getCurrentDate();
            chatMessage.Time = CommonMethods.getCurrentTime();
            Message.setText("");
            chatAdapter.add(chatMessage);
            chatAdapter.notifyDataSetChanged();
            MainActivity activity = (MainActivity) getActivity();
            activity.getmService().xmpp.sendMessage(chatMessage);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendBtn:
                SendMessage(view);
        }
    }
}
