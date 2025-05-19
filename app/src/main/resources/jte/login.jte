@params(error: boolean = false, logout: boolean = false)

<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h2>Login</h2>

<form action="/login" method="post">
    <label>Username: <input type="text" name="username"/></label><br/>
    <label>Password: <input type="password" name="password"/></label><br/>
    <input type="submit" value="Login"/>
</form>

@if(error) {
    <p style="color:red;">Invalid username or password</p>
}
@if(logout) {
    <p style="color:green;">You have been logged out</p>
}
</body>
</html>
