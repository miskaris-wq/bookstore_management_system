@params(username: String)

<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
</head>
<body>
    <h2>Welcome, @{username}!</h2>
    <p>You are successfully logged in.</p>

    <form action="/logout" method="post">
        <button type="submit">Logout</button>
    </form>
</body>
</html>
