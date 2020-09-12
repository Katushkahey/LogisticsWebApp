<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>

<head>
    <title>Create Order</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <style>
        html {
            height: 100%; /* Высота страницы */
        }

        body {
            /* путь к файлу */
            background: rgb(255, 255, 255) url("../images/create_order.jpg") no-repeat right;
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

        .mainDiv2 {
            width: 50%;
            height: 400px;
            overflow-y: auto;
            overflow-x: auto;
            margin-left: 1rem;
        }

        .btn {
            margin-left: 1rem;
        }

        .info {
            margin-left: 1rem;
        }

        .nav-item8 {
            position: absolute;
            top: 0.3rem;
            right: 0.5rem;
        }
    </style>
</head>
<body>
<div>
    <nav class="navbar navbar-expand-lg navbar-light" style="background: rgba(136,144,229,0.74)">
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a class="nav-link" href="/order/info"><strong> Back to saved orders </strong></a>
                <a class="nav-link active" href="/create_order"><strong><u> Order create page</u></strong><span
                        class="sr-only">(current)</span></a>
                <a class="nav-item8">
                    <form action="/logout" method="post">
                        <input type="submit" class="btn btn-danger" value="Logout"/>
                    </form>
                </a>
            </div>
        </div>
    </nav>
</div>
</br>
<div>
    <span>
        <a class="btn btn-success" data-toggle="modal" data-target="#create_loading_point"> Add LoadingPoint </a>
        <div class="modal fade" id="create_loading_point" tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createEditLabel"> Add Loading Point </h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form action="/create_order/add_waypoint" method="get" class="formCreateWithValidation"
                              role="form">
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="createNameInput"> CargoName </label>
                                <div class="col-sm-9">
                                    <input type="text" class="name field" name="name" id="createNameInput"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="createWeightInput"> CargoWeight, kg </label>
                                <div class="col-sm-9">
                                    <input type="number" class="weight field" name="weight" id="createWeightInput"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="createCityInput">City</label>
                                <div>
                                    <select class="col-sm-6 field" name="cityName" id="createCityInput">
                                        <option></option>
                                        <c:forEach var="city" items="${listOfCities}">
                                            <option value=${city.name}>${city.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-3 col-sm-10">
                                    <button type="submit" class="btn btn-success">Save</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </span>
    <span>
    <c:choose>
        <c:when test="${order.cargoesToUnload.size() != 0}">
            <a class="btn btn-success" data-toggle="modal" data-target="#create_waypoint"> Add LoadingPoint </a>
            <div class="modal fade" id="create_waypoint" tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="createWaypointEditLabel"> Add Unloading Point </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form action="/create_order/add_waypoint" method="get" class="formCreateWithValidation2"
                                  role="form">
                                <div class="form-group">
                                    <label class="col-sm-3 control-label" for="cargoInput">Cargo</label>
                                    <div>
                                        <select class="col-sm-6 field" name="cargoId" id="cargoInput">
                                            <option></option>
                                            <c:forEach var="cargo" items="${order.cargoesToUnload}">
                                                <option value=${cargo.id}>${cargo.name + ", " + cargo.weight}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label" for="cityInput">City</label>
                                    <div>
                                        <select class="col-sm-6 field" name="cityName" id="cityInput">
                                            <option></option>
                                            <c:forEach var="city" items="${listOfCities}">
                                                <option value=${city.name}>${city.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-3 col-sm-10">
                                        <button type="submit" class="Save3btn btn-default">Save</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
    </c:choose>
    </span>
</div>
<c:choose>
    <c:when test="${order.waypoints.size() != 0}">
        <div>
            <div>
                <div class="mainDiv">
                    <div class="tableTab">
                        <table class="table">
                            <h5 class="text-black h4" style="background: rgba(136,144,229,0.74)" align="center"> List of waypoints </h5>
                            <span class="text-black">
                                <thead style="background: rgba(136,144,229,0.74)" align="center">
                                    <tr>
                                        <th scope="col"> № </th>
                                        <th scope="col"> Cargo </th>
                                        <th scope="col"> Weight, kg </th>
                                        <th scope="col"> City </th>
                                        <th scope="col"> Action </th>
                                        <th scope="col"> Change City </th>
                                        <th scope="col"> Delete </th>
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
                                            <td scope="row" align="center"><button type="button"
                                                                                   class="btn btn-secondary"
                                                                                   data-toggle="modal"
                                                                                   data-target="#edit_waypoint"
                                                                                   data-waypoint-id="${waypoint.id}"> Edit </button></td>
                                            <td scope="row" align="center">
                                            <c:choose>
                                            <c:when test="${waypoint.action == 'UNLOADING'}">
                                                <a class="btn btn-danger"
                                                   href="/create_order/delete_waypoint/${waypoint.id}"> Delete </a></td>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="button" class="btn btn-danger"
                                                        data-toggle="modal" data-target="#delete_loading_waypoint"> Delete
                                                </button>
                                                </td>
                                                <div class="modal fade" id="delete_loading_waypoint" tabindex="-1"
                                                     role="dialog" aria-labelledby="allertModalLabel"
                                                     aria-hidden="true">
                                                    <div class="modal-dialog" role="document">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="allertModalLabel10">Delete ${waypoint.cargoName}</h5>
                                                                <button type="button" class="close"
                                                                        data-dismiss="modal" aria-label="Close">
                                                                    <span aria-hidden="true">&times;</span>
                                                                </button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <form action="/create_order/delete_waypoint/${waypoint.id}"
                                                                      method="get" class="formWithValidation10"
                                                                      role="form">
                                                                    Are you sure, that you want to delete this waypoint? If this cargo has waypoint for UNLOADING
                                                                    if also will be deleted. You can use button 'Edit' to edit waypoint.
                                                                    <div class="modal-footer">
                                                                        <button type="button"
                                                                                class="btn btn-secondary"
                                                                                data-dismiss="modal"> Cancel </button>
                                                                        <button type="submit"
                                                                                class="btn btn-success"> Yes, delete </button>
                                                                    </div>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
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
        </div>
        <div class="modal fade" id="edit_waypoint" tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editWaypointEditLabel"> Add Waypoint </h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form action="/create_order/edit_waypoint" method="get" class="formCreateWithValidation3"
                              role="form">
                            <div class="form-group">
                                <label class="col-sm-3 control-label" visibility: hidden for="idEditInput">ID</label>
                                <div class="col-sm-9">
                                    <input type="number" readonly visibility: hidden
                                           class="id field" name="id" id="idEditInput"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="editNameInput"> CargoName </label>
                                <div class="col-sm-9">
                                    <input type="text" class="name field" name="name" id="editNameInput"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="editWeightInput"> CargoWeight, kg </label>
                                <div class="col-sm-9">
                                    <input type="number" class="weight field" name="weight" id="editWeightInput"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="cityEditInput">City</label>
                                <div>
                                    <select class="col-sm-6 field" name="cityName" id="cityEditInput">
                                        <c:forEach var="city" items="${listOfCities}">
                                            <option value=${city.name}>${city.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-3 col-sm-10">
                                    <button type="submit" class="Save4btn btn-default">Save</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </c:when>
</c:choose>
<div>
    <c:choose>
        <c:when test="${order.waypoints.size() != 0}">
            <span>
            <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#clear_all"> Clear all </button>
            <div class="modal fade" id="clear_all" tabindex="-1" role="dialog" aria-labelledby="allertModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="allertModalLabel15">Clear all</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form action="/create_order/clear_all" method="get" class="formWithValidation15"
                                  role="form">
                                Are you sure, that you want to delete all changes and redirect to page with saved
                                orders?
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal"> No
                                    </button>
                                    <button type="submit" class="btn btn-success"> Yes</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            </span>
        </c:when>
    </c:choose>
    <span>
        <c:choose>
            <c:when test="${order.waypoints.size() != 0 && order.cargoesToUnload.size() == 0}">
                <button type="button" class="btn btn-success" data-toggle="modal" data-target="#save_order"> Save order
                </button>
                <div class="modal fade" id="save_order" tabindex="-1" role="dialog" aria-labelledby="allertModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="allertModalLabel">Save order</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form action="/create_order/save_order" method="get" class="formWithValidation4" role="form">
                                    Are you sure, that you want to save this order?
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal"> No</button>
                                        <button type="submit" class="btn btn-success"> Yes</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
        </c:choose>
    </span>
</div>
</body>
<script>
    $("#create_loading_point").on('show.bs.modal', function (e) {
        $('#createNameInput').val(null)
        $('#createWeightInput').val(null);
        $('#createCityInput').val(null);
    });
    $("#create_waypoint").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });

    var form = document.querySelector('.formCreateWithValidation')
    var fields = form.querySelectorAll('.field')
    var weight = form.querySelector('.weight')

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

        if (errors_counter < 1) {
            form.submit()
        }
    });
