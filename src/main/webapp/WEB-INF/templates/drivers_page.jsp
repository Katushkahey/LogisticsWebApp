<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<head>
    <title>Drivers</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <style>
        html {
            height: 100%; /* Высота страницы */
        }

        body {
            background: rgb(101, 177, 155) url("../images/drivers.jpg"); /* путь к файлу */
            background-position: right;
            background-repeat: no-repeat;
            background-size: contain;
            color: #000;
        }

        .mainDiv {
            width: 57%;
            height: 400px;
            overflow-y: auto;
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
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: #ffcc88">
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a class="nav-link" href="/admin/"><strong>Главная </strong></a>
                <a class="nav-link" href="/admin/trucks-info"><strong>Фуры </strong></a>
                <a class="nav-link active" href="/admin/drivers-info"><strong><u>Водители </u></strong><span
                        class="sr-only">(current)</span></a>
                <a class="nav-link" href="/admin/orders-info"><strong>Заказы </strong></a>
                <a class="nav-link" href="/admin/cargoes-info"><strong>Грузы </strong></a>
            </div>
        </div>
    </nav>
</div>
</br></br>
<div>
    <a class="btn btn-success" href="/driver/create_driver">Create Driver</a>
    </br></br>
    <div class="mainDiv">
        <div class="tableTab">
            <table class="table">
                <h5 class="text-black h4" style="background: #ffcc88" align="center"> List of drivers </h5>
                <span class="text-black">
                    <thead style="background:  #ffcc88" align="center">
                            <tr>
                                <th scope="col"> № </th>
                                <th scope="col"> Name </th>
                                <th scope="col"> Surname </th>
                                <th scope="col"> Telephone </th>
                                <th scope="col"> Hours </th>
                                <th scope="col"> City </th>
                                <th scope="col"> Available </th>
                                <th scope="col"> Edit </th>
                                <th scope="col"> Delete </th>
                            </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="driver" items="${listOfDrivers}">
                            <tr>
                                <td scope="row" align="center">${driver.id}</td>
                                <td scope="row" align="center">${driver.name}</td>
                                <td scope="row" align="center">${driver.surname}</td>
                                <td scope="row" align="center">${driver.telephoneNumber}</td>
                                <td scope="row" align="center">${driver.hoursThisMonth}</td>
                                <td scope="row" align="center">${driver.currentCity.name}</td>
                                <td scope="row" align="center">${driver.currentOrder.id}</td>
                                <td scope="row" align="center"><a class="btn btn-secondary"
                                                                  href="/truck/edit_truck/${driver.id}"> Edit </a></td>
                                <td scope="row" align="center"><a class="btn btn-danger"
                                                                  href="/truck/delete_truck/${driver.id}"> Delete </a></td>
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