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
            /* путь к файлу */
            background: rgb(255, 255, 255) url('${pageContext.request.contextPath}/images/info.jpg') no-repeat right;
            background-size: contain;
            color: #000;
        }

        .mainDiv {
            width: 55%;
            height: 450px;
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
                <h5 class="text-black h4" style="background: rgba(150,214,132,0.93)" align="center"> Details of order
                    № ${order.number}  </h5>
                <span class="text-black">
                    <thead style="background: rgba(150,214,132,0.93)" align="center">
                            <tr>
                                <th scope="col"> № </th>
                                <th scope="col"> Cargo </th>
                                <th scope="col"> Weight, kg </th>
                                <th scope="col"> City </th>
                                <th scope="col"> Action </th>
                                <th scope="col"> Status </th>
                                <c:choose>
                                    <c:when test="${order.status=='IN_PROGRESS' || order.status=='COMPLETED' || order.status=='WAITING'}">
                                    </c:when>
                                    <c:otherwise>
                                        <th scope="col"> Edit </th>
                                        <th scope="col"> Delete </th>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="waypoint" items="${order.waypoints}" varStatus="loop">
                            <tr id="waypoint-${waypoint.id}">
                                <td scope="row" align="center">${loop.count}</td>
                                <td scope="row" align="center">${waypoint.cargoName}</td>
                                <td scope="row" align="center">${waypoint.cargoWeight}</td>
                                <td scope="row" align="center">${waypoint.cityName}</td>
                                <td scope="row" align="center">${waypoint.action}</td>
                                <td scope="row" align="center">${waypoint.status}</td>
                                <c:choose>
                                    <c:when test="${order.status=='IN_PROGRESS' || order.status=='COMPLETED' || order.status=='WAITING'}">
                                    </c:when>
                                    <c:otherwise>
                                        <td scope="row" align="center"><button type="button" class="btn btn-secondary"
                                                                               data-toggle="modal"
                                                                               data-target="#edit_waypoint"
                                                                               data-waypoint-id="${waypoint.id}"> Edit </button>
                                        <td scope="row" align="center"><a class="btn btn-danger"
                                                                          href="/order/delete_waypoint/${order.id}/${waypoint.id}"> Delete </a></td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        </c:forEach>
                    </tbody>
                </span>
            </table>
        </div>
    </div>
</div>
<div class="modal fade" id="edit_waypoint" tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editLabel"> Edit Waypoint </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="/order/edit_waypoint/${order.id}" method="get" class="formWithValidation" role="form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label" visibility: hidden for="idInput">ID</label>
                        <div class="col-sm-9">
                            <input type="number" readonly visibility: hidden class="id field" name="id" id="idInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="cargoInput">Cargo</label>
                        <div class="col-sm-9">
                            <input type="text" class="cargo field" name="cargoName" id="cargoInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="weightInput">Weight, kg</label>
                        <div class="col-sm-9">
                            <input type="number" class="weight field" name="weight" id="weightInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="cityInput">City</label>
                        <div>
                            <select class="col-sm-6 field" name="city" id="cityInput">
                                <c:forEach var="city" items="${listOfCities}">
                                    <option value=${city.name}>${city.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="sequenceInput">Weight, kg</label>
                        <div class="col-sm-9">
                            <input type="number" class="sequence field" name="sequence" id="sequenceInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-10">
                            <button type="submit" class="btn btn-default">Save</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    $("#edit_waypoint").on('show.bs.modal', function (e) {
        var waypointId = $(e.relatedTarget).data('waypoint-id');
        var cols = $('#waypoint-' + waypointId + ' td');
        var id = waypointId;
        var cargo = $(cols[1]).text();
        var weight = $(cols[2]).text();
        var city = $(cols[3]).text();
        $('#idInput').val(id);
        $('#cargoInput').val(cargo);
        $('#weightInput').val(weight);
        $('#cityInput').val(city);
    });
    $("#edit_waypoint").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });

    var form = document.querySelector('.formWithValidation')
    var weight = form.querySelector('.weight')
    var fields = form.querySelectorAll('.field')

    form.addEventListener("submit", function (event) {
        event.preventDefault()

        var errors = form.querySelectorAll('.error')

        for (var i = 0; i < errors.length; i++) {
            errors[i].remove()
        }

        var errors_counter = 0
        for (var i = 0; i < fields.length; i++) {
            if (!fields[i].value) {
                errors_counter += 1
                var error = document.createElement('div')
                error.className = 'error'
                error.style.color = 'red'
                error.innerHTML = 'Can`t be empty'
                form[i].parentElement.insertBefore(error, fields[i])
            }
        }

        if (errors_counter < 1) {
            form.submit()
        }

        if (weight.value > ${maxWeight} * 1000) {
            errors_counter += 1
            var error2 = document.createElement('div')
            error2.className = 'error'
            error2.style.color = 'red'
            error2.innerHTML = 'This cargoWeight bigger than capacity of the biges`t truck'
            weight.parentElement.insertBefore(error2, weight)
        }

        if (weight.value <= 0) {
            errors_counter += 1
            var error3 = document.createElement('div')
            error3.className = 'error'
            error3.style.color = 'red'
            error3.innerHTML = 'Can`t be < 0'
            weight.parentElement.insertBefore(error3, weight)
        }
    })
</script>
</html>
