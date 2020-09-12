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
            background: rgb(247, 205, 145) url("../images/driver_page.jpg"); /* путь к файлу */
            background-position: right;
            background-repeat: no-repeat;
            background-size: contain;
            color: #000; /* Цвет текста */
        }

        .navbar {
            width: 50%;
            margin-left: 1rem;
        }

        .p-4 {
            height: 250px;
            width: 50%;
            overflow-x: auto;
            margin-left: 1rem;
        }

        .info {
            margin-left: 1rem;
        }

        .form-group {
            margin-left: 1rem;
            width: 50%;
        }

        .nav-item {
            position: relative;
            left: 5em;
            margin-top: 0.3rem;
        }

        .nav-item2 {
            position: absolute;
            margin-top: 0.3rem;
            right: 0.5rem;
        }

        .nav-item3 {
            position: relative;
            left: 7em;
            margin-top: 0.3rem;
        }

    </style>
</head>
<body>
<div>
    <nav class="navbar2 navbar-expand-lg navbar-light" style="background: rgba(67,41,28,0.99)">
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a class="navbar-link">
                    <img src="../images/man_icon.png" width="50" height="50" class="d-inline-block align-top" alt=""
                         loading="lazy">
                    <span class="text-white">${driver.name} ${driver.surname}, ${driver.telephoneNumber}</span>
                </a>
                <a class="nav-item">
                    <form data-toggle="modal" data-target="#edit_phone"
                          data-driver-telephone="${driver.telephoneNumber}">
                        <button type="button" class="btn btn-" style="background: #e7ecf0"> Edit phone</button>
                    </form>
                </a>
                <%--<a class="nav-item3">--%>
                    <%--<form data-toggle="modal" data-target="#edit_password">--%>
                        <%--<button type="button" class="btn btn-" style="background: #ffffff"> Edit password</button>--%>
                    <%--</form>--%>
                <%--</a>--%>
                <a class="nav-item2">
                    <form action="/logout" method="post">
                        <input type="submit" class="btn btn-danger" value="Logout"/>
                    </form>
                </a>
            </div>
        </div>
    </nav>
