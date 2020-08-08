<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>

<head>
    <title>Cargoes</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</head>
<body>
<div>
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: #d1d9dc">
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a class="nav-link" href="/admin/"><strong>Главная </strong></a>
                <a class="nav-link" href="/admin/trucks-info"><strong>Фуры </strong></a>
                <a class="nav-link" href="/admin/drivers-info"><strong>Водители </strong></a>
                <a class="nav-link" href="/admin/orders-info"><strong>Заказы </strong></a>
                <a class="nav-link active" href="/admin/cargoes-info"><strong><u>Грузы </u></strong><span
                        class="sr-only">(current)</span>
                </a>
            </div>
        </div>
    </nav>
</div>
<h1>Вы попали на страницу с грузами</h1>
</body>
</html>
