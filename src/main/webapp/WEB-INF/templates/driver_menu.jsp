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
            background: rgba(74, 71, 73, 0.46);
            color: #ffffff; /* Цвет текста */
        }

        .collapse {
            height: 300px;
            overflow-x: auto;
        }

        .collapse2 {
            height: 300px;
            overflow-x: auto;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-dark bg-dark">
    <a class="navbar-brand" href="#">
        <img src="../images/man_icon.png" width="50" height="50" class="d-inline-block align-top" alt="" loading="lazy">
        <span>${name}</span> <span>${surname}</span> TelephoneNumber: <span>${telephoneNumber}</span>
    </a>
</nav>
</br></br>
<div>
    <div class="collapse" id="navbarToggleExternalContent2">
        <div class="bg-dark p-4">
            <h5 class="text-white h4" align="center"> The team </h5>
            <span class="text-muted">
                <table class="table">
                    <thead class="thead-dark" align="center">
                            <tr>
                                <th scope="col"> Name </th>
                                <th scope="col"> Surname </th>
                                <th scope="col"> Telephone Number </th>
                            </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="partner" items="${partners}">
                            <tr>
                                <td scope="row" align="center"> ${partner.name} </td>
                                <td scope="row" align="center"> ${partner.surname} </td>
                                <td scope="row" align="center"> ${partner.telephoneNumber} </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </span>
        </div>
    </div>
    <nav class="navbar navbar-dark bg-dark">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarToggleExternalContent2"
                aria-controls="navbarToggleExternalContent2" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
            Team
        </button>
    </nav>
</div>
</br></br>
<div>
    <div class="collapse2" id="navbarToggleExternalContent">
        <div class="bg-dark p-4">
            <h5 class="text-white h4" align="center">Waypoints of order №${order}</h5>
            <span class="text-muted">
                <table class="table">
                    <thead class="thead-dark" align="center">
                            <tr>
                                <th scope="col"> City </th>
                                <th scope="col"> Cargo </th>
                                <th scope="col"> Action </th>
                            </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="waypoint" items="${waypoints}">
                            <tr>
                                <td scope="row" align="center"> ${waypoint.city.name} </td>
                                <td scope="row" align="center"> ${waypoint.cargo.name} </td>
                                <td scope="row" align="center"> ${waypoint.action.name()} </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </span>
        </div>
    </div>
    <nav class="navbar navbar-dark bg-dark">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarToggleExternalContent"
                aria-controls="navbarToggleExternalContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
            Waypoints
        </button>
    </nav>
</div>
</body>
</html>