</div>
</br>
<div class="modal fade" id="edit_phone" tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editLabel"> New phone number </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="/driver/edit_telephoneNumber" method="post" class="formWithValidation" role="form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label" visibility: hidden for="idInput">ID</label>
                        <div class="col-sm-9">
                            <input type="text" class="id" visibility: hidden name="id" id="idInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="phoneInput">Phone Number</label>
                        <div class="col-sm-9">
                            <input type="text" class="telephone" name="telephone" id="phoneInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-10">
                            <button type="submit" class="saveBtn btn-success">Save</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<c:choose>
    <c:when test="${order==null}">
        <br/>
        <h6><strong> You are not assigned to any order. </strong></h6>
        <br/>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${order.status=='WAITING'}">
                <a class="nav-item3">
                    <button type="button" class="btn btn-success"
                            data-toggle="modal" data-target="#start_order"
                            data-order-id="${order.id}"> StartOrder
                    </button>
                </a>
            </c:when>
            <c:otherwise>
                <div id="loginbox" style="..." class="mainbox col-md-3 col-md-offset-2 col-sm-4 col-sm-offset-2">
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <div class="panel-title"><h5><u> The working process </u></h5></div>
                        </div>
                        <div style="..." class="panel-body">
                            <div class="info"><h5><strong>State:</strong> ${driver.driverState}</h5></div>
                            <div>
                                <button type="button" class="btn btn-secondary"
                                        data-toggle="modal" data-target="#edit_state"
                                        data-driver-state="${driver.driverState}"> Edit state
                                </button>
                                <button type="button" class="btn btn-success"
                                        data-toggle="modal" data-target="#finish_order"
                                        data-order-id="${order.id}"> Finish
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${driver.partners.size()==0}">
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${driver.partners.size() == 1}">
                        </br>
                        <div class="info"><h5>
                            <strong>Partner:</strong> ${driver.partners.get(0)}
                        </h5>
                        </div>
                    </c:when>
                    <c:otherwise>
                        </br>
                        <div class="info"><h5>
                            <strong>Partners:</strong> ${driver.partners.get(0)}
                            </br> ${driver.partners.get(1)}
                        </h5>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${order.truckNumber==null}">
            </c:when>
            <c:otherwise>
                </br>
                <div class="info"><h5><strong>Truck:</strong> ${order.truckNumber}</h5></div>
            </c:otherwise>
        </c:choose>
        </br>
        <div class="collapse" id="navbarToggleExternalContent">
            <div class="p-4" style="background: rgba(67,41,28,0.99)">
                <h5 class="text-white h4" align="center">Waypoints of order №${order.number}</h5>
                <span class="text-white">
                <table class="table">
                    <thead class="thead-light" align="center">
                            <tr>
                                <th scope="col" style="display:none; visibility:collapse"> CargoNumber </th>
                                <th scope="col"> City </th>
                                <th scope="col"> Cargo </th>
                                <th scope="col"> Weight </th>
                                <th scope="col"> Action </th>
                                <c:choose>
                                    <c:when test="${order.status=='IN_PROGRESS'}">
                                        <th scope="col">Status</th>
                                    </c:when>
                                </c:choose>
                                <th scope="col" style="display:none; visibility:collapse"> Sequence </th>
                            </tr>
                    </thead>
                    <tbody align="center">
                        <c:forEach var="waypoint" items="${order.waypoints}">
                            <tr id="waypoint-${waypoint.id}">
                                <td scope="row" style="display:none; visibility:collapse">${waypoint.cargoNumber}</td>
                                <td scope="row">${waypoint.cityName}</td>
                                <td scope="row">${waypoint.cargoName}</td>
                                <td scope="row">${waypoint.cargoWeight}</td>
                                <td scope="row">${waypoint.action}</td>
                                <c:choose>
                                    <c:when test="${order.status == 'IN_PROGRESS'}">
                                        <c:choose>
                                            <c:when test="${waypoint.status=='TODO'}">
                                                <td scope="row">
                                                    <button type="button" class="btn btn-success"
                                                            data-toggle="modal" data-target="#complete_waypoint"
                                                            data-waypoint-id="${waypoint.id}"> Done
                                                    </button>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td scope="row">${waypoint.status}</td>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                </c:choose>
                                <td scope="row" style="display:none; visibility:collapse">${waypoint.sequence}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </span>
            </div>
        </div>
        <nav class="navbar navbar-light" style="background: rgba(67,41,28,0.99)">
            <button class="navbar-toggler" type="button" data-toggle="collapse"
                    data-target="#navbarToggleExternalContent"
                    aria-controls="navbarToggleExternalContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
                <span class="text-white">Waypoints of order №${order.number}</span>
            </button>
        </nav>
        <div class="modal fade" id="complete_waypoint" tabindex="-1" role="dialog" aria-labelledby="allertCompleteWaypointModal"
             aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="allertCompleteWaypointModal">Complete Waypoint</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form action="/driver/complete_waypoint" method="post"
                              class="formWithValidation_waypoint" role="form">
                            Are you sure, you want to complete this waypoint?
                            <div class="form-group">
                                <label class="col-sm-3 control-label" visibility: hidden for="idInputWaypoint">Id</label>
                                <div class="col-sm-9">
                                    <input type="number" readonly visibility: hidden class="id field" name="id" id="idInputWaypoint"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" visibility: hidden for="cargoNumberInputWaypoint">cargoNumber</label>
                                <div class="col-sm-9">
                                    <input type="number" readonly class="number field" name="number" visibility: hidden id="cargoNumberInputWaypoint"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="cityInputWaypoint">City</label>
                                <div class="col-sm-9">
                                    <input type="text" readonly class="city field" name="city" id="cityInputWaypoint"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="cargoNameInputWaypoint">Cargo</label>
                                <div class="col-sm-9">
                                    <input type="text" readonly class="name field" name="name" id="cargoNameInputWaypoint"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="cargoWeightInputWaypoint">Weight</label>
                                <div class="col-sm-9">
                                    <input type="text" readonly class="weight field" name="weight" id="cargoWeightInputWaypoint"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="actionInputWaypoint">Action</label>
                                <div class="col-sm-9">
                                    <input type="text" readonly class="action field" name="action" id="actionInputWaypoint"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label" visibility: hidden for="sequenceInputWaypoint">Sequence</label>
                                <div class="col-sm-9">
                                    <input type="text" readonly class="sequence field" name="sequence" visibility: hidden id="sequenceInputWaypoint"/>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal"> No</button>
                                <button type="submit" class="btn btn-success"> Yes</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="start_order" tabindex="-1" role="dialog" aria-labelledby="allertStartModal"
             aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="allertStartModal">Finish working</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form action="/driver/change_order_status" method="post"
                              class="formWithValidation0" role="form">
                            Are you sure, you want to start order №${order.number}?
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal"> No</button>
                                <button type="submit" class="btn btn-success"> Yes</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="edit_state" tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editLabel2"> What are you going to do? </h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form action="/driver/edit_state" method="post" class="formWithValidation2" role="form">
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="stateInput">State</label>
                                <div class="col-sm-9">
                                    <select class="col-sm-20 state field" name="state" id="stateInput">
                                        <c:forEach var="state" items="${driverState}">
                                            <option value=${state.toString()}>${state.toString()}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-3 col-sm-10">
                                    <button type="submit" class="saveBtn btn-success">Save</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="finish_order" tabindex="-1" role="dialog" aria-labelledby="allertModalLabel"
             aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="allertModalLabel">Finish working</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form action="/driver/change_order_status" method="post"
                              class="formWithValidation3" role="form">
                            Are you sure, that order №${order.number} is completed?
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
    $("#edit_phone").on('show.bs.modal', function (e) {
        var telephone = $(e.relatedTarget).data('driver-telephone');
        $('#idInput').val(${driver.id})
        $('#phoneInput').val(telephone);
    });
    $("#edit_phone").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });

    var form = document.querySelector('.formWithValidation')
    var telephone = form.querySelector('.telephone')

    form.addEventListener("submit", function (event) {
        event.preventDefault()

        var errors = form.querySelectorAll('.error')

        for (var i = 0; i < errors.length; i++) {
            errors[i].remove()
        }

        var errors_counter = 0

        if (!telephone.value) {
            errors_counter += 1
            var error = document.createElement('div')
            error.className = 'error'
            error.style.color = 'red'
            error.innerHTML = 'Can`t be empty'
            form[i].parentElement.insertBefore(error, telephone)
        }

        if (!telephone.value.match("^\\d{1}[-]\\d{3}[-]\\d{3}[-]\\d{2}[-]\\d{2}$")) {
            errors_counter += 1
            var error2 = document.createElement('div')
            error2.className = 'error'
            error2.style.color = 'red'
            error2.innerHTML = 'Invalid format of telephone number'
            telephone.parentElement.insertBefore(error2, telephone)
        }

        if (errors_counter < 1) {
            var partners = null;
            if (!('${driver.partners}'.length < 1)) {
                partners = '${driver.partners}';
            }

            var order = null;
            if (!('${driver.orderNumber}'.length < 1)) {
                order = '${driver.orderNumber}';
            }

            var startWorkingTime = null;
            if (!('${driver.startWorkingTime}'.length < 1)) {
                startWorkingTime = '${driver.startWorkingTime}';
            }

            $.ajax({
                url: '/driver/edit_telephoneNumber',
                datatype: 'json',
                type: "POST",
                dataType: 'JSON',
                data: JSON.stringify({
                    id: ${driver.id},
                    name: '${driver.name}',
                    surname: "${driver.surname}",
                    telephoneNumber: telephone.value,
                    hoursThisMonth: ${driver.hoursThisMonth},
                    partners: partners,
                    driverState: '${driver.driverState}',
                    orderNumber: order,
                    startWorkingTime: startWorkingTime
                }),
                success : function(data) {
                    window.location.reload();
                },
                error : function(result) {
                    alert("error" + result.responseText);
                }
            });
        }
    });
