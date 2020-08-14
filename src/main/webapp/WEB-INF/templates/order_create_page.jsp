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
            background: rgb(255, 255, 255);
            background-size: contain;
            color: #000;
        }

        .mainDiv {
            width: 50%;
            height: 450px;
            overflow-y: auto;
            overflow-x: auto;
            margin-left: 1rem;
        }

        .mainDiv2 {
            width: 50%;
            height: 450px;
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
<c:choose>
    <c:when test="${mapOfCargoes.size() == 0 && listOfWaypoints.size() == 0}">
        <a class="btn btn-success" data-toggle="modal" data-target="#create_raw_cargo"> Add Cargo </a>
        </br>
        <h6 class="info"><strong> Here will be your order. First of all add all cargoes.</strong></h6>
        <div class="modal fade" id="create_raw_cargo" tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createEditLabel"> Add Cargo </h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form action="/create_order/add_cargo" method="get" class="formCreateWithValidation"
                              role="form">
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="createNameInput"> Name </label>
                                <div class="col-sm-9">
                                    <input type="text" class="name field" name="name" id="createNameInput"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="createWeightInput"> Weight </label>
                                <div class="col-sm-9">
                                    <input type="number" class="weight field" name="weight" id="createWeightInput"/>
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
        <c:choose>
            <c:when test="${listOfCargoes.size() == 0}">
                </br>
            </c:when>
            <c:otherwise>
                <div>
                    <div class="mainDiv2">
                        <div class="tableTab">
                            <table class="table">
                                <h5 class="text-black h4" style="background: rgba(136,144,229,0.74)" align="center">
                                    List of cargoes </h5>
                                <span class="text-black">
                                    <thead style="background: rgba(136,144,229,0.74)" align="center">
                                        <tr>
                                            <th scope="col"> № </th>
                                            <th scope="col"> Cargo </th>
                                            <th scope="col"> Weight </th>
                                            <th scope="col"> Edit </th>
                                            <th scope="col"> Delete </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="cargo" items="${listOfCargoes}">
                                            <tr id="cargo-${cargo.id}">
                                                <td scope="row" align="center">${cargo.id}</td>
                                                <td scope="row" align="center">${cargo.name}</td>
                                                <td scope="row" align="center">${cargo.weight}</td>
                                                <td scope="row" align="center"><button type="button"
                                                                                       class="btn btn-secondary"
                                                                                       data-toggle="modal"
                                                                                       data-target="#edit_raw_cargo"
                                                                                       data-cargo-id="${cargo.id}"> Edit </button></td>
                                                <td scope="row" align="center"><a class="btn btn-danger"
                                                                                  href="/create_order/delete_cargo/${cargo.id}"> Delete </a></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </span>
                            </table>
                        </div>
                    </div>
                </div>
                <div>
                    <a class="btn btn-success" href="/create_order/save_cargoes">Save</a>
                </div>
                <div class="modal fade" id="edit_raw_cargo" tabindex="-1" aria-labelledby="editLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="editLabel"> Edit cargo </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form action="/create_order/edit_cargo" method="get" class="formEditWithValidation"
                                      role="form">
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label" for="idInput">ID</label>
                                        <div class="col-sm-9">
                                            <input type="number" readonly
                                                   class="id field" name="id" id="idInput"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label" for="nameInput">Name</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="name field" name="name" id="nameInput"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label" for="weightInput">Weight</label>
                                        <div class="col-sm-9">
                                            <input type="number" class="weight field" name="weight" id="weightInput"/>
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
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${listOfCargoes.size() == 0}">
            </c:when>
            <c:otherwise>
                <div>
                    <div class="mainDiv2">
                        <div class="tableTab">
                            <table class="table">
                                <h5 class="text-black h4" style="background: rgba(136,144,229,0.74)" align="center">
                                    List of cargoes </h5>
                                <span class="text-black">
                                    <thead style="background: rgba(136,144,229,0.74)" align="center">
                                        <tr>
                                            <th scope="col"> № </th>
                                            <th scope="col"> Cargo </th>
                                            <th scope="col"> Weight </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="cargo" items="${mapOfCargoes.keySet()}">
                                            <tr id="cargo-${cargo.id}">
                                                <td scope="row" align="center">${cargo.id}</td>
                                                <td scope="row" align="center">${cargo.name}</td>
                                                <td scope="row" align="center">${cargo.weight}</td>
                                        </c:forEach>
                                    </tbody>
                                </span>
                            </table>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${listOfWaypoints.size() != 0 && mapOfCargoes.size() == 0}">

            </c:when>
            <c:otherwise>
                <div>
                    <button type="button" class="btn btn-success" data-toggle="modal" data-target="#create_waypoint">
                        Create waypoint
                    </button>
                </div>
                <div>
                    <h6 class="info"><strong> For each cargo you`ll have 2 waypoints : first for LOADING and second for UNLOADING. </strong></h6>
                </div>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${listOfWaypoints.size() == 0}">
                </br>
            </c:when>
            <c:otherwise>
                </br>
                <div>
                    <div class="mainDiv">
                        <div class="tableTab">
                            <table class="table">
                                <h5 class="text-black h4" style="background: rgba(136,144,229,0.74)" align="center">
                                    List of waypoints </h5>
                                <span class="text-black">
                                    <thead style="background: rgba(136,144,229,0.74)" align="center">
                                        <tr>
                                            <th scope="col"> № </th>
                                            <th scope="col"> Cargo </th>
                                            <th scope="col"> Weight </th>
                                            <th scope="col"> City </th>
                                            <th scope="col"> Action </th>
                                            <th scope="col"> Change City </th>
                                            <th scope="col"> Delete </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="waypoint" items="${listOfWaypoints}">
                                            <tr id="waypoint-${waypoint.id}">
                                                <td scope="row" align="center">${waypoint.id}</td>
                                                <td scope="row" align="center">${waypoint.cargo.name}</td>
                                                <td scope="row" align="center">${waypoint.cargo.weight}</td>
                                                <td scope="row" align="center">${waypoint.city.name}</td>
                                                <td scope="row" align="center">${waypoint.action}</td>
                                                <td scope="row" align="center"><button type="button"
                                                                                       class="btn btn-secondary"
                                                                                       data-toggle="modal"
                                                                                       data-target="#edit_waypoint"
                                                                                       data-waypoint-id="${waypoint.id}"> Edit </button>
                                                <td scope="row" align="center"><a class="btn btn-danger"
                                                                                  href="/create_order/delete_waypoint/${waypoint.id}"> Delete </a></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </span>
                            </table>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
        <div class="modal fade" id="create_waypoint" tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createWaypointEditLabel"> Add Waypoint </h5>
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
                                        <c:forEach var="cargo" items="${mapOfCargoes.keySet()}">
                                            <option value=${cargo.id}>${cargo.name}</option>
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
                                <label class="col-sm-3 control-label" for="idEditInput">ID</label>
                                <div class="col-sm-9">
                                    <input type="number" readonly
                                           class="id field" name="id" id="idEditInput"/>
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
        <div>
            <c:choose>
                <c:when test="${listOfWaypoints.size() != 0 && mapOfCargoes.size() == 0}">
                    <button type="button" class="btn btn-success"
                            data-toggle="modal" data-target="#save_order"> Save order
                    </button>
                </c:when>
            </c:choose>
            <button type="button" class="btn btn-danger"
                    data-toggle="modal" data-target="#clear_all"> Clear all
            </button>
        </div>
        <div class="modal fade" id="save_order" tabindex="-1" role="dialog" aria-labelledby="allertModalLabel"
             aria-hidden="true">
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
        <div class="modal fade" id="clear_all" tabindex="-1" role="dialog" aria-labelledby="allertModalLabel"
             aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="allertModalLabel2">Clear all</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form action="/create_order/clear_all" method="get" class="formWithValidation5" role="form">
                            Are you sure, that you want to delete all changes and redirect to page with saved orders?
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal"> No</button>
                                <button type="submit" class="btn btn-success"> Yes</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>
</body>
<script>
    $("#edit_raw_cargo").on('show.bs.modal', function (e) {
        var cargoId = $(e.relatedTarget).data('cargo-id');
        var cols = $('#cargo-' + cargoId + ' td');
        var id = $(cols[0]).text();
        var name = $(cols[1]).text();
        var weight = $(cols[2]).text();
        $('#idInput').val(id);
        $('#nameInput').val(name);
        $('#weightInput').val(weight);
    });
    $("#edit_raw_cargo").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });

    var form2 = document.querySelector('.formEditWithValidation')
    var weight2 = form2.querySelector('.weight')
    var fields2 = form2.querySelectorAll('.field')

    form2.addEventListener("submit", function (event) {
        event.preventDefault()

        var errors = form2.querySelectorAll('.error')

        for (var i = 0; i < errors.length; i++) {
            errors[i].remove()
        }

        var errors_counter2 = 0
        for (var i = 0; i < fields2.length; i++) {
            if (!fields2[i].value) {
                errors_counter2 += 1
                var error3 = document.createElement('div')
                error3.className = 'error'
                error3.style.color = 'red'
                error3.innerHTML = 'Can`t be empty'
                form2[i].parentElement.insertBefore(error3, fields2[i])
            }
        }

        if (weight2.value > ${maxWeight} * 1000) {
            errors_counter2 += 1
            var error4 = document.createElement('div')
            error4.className = 'error'
            error4.style.color = 'red'
            error4.innerHTML = 'This weight bigger than capacity of the biges`t truck'
            weight2.parentElement.insertBefore(error4, weight2)
        }

        if (weight2.value <= 0) {
            errors_counter2 += 1
            var error8 = document.createElement('div')
            error8.className = 'error'
            error8.style.color = 'red'
            error8.innerHTML = 'Can`t be < 0'
            weight2.parentElement.insertBefore(error8, weight2)
        }

        if (errors_counter2 < 1) {
            form2.submit()
        }
    })
