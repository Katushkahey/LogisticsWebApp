<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>

<head>
    <title>Logistics</title>

    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <style>
        html {
            height: 100%; /* Высота страницы */
        }

        body {
            background: #95b7a6 url("../images/background.jpg"); /* Цвет фона и путь к файлу */
            background-position: center;
            background-repeat: no-repeat;
            background-size: contain;
            color: #000; /* Цвет текста */
        }

        .mainbox {
            position: absolute;
            right: 42%;
            bottom: 10%;
            opacity: 0.9;
        }

        .error-message {
            color: #ee2e10;
        }
    </style>
</head>
<body>
<% String message = "";
    if (request.getParameter("error") != null) {
        message = "Invalid login or password";
    }
%>
<div id="loginbox" style="..." class="mainbox col-md-2 col-md-offset-1 col-sm-3 col-sm-offset-1">
    <div class="panel panel-info">
        <div class="panel-heading">
            <div class="panel-title">Sign In</div>
        </div>
        <div style="..." class="panel-body">
            <form action="/authenticateTheUser" method="post" class="form-horizontal">
                <div style="..." class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                    <input type="text" name="username" placeholder="username" class="form-control">
                </div>
                <div style="..." class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                    <input type="password" name="password" placeholder="password" class="form-control">
                </div>
                <div style="..." class="form-group">
                    <div class="col-sm-6 controls">
                        <button type="submit" class="btn btn-success">Login</button>
                    </div>
                </div>
                <div class="error-message"><%=message%>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>