</script>
<script>
    $("#create_waypoint").on('show.bs.modal', function (e) {
        $('#cargoInput').val(null);
        $('#cityInput').val(null);
    });
    $("#create_waypoint").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });

    var form2 = document.querySelector('.formCreateWithValidation2')
    var fields2 = form2.querySelectorAll('.field')

    form2.addEventListener("submit", function (event) {
        event.preventDefault()

        var errors2 = form2.querySelectorAll('.error')

        for (var i = 0; i < errors2.length; i++) {
            errors[i].remove()
        }

        var errors_counter2 = 0
        for (var i = 0; i < fields2.length; i++) {
            if (!fields2[i].value) {
                errors_counter2 += 1
                var error = document.createElement('div')
                error.className = 'error'
                error.style.color = 'red'
                error.innerHTML = 'Can`t be empty'
                form2[i].parentElement.insertBefore(error, fields2[i])
            }
        }

        if (errors_counter2 < 1) {
            form2.submit()
        }
    })
</script>
<script>
    $("#edit_waypoint").on('show.bs.modal', function (e) {
        var waypointId = $(e.relatedTarget).data('waypoint-id');
        var cols = $('#waypoint-' + waypointId + ' td');
        var id = waypointId;
        var name = $(cols[1]).text();
        var weight = $(cols[2]).text();
        var city = $(cols[3]).text();
        $('#idEditInput').val(id);
        $('#editNameInput').val(name);
        $('#editWeightInput').val(weight);
        $('#cityEditInput').val(city);
    });
    $("#edit_waypoint").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });

    var form3 = document.querySelector('.formCreateWithValidation3')
    var fields3 = form3.querySelectorAll('.field')
    var weight3 = form3.querySelector('.weight')

    form3.addEventListener("submit", function (event) {
        event.preventDefault()

        var errors3 = form3.querySelectorAll('.error')



        for (var i = 0; i < errors.length; i++) {
            errors3[i].remove()
        }

        var errors_counter3 = 0
        for (var i = 0; i < fields3.length; i++) {
            if (!fields3[i].value) {
                errors_counter3 += 1
                var error = document.createElement('div')
                error.className = 'error'
                error.style.color = 'red'
                error.innerHTML = 'Can`t be empty'
                form3[i].parentElement.insertBefore(error, fields3[i])
            }
        }

        if (weight3.value > ${maxWeight} * 1000) {
            errors_counter3 += 1
            var error2 = document.createElement('div')
            error2.className = 'error'
            error2.style.color = 'red'
            error2.innerHTML = 'This cargoWeight bigger than capacity of the biges`t truck'
            weight3.parentElement.insertBefore(error2, weight3)
        }

        if (weight3.value <= 0) {
            errors_counter3 += 1
            var error3 = document.createElement('div')
            error3.className = 'error'
            error3.style.color = 'red'
            error3.innerHTML = 'Can`t be < 0'
            weight3.parentElement.insertBefore(error3, weight3)
        }

        if (errors_counter3 < 1) {
            form3.submit()
        }
    })
