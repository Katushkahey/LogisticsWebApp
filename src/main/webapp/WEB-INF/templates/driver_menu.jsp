<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<head>
    <meta charset="utf-8" lang="ru">
    <title>Drivers-info</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <style>
        html {
            height: 100%; /* Высота страницы */
        }

        body {
            background: rgb(247, 205, 145) url("../images/driver_page.jpg"); /* путь к файлу */
            background-position: right;
            background-repeat: no-repeat;
            background-size: contain;
            color: #000; /* Цвет текста */
        }

        .navbar {
            width: 50%;
            margin-left: 1rem;
        }

        .p-4 {
            height: 250px;
            width: 50%;
            overflow-x: auto;
            margin-left: 1rem;
        }

        .info {
            margin-left: 1rem;
        }

        .form-group {
            margin-left: 1rem;
            width: 50%;
        }

        .form-row {
            margin-left: 1rem;
            width: 50%;
        }

    </style>
</head>
<body>
<nav class="navbar2 navbar-light" style="background: rgba(67,41,28,0.99)">
    <a class="navbar-brand">
        <img src="../images/man_icon.png" width="50" height="50" class="d-inline-block align-top" alt="" loading="lazy">
        <span class="text-white">${name} ${surname}, ${telephoneNumber}</span>
    </a>
</nav>
</br>
<div class="form-group form-check">
    <input type="checkbox" class="form-check-input" id="exampleCheck1">
    <label class="form-check-label" for="exampleCheck1"><strong> Начал работу </strong></label>
</div>
<form>
    <div class="form-row align-items-center">
        <div class="col-auto my-1">
            <label class="mr-sm-2 sr-only" for="inlineFormCustomSelect"></label>
            <select class="custom-select mr-sm-2" id="inlineFormCustomSelect">
                <option selected>Status...</option>
                <option value="1">За рулем</option>
                <option value="2">Второй водитель</option>
                <option value="3">Погрузочно-разгрузочные работыe</option>
                <option value="4">Отдых</option>
            </select>
        </div>
    </div>
</form>
<div class="form-group form-check">
    <input type="checkbox" class="form-check-input" id="exampleCheck2">
    <label class="form-check-label" for="exampleCheck2"><strong>Закончил работу</strong></label>
</div>
</br>
<div class="info"><h5><strong>Partner:</strong> ${partner.name} ${partner.surname}, ${partner.telephoneNumber}</h5>
</div>
</br>
<div class="info"><h5><strong>Truck:</strong> ${truck}</h5></div>
</br>
<div class="collapse" id="navbarToggleExternalContent">
    <div class="p-4" style="background: rgba(67,41,28,0.99)">
        <h5 class="text-white h4" align="center">Waypoints of order №${order}</h5>
        <span class="text-white">
            <table class="table">
                <thead class="thead-light" align="center">
                        <tr>
                            <th scope="col"> City </th>
                            <th scope="col"> Cargo </th>
                            <th scope="col"> Action </th>
                            <th scope="col">Status</th>
                        </tr>
                </thead>
                <tbody align="center">
                    <c:forEach var="waypoint" items="${waypoints}">
                        <tr>
                            <td scope="row"> ${waypoint.city.name} </td>
                            <td scope="row"> ${waypoint.cargo.name} </td>
                            <td scope="row"> ${waypoint.action.name()} </td>
                            <td>
                                <div class="input-group-prepend">
                                    <div class="input-group-text">
                                        <input type="checkbox" aria-label="Checkbox for following text input">
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </span>
    </div>
</div>
<nav class="navbar navbar-light" style="background: rgba(67,41,28,0.99)">
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarToggleExternalContent"
            aria-controls="navbarToggleExternalContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
        <span class="text-white">Waypoints of order №${order}</span>
    </button>
</nav>
</body>
</html>