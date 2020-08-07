<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<style>
    .image {
        display: block;
        margin: auto;
    }

    .image-container {
        background: #f6fbff;
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
                <a class="nav-link active" href="/admin/"><strong> Главная </strong><span
                        class="sr-only">(current)</span></a>
                <a class="nav-link" href="/admin/trucks-info"><strong> Фуры </strong></a>
                <a class="nav-link" href="/admin/drivers-info"><strong> Водители </strong></a>
                <a class="nav-link" href="/admin/orders-info"><strong> Заказы </strong></a>
                <a class="nav-link" href="/admin/cargoes-info"><strong> Грузы </strong></a>
            </div>
        </div>
    </nav>
</div>
<div class="image-container">
    <img src="../images/manager.jpg" height="637" class="image">
</div>
</body>
</html>