</script>
<%--<script>--%>
    <%--$("#save_order").on('show.bs.modal', function (e) {--%>

    <%--});--%>
    <%--$("#save_order").on('hidden.bs.modal', function () {--%>
        <%--var form = $(this).find('form');--%>
        <%--form[0].reset();--%>
    <%--});--%>

    <%--var form40 = document.querySelector('.formWithValidation40')--%>
    <%--var fields40 = form40.querySelectorAll('.field')--%>

    <%--form40.addEventListener("submit", function (event) {--%>
        <%--event.preventDefault()--%>

        <%--var errors = form40.querySelectorAll('.error')--%>

        <%--for (var i = 0; i < errors.length; i++) {--%>
            <%--errors[i].remove()--%>
        <%--}--%>

        <%--var errors_counter40 = 0--%>
        <%--for (var i = 0; i < fields40.length; i++) {--%>
            <%--if (!fields40[i].value) {--%>
                <%--errors_counter40 += 1--%>
                <%--var error = document.createElement('div')--%>
                <%--error.className = 'error'--%>
                <%--error.style.color = 'red'--%>
                <%--error.innerHTML = 'Can`t be empty'--%>
                <%--form40[i].parentElement.insertBefore(error, fields40[i])--%>
            <%--}--%>
        <%--}--%>

        <%--if (errors_counter40 < 1) {--%>
            <%--form40.submit()--%>
        <%--}--%>
    <%--})--%>
<%--</script>--%>
</html>