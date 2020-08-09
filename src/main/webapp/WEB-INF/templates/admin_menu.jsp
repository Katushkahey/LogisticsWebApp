<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<style>
    html {
        height: 100%; /* Высота страницы */
    }

    body {
        background: #e7ecf0 url("../images/manager.jpg"); /* путь к файлу */
        background-position: bottom;
        background-repeat: no-repeat;
        background-size: auto;
        color: #000;
    }

    .nav-item {
        position: relative;
        left: 55em ;
    }
</style>
<head>
    <meta charset="utf-8">
    <title>Admins-info</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</head>
<body>
<div>
    <nav class="navbar navbar-expand-lg navbar-light" style="background: #d1d9dc">
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a class="nav-link active" href="/admin/"><strong><u>Главная </u></strong><span
                        class="sr-only">(current)</span></a>
                <a class="nav-link" href="/truck/info"><strong> Фуры </strong></a>
                <a class="nav-link" href="/drivers/info"><strong> Водители </strong></a>
                <a class="nav-link" href="/order/info"><strong> Заказы </strong></a>
                <a class="nav-item">
                    <form action="/logout" method="post">
                        <input type="submit" class="btn btn-danger" value="Logout"/>
                    </form>
                </a>
            </div>
        </div>
    </nav>
</div>
</body>
</html>