<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<head>
    <title>Order details</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <style>
        html {
            height: 100%; /* Высота страницы */
        }

        body {
            background: rgb(255, 255, 255) url("../images/orders.jpg"); /* путь к файлу */
            background-position: right;
            background-repeat: no-repeat;
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

        .nav-item {
            position: relative;
            left: 55em ;
        }
    </style>
</head>
<body>
</br></br>
</br></br>
<div>
    <div class="mainDiv">
        <div class="tableTab">
            <table class="table">
                <h5 class="text-black h4" style="background: rgba(255,162,69,0.57)" align="center"> Details of
                    orders № ${order}  </h5>
                <span class="text-black">
                    <thead style="background: rgba(255,162,69,0.57)" align="center">
                            <tr>
                                <th scope="col"> № </th>
                                <th scope="col"> Cargo </th>
                                <th scope="col"> Weight </th>
                                <th scope="col"> City </th>
                                <th scope="col"> Action </th>
                                <th scope="col"> Status </th>
                            </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="waypoint" items="${waypoints}">
                            <tr>
                                <td scope="row" align="center">${waypoint.id}</td>
                                <td scope="row" align="center">${waypoint.cargo.name}</td>
                                <td scope="row" align="center">${waypoint.cargo.weight}</td>
                                <td scope="row" align="center">${waypoint.city.name}</td>
                                <td scope="row" align="center">${waypoint.action}</td>
                                <td scope="row" align="center">${waypoint.status}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </span>
            </table>
        </div>
    </div>
</div>
</body>
</html>

