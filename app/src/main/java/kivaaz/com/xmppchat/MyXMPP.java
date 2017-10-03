package kivaaz.com.xmppchat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;



/**
 * Created by Muguntan on 9/27/2017.
 */

public class MyXMPP {
    public static boolean connected =false;
    public boolean loggedin = false;
    public static boolean isConnecting = false;
    public static boolean isToasted = true;
    private boolean chat_created = false;
    private String serverAddress;
    public  static XMPPTCPConnection connection;
    private static String loginUser;
    private static String passwordUser;
    Gson gson;
    MyService context;
    public static MyXMPP instance = null;
    public static boolean instanceCreated = false;


    public MyXMPP(MyService context,String serverAddress, String loginUser, String passwordUser) {
        this.serverAddress = serverAddress;
        this.context = context;
        this.loginUser = loginUser;
        this.passwordUser = passwordUser;
        init();
    }

    public static MyXMPP getInstance(MyService context, String server, String user,String pass){
        if(instance==null){
            instance = new MyXMPP(context, server, user, pass);
            instanceCreated = true;
        }
        return instance;
    }

    public org.jivesoftware.smack.chat.Chat myChat;

    ChatManagerListenerImpl chatManagaerListenerImp;
    MMessageListener MmessageListener;

    String text = "";
    String message = "", receiver = "";
    static{
        try{
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        gson = new Gson();
        MmessageListener = new MMessageListener(context);
        chatManagaerListenerImp = new ChatManagerListenerImpl();
        iniatializeConnection();
    }

    private void iniatializeConnection() {
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setServiceName(serverAddress)
                .setHost(serverAddress)
                .setPort(5222)
                .setDebuggerEnabled(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);
        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
        connection = new XMPPTCPConnection(config.build());
        XMPPConnectionListener connectionListener = new XMPPConnectionListener();
        connection.addConnectionListener(connectionListener);
    }

    public void disconnect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        });
    }

    public void connect(final String caller) {
        final AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                if (connection.isConnected()) {
                    return false;
                }
                isConnecting = true;
                if (isToasted) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, caller + "=> CONNECTING.....", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.d("Connect() Function", caller + "=> CONNECTING...");

                    try {
                        connection.connect();
                        DeliveryReceiptManager dm = DeliveryReceiptManager.getInstanceFor(connection);
                        dm.setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
                        dm.addReceiptReceivedListener(new ReceiptReceivedListener() {
                            @Override
                            public void onReceiptReceived(String fromJid, String toJid, String receiptId, Stanza receipt) {

                            }
                        });
                        connected = true;
                    } catch (SmackException e) {
                        if (isToasted) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "(" + caller + ") SmackException", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.d("(" + caller + ")", "SmackException " + e.getMessage());
                        }
                    } catch (final IOException e) {
                        if (isToasted) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "(" + caller + ") IOException", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.d("(" + caller + ")", "IOException " + e.getMessage());
                        }
                    } catch (XMPPException e) {
                        if (isToasted) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "(" + caller + ") XMPPException", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.d("(" + caller + ")", "XMPPException " + e.getMessage());
                        }

                    }


                }
                return isConnecting = false;
            }
        };
        connectionThread.execute();
    }

    private void Login() {
        try {
            connection.login(loginUser,passwordUser);
            Log.i("LOGIN","LOGGED IN SUCCESSFULLY");

        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void sendMessage(final ChatMessage chatMessage){
        String body = gson.toJson(chatMessage);

        if(!chat_created){
            myChat = ChatManager.getInstanceFor(connection).createChat("Kivaaz" + "@jabber.network", MmessageListener);
            myChat.addMessageListener(MmessageListener);
            chat_created = true;
        }

        final Message message = new Message();
        message.setBody(body);
        message.setStanzaId(chatMessage.msgid);
        message.setType(Message.Type.chat);

        try {
            if(connection.isAuthenticated()){
                myChat.sendMessage(chatMessage.body);
            }
            else{
                Login();
            }
        } catch (SmackException.NotConnectedException e) {
            Log.d("XMPP.SendMessage()","Msg Not Sent - Not Connected");
        } catch (Exception e){
            Log.d("XMPP.SendMessage()", "Msg Not Sent " + e.getMessage());
        }


    }

    public boolean isJSON(String s){
        String firstChar = String.valueOf(s.charAt(0));
        if (firstChar.equalsIgnoreCase("{")){
            return true;
        }
        else
            return false;

    }





    //                            CLASS CREATION BELOW HERE
    public class MMessageListener implements ChatMessageListener {


        public MMessageListener(Context c) {
        }

        @Override
        public void processMessage(final org.jivesoftware.smack.chat.Chat chat, final Message message) {
            Log.i("MyXMPP_MESSAGE_LISTENER","XMPP Message Received: " + message);


            if(message.getType() == Message.Type.chat && message.getBody() != null){
                Boolean isJSON = isJSON(message.getBody());
                final ChatMessage chatMessage;
                if (!isJSON){
                    ChatMessage chatMessage1 = new ChatMessage("Kivaaz","Wakerz",message.getBody(),"",false);
                    //{"Date":"3 Oct 2017","Time":"6:45pm","body":"yo","isMine":true,"msgid":"181-74","receiver":"Wakerz","sender":"Wakerz","senderName":"Wakerz"}
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("Date",CommonMethods.getCurrentDate());
                        jsonObject.put("Time",CommonMethods.getCurrentDate());
                        jsonObject.put("body",message.getBody());
                        jsonObject.put("isMine", false);
                        jsonObject.put("msgid","");
                        jsonObject.put("receiver","Wakerz");
                        jsonObject.put("sender","Kivaaz");
                        jsonObject.put("senderName","Kivaaz");

                        chatMessage = gson.fromJson(jsonObject.toString(),ChatMessage.class);
                        processMessage(chatMessage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else {

                    chatMessage = gson.fromJson(message.getBody(), ChatMessage.class);
                    processMessage(chatMessage);
                }

            }
        }

        private void processMessage(final ChatMessage chatMessage) {
            chatMessage.isMine = false;
            kivaaz.com.xmppchat.Chat.chatlist.add(chatMessage);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    kivaaz.com.xmppchat.Chat.chatAdapter.notifyDataSetChanged();
                }
            });
        }


    }

    public class ChatManagerListenerImpl implements ChatManagerListener {
        @Override
        public void chatCreated(org.jivesoftware.smack.chat.Chat chat, boolean createdLocally) {
//            if(!createdLocally)
                chat.addMessageListener(MmessageListener);
        }
    }

    public class XMPPConnectionListener implements ConnectionListener{

        @Override
        public void connected(XMPPConnection connection) {
            Log.d("xmpp", "Connceted");
            connected = true;
            if (!connection.isAuthenticated()){
                Login();
            }
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            Log.d("XMPP","AUTHENTICATED");
            loggedin = true;

            ChatManager.getInstanceFor(connection).addChatListener(chatManagaerListenerImp);

            chat_created = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            if (isToasted){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"CONNECTED",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public void connectionClosed() {
            if (isToasted){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"CONNECTION CLOSED",Toast.LENGTH_SHORT).show();
                    }
                });

            }
            Log.d("xmpp", "ConnectionCLosed!");
            connected = false;
            chat_created = false;
            loggedin = false;
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            if(isToasted){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"CONNECTION ERROR",Toast.LENGTH_SHORT).show();
                    }
                });

                Log.d("XMPP","CONNECTION CLOSED ON ERROR");


            }
            connected = false;
            chat_created=false;
            loggedin=false;
        }

        @Override
        public void reconnectionSuccessful() {
            if(isToasted){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"RECONNECTION SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    }
                });


            }
            Log.d("XMPP", "RECONNECTION SECCESFUL");
            connected = true;
            chat_created = false;
            loggedin = false;
        }

        @Override
        public void reconnectingIn(int seconds) {
            Log.d("XMPP","RECONNECTING "+ connection.getParsingExceptionCallback());

            loggedin = false;
        }

        @Override
        public void reconnectionFailed(Exception e) {
            if (isToasted){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"RECONNECTION FAILED",Toast.LENGTH_SHORT).show();
                    }
                });


            }
            Log.d("XMPP","RECONNECTION FILED DUE TO "+ e);

            connected = false;
            chat_created = false;
            loggedin = false;
        }
    }




}