</script>
<script>
    $("#edit_state").on('show.bs.modal', function (e) {
        var state = $(e.relatedTarget).data('driver-state');
        $('#stateInput').val(state);
    });
    $("#edit_state").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();

    });
    var form = document.querySelector('.formWithValidation2')
    var state = form.querySelector('.state')

    form.addEventListener("submit", function (event) {
        event.preventDefault()

        var partners = null;
        if (!('${driver.partners}'.length < 1)) {
            partners = '${driver.partners}';
        }

        var order = null;
        if (!('${driver.orderNumber}'.length < 1)) {
            order = '${driver.orderNumber}';
        }

        var startWorkingTime = null;
        if (!('${driver.startWorkingTime}'.length < 1)) {
            startWorkingTime = '${driver.startWorkingTime}';
        }

        $.ajax({
            url: '/driver/edit_state',
            datatype: 'json',
            type: "POST",
            dataType: 'JSON',
            data: JSON.stringify({
                id: ${driver.id},
                name: '${driver.name}',
                surname: "${driver.surname}",
                telephoneNumber: '${driver.telephoneNumber}',
                hoursThisMonth: ${driver.hoursThisMonth},
                partners: partners,
                driverState: state.value,
                orderNumber: order,
                startWorkingTime: startWorkingTime
            }),
            success : function(data) {
                window.location.reload();
            },
            error : function(result) {
                alert("error" + result.responseText);
            }
        });
    });
