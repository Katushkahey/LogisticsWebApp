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
            background: rgb(244, 244, 234) url('${pageContext.request.contextPath}/images/order_assign.jpg') no-repeat right;
            background-size: contain;
            color: #000;
        }

        .mainDiv {
            width: 60%;
            height: 500px;
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
</br>
<input type=button class="btnBack btn-secondary" value=" Back " onCLick="history.back()">
</br></br>
<div>
    <div class="mainDiv">
        <div class="tableTab">
            <table class="table">
                <h5 class="text-black h4" style="background: rgba(150,214,132,0.93)" align="center">Order
                    №${order.number} assignment: truck and drivers </h5>
                <span class="text-black">
                    <thead style="background: rgba(150,214,132,0.93)" align="center">
                            <tr>
                                <th scope="col"> Truck </th>
                                <th scope="col"> Drivers </th>
                                <th scope="col"> Home city </th>
                                <th scope="col"> Number of hours to complete the order </th>
                                <th scope="col"> Total billable hours </th>
                                <th scope="col"> Choose </th>
                            </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="combination" items="${listOfCombinations}">
                            <tr id="combination-${combination.id}">
                                <td scope="row" align="center">${combination.truckNumber}</td>
                                <td scope="row" align="center">${combination.drivers}</td>
                                <td scope="row" align="center">${combination.city}</td>
                                <td scope="row" align="center">${combination.totalHours}</td>
                                <td scope="row" align="center">${combination.totalBillableHours}</td>
                                <td scope="row" align="center"><a class="btn btn-success"
                                                                  href="choose_assignment/${id}/${combination.id}">
                                    Choose </a>
                            </tr>
                        </c:forEach>
                    </tbody>
                </span>
            </table>
        </div>
    </div>
</div>
</body>
<script>
    function setData() {
        var category = $('#assignment').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            data: {categoryList: category},
            url: '/order/assign_order/choose_assignment'
        });
    }
</script>
</html>
