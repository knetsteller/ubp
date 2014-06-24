package demo.ufgbipperv2.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MessagesActivity extends Activity {
    // Variáveis para os nós do JSON
    private static final String TAG_SUCCESS = "success";
    private static final String MESSAGES = "messages";
    private static final String SENDER = "sender";
    private static final String DATE = "date";
    private static final String MESSAGE = "message";
    private static String url_messages = "http://192.168.1.2/ubp/messages.json"; // Máquina local

    // JSONHandler contém métodos para tratar do envio e recebimento HTTP
    JSONHandler jParser = new JSONHandler();
    ArrayList<HashMap<String, String>> messagesList;
    JSONArray messages = null;
    // DBHandler contém métodos para o gerenciamento do banco de dados no aplicativo
    private DBHandler dbHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        Log.d("oncreate", "onCreateMessageActivity");
        // Hashmap para a atribuição do padrão chave e valor do JSON
        messagesList = new ArrayList<HashMap<String, String>>();

        new MessageView().execute();
    }

    class MessageView extends AsyncTask<String, String, String> {
        // Feedback para o usuário
        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MessagesActivity.this);
            pDialog.setMessage("Aguarde...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }*/

        protected String doInBackground(String... args) {
            //
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            Log.d("jparser", "antes do jsonparser");
            JSONObject json = jParser.makeHttpRequest(url_messages, "GET", params);

            Log.d("Mensagens: ", json.toString());

            try {
                // Código HTTP para sucesso ou não no recebimento de dados
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // messages found
                    messages = json.getJSONArray(MESSAGES);
                    dbHandler = new DBHandler(getBaseContext());
                    dbHandler.open();

                    // Atribuição dos elementos JSON das mensagems
                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject c = messages.getJSONObject(i);

                        String sender = c.getString(SENDER);
                        String date = c.getString(DATE);
                        String message = c.getString(MESSAGE);

                        dbHandler.insertData(sender, date, message);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(SENDER, sender);
                        map.put(DATE, date);
                        map.put(MESSAGE, message);

                        messagesList.add(map);
                    }
                } else {
                    Log.v("doInBackground", "Sem Mensagens");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExcute(String string){
            // Inserção dos dados recebidos no banco de dados da aplicação
            Cursor cursor = dbHandler.returnData();
            String theSender = "";
            String theDate = "";
            String theMessage = "";

            if (cursor.moveToFirst()){
                do {
                    theSender = cursor.getString(0);
                    Log.v("theSender", theSender);
                    theDate = cursor.getString(1);
                    Log.v("theDate", theDate);
                    theMessage = cursor.getString(2);
                    Log.v("theMessage", theMessage);
                } while (cursor.moveToNext());
            }
        }
    }
}