</script>
<script>
    $("#finish_order").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });
</script>
<script>
    $("#start_order").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });

    var form = document.querySelector('.formWithValidation0')

    form.addEventListener("submit", function (event) {
        event.preventDefault()

        $.ajax({
            url: '/driver/change_order_status',
            datatype: 'json',
            type: "POST",
            dataType: 'JSON',
            data: JSON.stringify({
                id: ${order.id},
                number: '${order.number}',
                truckNumber: '${order.truckNumber}',
                status: 'IN_PROGRESS'
            }),
            success : function(data) {
                window.location.reload();
            },
            error : function(result) {
                alert("error" + result.responseText);
            }
        });
    });
</script>
<script>
    $("#finish_order").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });

    var form = document.querySelector('.formWithValidation3')

    form.addEventListener("submit", function (event) {
        event.preventDefault()

        $.ajax({
            url: '/driver/change_order_status',
            datatype: 'json',
            type: "POST",
            dataType: 'JSON',
            data: JSON.stringify({
                id: ${order.id},
                number: '${order.number}',
                truckNumber: '${order.truckNumber}',
                status: 'COMPLETED'
            }),
            success : function(data) {
                alert("Congratulations! You've finished order.\nThank you for choosing our company.");
                window.location.reload();
            },
            error : function(result) {
                alert("error" + result.responseText);
            }
        });
    });
</script>
<script>
    $("#complete_waypoint").on('show.bs.modal', function (e) {
        var waypointId = $(e.relatedTarget).data('waypoint-id');
        var cols = $('#waypoint-' + waypointId + ' td');
        var id = waypointId;
        var cargoNumber = $(cols[0]).text();
        var cityName = $(cols[1]).text();
        var cargoName = $(cols[2]).text();
        var cargoWeight = $(cols[3]).text();
        var action = $(cols[4]).text();
        var sequence = $(cols[6]).text();
        $('#idInputWaypoint').val(id);
        $('#cargoNumberInputWaypoint').val(cargoNumber);
        $('#cityInputWaypoint').val(cityName);
        $('#cargoNameInputWaypoint').val(cargoName);
        $('#cargoWeightInputWaypoint').val(cargoWeight);
        $('#actionInputWaypoint').val(action);
        $('#sequenceInputWaypoint').val(sequence);
    });
    $("#complete_waypoint").on('hidden.bs.modal', function () {
        // alert("Изменения будут отменены");
        var form = $(this).find('form');
        form[0].reset();
    });

    var form = document.querySelector('.formWithValidation_waypoint')
    var id = form.querySelector('.id')
    var cargoNumber = form.querySelector('.number')
    var cityName = form.querySelector('.city')
    var cargoName = form.querySelector('.name')
    var cargoWeight = form.querySelector('.weight')
    var action = form.querySelector('.action')
    var sequence = form.querySelector('.sequence')

    form.addEventListener("submit", function (event) {
        event.preventDefault()

        $.ajax({
            url: '/driver/complete_waypoint',
            datatype: 'json',
            type: "POST",
            dataType: 'JSON',
            data: JSON.stringify({
                id: id.value,
                cargoNumber: cargoNumber.value,
                cargoName: cargoName.value,
                cargoWeight: cargoWeight.value,
                cityName: cityName.value,
                action: action.value,
                status: 'DONE',
                sequence: sequence.value
            }),
            success : function(data) {
                window.location.reload();
            },
            error : function(result) {
                alert(result.responseText);
            }
        });
    })
</script>
</html>