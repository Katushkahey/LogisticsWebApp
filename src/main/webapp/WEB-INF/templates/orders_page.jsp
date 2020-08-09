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
            width: 53%;
            height: 400px;
            overflow-y: auto;
            overflow-x: auto;
            margin-left: 1rem;
        }

        .btn-success {
            margin-left: 1rem;
        }

        .nav-item {
            position: relative;
            left: 55em ;
        }
    </style>
</head>
<body>
<div>
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: rgba(223,96,97,0.66)">
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
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: rgba(217,97,101,0.74)">
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup2">
            <div class="navbar-nav">
                <a class="nav-link active" href="/order/info"><strong><u> Неназначенные </u></strong><span
                        class="sr-only">(current)</span></a>
                <a class="nav-link" href="/order/info-2"><strong> Назначенные </strong></a>
                <a class="nav-link" href="/order/info-3"><strong> Выполненные за месяц </strong></a>
            </div>
        </div>
    </nav>
</div>
</br></br>
<div>
    <a class="btn btn-success" href="/order/create_order">Create Order</a>
    </br></br>
    <div class="mainDiv">
        <div class="tableTab">
            <table class="table">
                <h5 class="text-black h4" style="background: rgba(116,219,116,0.61)" align="center"> List of orders </h5>
                <span class="text-black">
                    <thead style="background:  rgba(116,219,116,0.61)" align="center">
                            <tr>
                                <th scope="col"> № </th>
                                <th scope="col"> Drivers </th>
                                <th scope="col"> Truck </th>
                                <th scope="col"> Max weight </th>
                                <th scope="col"> Add Driver </th>
                                <th scope="col"> Add Truck </th>
                                <th scope="col"> Details </th>
                                <th scope="col"> Edit </th>
                                <th scope="col"> Delete </th>
                            </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${mapOfOrders.keySet()}">
                            <tr>
                                <td scope="row" align="center">${order.id}</td>
                                <td scope="row" align="center">${order.drivers}</td>
                                <td scope="row" align="center">${order.orderTruck.number}</td>
                                <td scope="row" align="center">${mapOfOrders.get(order)}</td>
                                <td scope="row" align="center"> Add Driver </td>
                                <td scope="row" align="center"> Add Truck </td>
                                <td scope="row" align="center"><a class="btn btn-secondary"
                                                                   href="/order/show_info/${order.id}"> Details </a></td>
                                <td scope="row" align="center"><a class="btn btn-secondary"
                                                                  href="/order/edit_order/${order.id}"> Edit </a></td>
                                <td scope="row" align="center"><a class="btn btn-danger"
                                                                  href="/order/delete_order/${order.id}"> Delete </a></td>
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

