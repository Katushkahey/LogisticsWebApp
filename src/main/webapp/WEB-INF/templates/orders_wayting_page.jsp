<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<head>
    <title>Orders</title>

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
            width: 65%;
            height: 400px;
            overflow-y: auto;
            overflow-x: auto;
            margin-left: 1rem;
        }

        .nav-item {
            position: absolute;
            margin-top: 0.3rem;
            right: 0;
        }
    </style>
</head>
<body>
<div>
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: rgba(255,162,69,0.57)">
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a class="nav-link" href="/admin/"><strong> Главная </strong></a>
                <a class="nav-link" href="/truck/info"><strong> Фуры </strong></a>
                <a class="nav-link" href="/drivers/info"><strong> Водители </strong></a>
                <a class="nav-link active" href="/order/info"><strong><u> Заказы </u></strong><span
                        class="sr-only">(current)</span></a>
                <a class="nav-item">
                    <form action="/logout" method="post">
                        <input type="submit" class="btn btn-danger" value="Logout"/>
                    </form>
                </a>
            </div>
        </div>
    </nav>
</div>
<div>
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: rgba(214,128,45,0.65)">
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup2">
            <div class="navbar-nav">
                <a class="nav-link" href="/order/info"><strong> No assigned </strong></a>
                <a class="nav-link active" href="/order/info-3"><strong><u> Waiting </u></strong><span class="sr-only">(current)</span></a>
                <a class="nav-link" href="/order/info-4"><strong> In Progress </strong></a>
                <a class="nav-link" href="/order/info-3"><strong> Completed </strong></a>
            </div>
        </div>
    </nav>
</div>
</br></br>
</br></br>
<div>
    <div class="mainDiv">
        <div class="tableTab">
            <table class="table">
                <h5 class="text-black h4" style="background: rgba(255,162,69,0.57)" align="center"> List of
                    orders </h5>
                <span class="text-black">
                    <thead style="background: rgba(255,162,69,0.57)" align="center">
                            <tr>
                                <th scope="col"> № </th>
                                <th scope="col"> Number </th>
                                <th scope="col"> Drivers </th>
                                <th scope="col"> Truck </th>
                                <th scope="col"> Max weight, kg </th>
                                <th scope="col"> List of cargoes </th>
                                <th scope="col"> City From </th>
                                <th scope="col"> City To </th>
                                <th scope="col"> Details </th>
                                <th scope="col"> Cancel </th>
                            </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${listOfOrders}" varStatus="loop">
                            <tr>
                                <td scope="row" align="center">${loop.count}</td>
                                <td scope="row" align="center">${order.number}</td>
                                <td scope="row" align="center">${order.drivers}</td>
                                <td scope="row" align="center">${order.truckNumber}</td>
                                <td scope="row" align="center">${order.maxWeight}</td>
                                <td scope="row" align="center">${order.cargoes}</td>
                                <td scope="row" align="center">${order.cityFrom}</td>
                                <td scope="row" align="center">${order.cityTo}</td>
                                <td scope="row" align="center"><a class="btn btn-secondary"
                                                                  href="/order/show_info/${order.id}"> Details </a></td>
                                <td scope="row" align="center"><a class="btn btn-danger"
                                                                  href="/order/cancel_assignment/${order.id}"> Cancel </a></td>
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