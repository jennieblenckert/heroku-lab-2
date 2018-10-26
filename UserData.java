import java.util.HashMap;
public class UserData extends HashMap&lt;String, String&gt; {
public static final String USERNAME = &quot;username&quot;;
public static final String LOGIN_TIME = &quot;logintime&quot;;
// Конструктор
public UserData(String username, String logintime) {
super.put(USERNAME, username);
super.put(LOGIN_TIME, logintime);
}}
