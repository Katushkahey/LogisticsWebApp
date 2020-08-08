<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<head>
    <title>Trucks</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <style>
        html {
            height: 100%; /* Высота страницы */
        }

        body {
            background: rgb(255, 217, 240) url("../images/trucks.jpg"); /* путь к файлу */
            background-position: right;
            background-repeat: no-repeat;
            background-size: contain;
            color: #000;
        }

        .tableTab {
            width: 54%;
            height: 400px;
            overflow-x: auto;
            margin-left: 1rem;
        }

        .btn-success {
            margin-left: 1rem;
        }

    </style>
</head>
<body>
<div>
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: rgba(255,81,83,0.55)">
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a class="nav-link" href="/admin/"><strong>Главная </strong></a>
                <a class="nav-link active" href="/admin/trucks-info"><strong><u>Фуры </u></strong><span class="sr-only">(current)</span></a>
                <a class="nav-link" href="/admin/drivers-info"><strong>Водители </strong></a>
                <a class="nav-link" href="/admin/orders-info"><strong>Заказы </strong></a>
                <a class="nav-link" href="/admin/cargoes-info"><strong>Грузы </strong></a>
            </div>
        </div>
    </nav>
</div>
</br></br>
<div>
    <a class="btn btn-success" href="/truck/create_truck">Create Truck</a>
    </br></br>
    <div class="mainDiv">
        <div class="tableTab">
            <table class="table">
                <h5 class="text-black h4" style="background: rgba(15,20,109,0.36)" align="center"> List of trucks </h5>
                <span class="text-black">
                    <thead style="background: rgba(15,20,109,0.36)" align="center">
                            <tr>
                                <th scope="col"> № </th>
                                <th scope="col"> Number </th>
                                <th scope="col"> Capacity </th>
                                <th scope="col"> Crew </th>
                                <th scope="col"> State </th>
                                <th scope="col"> Available </th>
                                <th scope="col"> Edit </th>
                                <th scope="col"> Delete </th>
                            </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="truck" items="${listOfTrucks}">
                            <tr>
                                <td scope="row" align="center">${truck.id}</td>
                                <td scope="row" align="center">${truck.number}</td>
                                <td scope="row" align="center">${truck.capacity}</td>
                                <td scope="row" align="center">${truck.crewSize}</td>
                                <td scope="row" align="center">${truck.truckState}</td>
                                <td scope="row" align="center">${truck.order.id}</td>
                                <td scope="row" align="center"><a class="btn btn-secondary"
                                                                  href="/truck/edit_truck/${truck.id}"> Edit </a></td>
                                <td scope="row" align="center"><a class="btn btn-danger"
                                                                  href="/truck/delete_truck/${truck.id}"> Delete </a></td>
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