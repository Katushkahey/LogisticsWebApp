<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<head>
    <title> Order assignment: truck and drivers </title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <style>
        html {
            height: 100%; /* Высота страницы */
        }

        body {
            /* путь к файлу */
            background: rgb(244, 244, 234) url('${pageContext.request.contextPath}/images/error.jpg') no-repeat right;
            background-size: contain;
            color: #000;
        }

        .mainDiv {
            width: 50%;
            height: 400px;
            overflow-y: auto;
            overflow-x: auto;
            margin-left: 1rem;
        }

        .btnBack {
            margin-left: 1rem;
        }
    </style>
</head>
<body>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
</br>
<input type=button class="btnBack btn-secondary" value=" Back " onCLick="history.back()">
</br></br>
</body>
</html>