</script>
<script>
    $("#create_raw_cargo").on('show.bs.modal', function (e) {
        $('#createNameInput').val(null);
        $('#createWeightInput').val(null);
    });
    $("#create_raw_cargo").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });

    var form = document.querySelector('.formCreateWithValidation')
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

        if (weight.value > ${maxWeight} * 1000) {
            errors_counter += 1
            var error2 = document.createElement('div')
            error2.className = 'error'
            error2.style.color = 'red'
            error2.innerHTML = 'This weight bigger than capacity of the biges`t truck'
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
    })
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

    var form3 = document.querySelector('.formCreateWithValidation2')
    var fields3 = form3.querySelectorAll('.field')

    form3.addEventListener("submit", function (event) {
        event.preventDefault()

        var errors = form3.querySelectorAll('.error')

        for (var i = 0; i < errors.length; i++) {
            errors[i].remove()
        }

        var errors_counter3 = 0
        for (var i = 0; i < fields3.length; i++) {
            if (!fields3[i].value) {
                errors_counter += 1
                var error = document.createElement('div')
                error.className = 'error'
                error.style.color = 'red'
                error.innerHTML = 'Can`t be empty'
                form3[i].parentElement.insertBefore(error, fields3[i])
            }
        }

        if (errors_counter3 < 1) {
            form3.submit()
        }
    })
</script>
<script>
    $("#edit_waypoint").on('show.bs.modal', function (e) {
        var cargoId = $(e.relatedTarget).data('cargo-id');
        var cols = $('#cargo-' + cargoId + ' td');
        var id = $(cols[0]).text();
        var city = $(cols[3]).text();
        $('#idEditInput').val(id);
        $('#cityEditInput').val(city);
    });
    $("#edit_waypoint").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });

    var form4 = document.querySelector('.formCreateWithValidation3')
    var fields4 = form4.querySelectorAll('.field')

    form4.addEventListener("submit", function (event) {
        event.preventDefault()

        var errors = form4.querySelectorAll('.error')

        for (var i = 0; i < errors.length; i++) {
            errors[i].remove()
        }

        var errors_counter4 = 0
        for (var i = 0; i < fields4.length; i++) {
            if (!fields4[i].value) {
                errors_counter4 += 1
                var error = document.createElement('div')
                error.className = 'error'
                error.style.color = 'red'
                error.innerHTML = 'Can`t be empty'
                form4[i].parentElement.insertBefore(error, fields4[i])
            }
        }

        if (errors_counter3 < 1) {
            form4.submit()
        }
    })
</script>
</html>