&lt;?php
//error_reporting(E_ALL);
//ini_set(&quot;display_errors&quot;, 1);
$connection = new PDO(&#39;pgsql:host=ec2-54-228-213-36.eu-west-1.compute.amazonaws.com
dbname=da37eh2ap5epo9 user=kadwtexnmwbhzo password=1JP2Vdv3EzBxJ_VRDdP9fp01UP&#39;);
if (!$connection) {
echo &quot;no connect&quot;;
} else {
//вход под существующем пользователем
if (isset($_POST[&#39;username&#39;]) &amp;&amp; isset($_POST[&#39;pass&#39;])) {
$responceApp = new User_Data($_POST[&#39;username&#39;], $_POST[&#39;pass&#39;], $connection);
echo $responceApp-&gt;getUserData();
}
//создание нового пользователя
if (isset($_POST[&#39;new-username&#39;]) &amp;&amp; isset($_POST[&#39;new-pass&#39;])) {
$responceApp = new User_Data($_POST[&#39;new-username&#39;], $_POST[&#39;new-pass&#39;], $connection);
echo $responceApp-&gt;setNewUser();
}
}
// класс для обработки полученных данных
class User_Data
{
private $username;
private $password;
private $connection;
private $logintime;
function User_Data($username, $password, $connection)
{
$this-&gt;password = md5($password . &quot;salt=)&quot;);
$this-&gt;username = $username;
$this-&gt;connection = $connection;
$this-&gt;logintime = date(&#39;U&#39;);
}
function getUserData()
{if ($this-&gt;userIsset()) {
$query = $this-&gt;connection-&gt;query(&quot;UPDATE users SET logintime=&#39;$this-&gt;logintime&#39;
WHERE username=&#39;$this-&gt;username&#39; AND password=&#39;$this-&gt;password&#39; &quot;);
$query = $this-&gt;connection-&gt;query(&quot;SELECT id, username, password, logintime FROM
users ORDER BY logintime DESC &quot;);
$rows = array();
foreach ($query as $r) {
$rows[] = $r;
}
return json_encode($rows);
} else {
$wrong = &quot;wrong password or username&quot;;
return $wrong;
}
}
function userIsset()
{
$search_user = $this-&gt;connection-&gt;query(&quot;SELECT id FROM users WHERE username=&#39;$this-
&gt;username&#39; AND password=&#39;$this-&gt;password&#39;&quot;);
$user_valid = $search_user-&gt;fetchColumn();
if ($user_valid &gt; 0)
return true;
else
return false;
}
function loginIsset()
{
$search_user = $this-&gt;connection-&gt;query(&quot;SELECT id, username, password, logintime FROM
users WHERE username=&#39;$this-&gt;username&#39;&quot;);
$user_valid = $search_user-&gt;fetchColumn();
if ($user_valid &gt; 0)
return true;
else
return false;
}
function setNewUser()
{
if (!$this-&gt;loginIsset()) {
$add_user = $this-&gt;connection-&gt;query(&quot;INSERT INTO users (username, password,
logintime) VALUES (&#39;$this-&gt;username&#39;, &#39;$this-&gt;password&#39;, &#39;$this-&gt;logintime&#39;);&quot;);
return $this-&gt;getUserData();
} else {
$userExists = &quot;user with this login already exists&quot;;
return $userExists;
}}}
?>
