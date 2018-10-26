import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
public class MainActivity extends AppCompatActivity {
private final String USERNAME = &quot;username&quot;;
private final String PASSWORD = &quot;pass&quot;;
private final String NEW_USERNAME = &quot;new-username&quot;;
private final String NEW_PASSWORD = &quot;new-pass&quot;;
private final String URL_SERVER = &quot;https://first-test2dec.herokuapp.com/&quot;;
private EditText username_et;
private EditText password_et;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
username_et = (EditText) findViewById(R.id.editTextUsername);
password_et = (EditText) findViewById(R.id.editTextPassword);
}
// Кнопка Войти
public void goEnter(View view) {
new AsyncRequest().execute(username_et.getText().toString(),
password_et.getText().toString(), USERNAME);
}
// Кнопка Регистрация
public void newUser(View view) {
new AsyncRequest().execute(username_et.getText().toString(),
password_et.getText().toString(), NEW_USERNAME);
}
public void showResults(String s) {
Intent intent = new Intent(this, ResultsActivity.class);intent.putExtra(&quot;json&quot;, s);
startActivity(intent);
}
class AsyncRequest extends AsyncTask&lt;String, Integer, String&gt; {
@Override
protected String doInBackground(String... arg) {
HashMap&lt;String, String&gt; hm = new HashMap&lt;&gt;();
if (arg[2].equals(USERNAME)) {
hm.put(USERNAME, arg[0]);
hm.put(PASSWORD, arg[1]);
} else {
hm.put(NEW_USERNAME, arg[0]);
hm.put(NEW_PASSWORD, arg[1]);
}
return performPostCall(hm);
}
@Override
protected void onPostExecute(String s) {
super.onPostExecute(s);
String wrong = &quot;wrong password or username&quot;;
String userExists = &quot;user with this login already exists&quot;;
if (s.equals(wrong))
Toast.makeText(MainActivity.this, wrong, Toast.LENGTH_LONG).show();
else
if (s.equals(userExists))
Toast.makeText(MainActivity.this, userExists,
Toast.LENGTH_LONG).show();
else
showResults(s);
}
public String performPostCall(HashMap&lt;String, String&gt; postDataParams) {
URL url;
String response = &quot;&quot;;
try {
url = new URL(URL_SERVER);
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setReadTimeout(15000);
conn.setConnectTimeout(15000);
conn.setRequestMethod(&quot;POST&quot;);
conn.setDoInput(true);
conn.setDoOutput(true);
OutputStream os = conn.getOutputStream();
BufferedWriter writer = new BufferedWriter(
new OutputStreamWriter(os, &quot;UTF-8&quot;));
writer.write(getPostDataString(postDataParams));
writer.flush();
writer.close();os.close();
int responseCode = conn.getResponseCode();
if (responseCode == HttpsURLConnection.HTTP_OK) {
String line;
BufferedReader br = new BufferedReader(new
InputStreamReader(conn.getInputStream()));
while ((line = br.readLine()) != null) {
response += line;
}
} else {
response = &quot;Err&quot;;
}
} catch (Exception e) {
e.printStackTrace();
response = e.toString();
}
return response;
}
private String getPostDataString(HashMap&lt;String, String&gt; params) throws
UnsupportedEncodingException {
StringBuilder result = new StringBuilder();
boolean first = true;
for (Map.Entry&lt;String, String&gt; entry : params.entrySet()) {
if (first)
first = false;
else
result.append(&quot;&amp;&quot;);
result.append(URLEncoder.encode(entry.getKey(), &quot;UTF-8&quot;));
result.append(&quot;=&quot;);
result.append(URLEncoder.encode(entry.getValue(), &quot;UTF-8&quot;));
}
return result.toString();
}}}
