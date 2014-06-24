package demo.ufgbipperv2.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements OnItemSelectedListener {

    private ProgressDialog mDialog;
    EditText fieldEmail;
    Button btnSignUp;
    String selectedCourse;
    Spinner spinner;
    JSONHandler jsonParser = new JSONHandler();
    private static String url_assign_course = "http://192.168.1.2/ubp/assign_course.php";
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        // Botoes
        fieldEmail = (EditText) findViewById(R.id.email);
        btnSignUp = (Button) findViewById(R.id.signUp);

        // Spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        // Apenas alguns cursos (teste)
        List<String> courses = new ArrayList<String>();
        courses.add("Engenharia de Software");
        courses.add("Sistemas de Informacao");
        courses.add("Ciencia da Computacao");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courses);
        spinner.setAdapter(arrayAdapter);

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new makeEnrollment().execute();
                /*Intent intent = new Intent(getApplicationContext(), MessagesActivity.class);
                startActivity(intent);*/
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedCourse = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), "Curso Selecionado: " + selectedCourse, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    class makeEnrollment extends AsyncTask<String, String, String> {

         // Feedback para o usuario
        @Override
        /*protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getBaseContext());
            pDialog.setMessage("Enviando para o servidor..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }*/

        protected String doInBackground(String... args) {
            String email = fieldEmail.getText().toString();

            // Variáveis para envio dos dados e-mail e curso
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("course", selectedCourse));

            // Requisição HTTP POST para as mensagens armazenadas no banco de dados do servidor
            JSONObject json = jsonParser.makeHttpRequest(url_assign_course,
                    "POST", params);

            //Log.d("Resposta: ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // Se a criaçao no banco de dados foi bem sucedida
                    //Toast.makeText(getBaseContext(), "Cadastro feito com sucesso!", Toast.LENGTH_LONG).show();
                    Log.v("MainActivity", "Sucesso na criação do banco de dados.");
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        // Chamada para o fechamento do diálogo de feedback
        /*protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }*/
    }
}
