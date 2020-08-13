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
        ;
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
                    <form data-toggle="modal" data-target="#edit_phone" data-driver-telephone="${driver.telephoneNumber}">
                        <button type="button"class="btn btn-" style="background: #e7ecf0"> Edit phone </button>
                    </form>
                </a>
                <a class="nav-item3">
                    <form data-toggle="modal" data-target="#edit_password">
                        <button type="button"class="btn btn-" style="background: #ffffff"> Edit password </button>
                    </form>
                </a>
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
<c:choose>
    <c:when test="${driver.currentOrder.id==null}">
        <br />
        <h6><strong> You are not assigned to any order. </strong></h6>
        <br />
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
                            data-driver-state="${driver.driverState}"> Edit state </button>
                    <button type="button" class="btn btn-success"
                            data-toggle="modal" data-target="#finish_order"
                            data-driver-order="${driver.currentOrder.id}"> Finish </button>
                </div>
            </div>
        </div>
    </div>
    <c:choose>
        <c:when test="${partner==null}">
        </c:when>
        <c:otherwise>
            </br>
            <div class="info"><h5><strong>Partner:</strong> ${partner.name} ${partner.surname}, ${partner.telephoneNumber}</h5>
            </div>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${driver.currentOrder.orderTruck==null}">
        </c:when>
        <c:otherwise>
            </br>
            <div class="info"><h5><strong>Truck:</strong> ${driver.currentOrder.orderTruck.number}</h5></div>
            </div>
        </c:otherwise>
    </c:choose>
    </br>
    <div class="collapse" id="navbarToggleExternalContent">
        <div class="p-4" style="background: rgba(67,41,28,0.99)">
            <h5 class="text-white h4" align="center">Waypoints of order №${driver.currentOrder.id}</h5>
            <span class="text-white">
                <table class="table">
                    <thead class="thead-light" align="center">
                            <tr>
                                <th scope="col"> City </th>
                                <th scope="col"> Cargo </th>
                                <th scope="col"> Weight </th>
                                <th scope="col"> Action </th>
                                <th scope="col">Status</th>
                            </tr>
                    </thead>
                    <tbody align="center">
                        <c:forEach var="waypoint" items="${waypoints}">
                            <tr>
                                <td scope="row"> ${waypoint.city.name} </td>
                                <td scope="row"> ${waypoint.cargo.name} </td>
                                <th scope="row"> ${waypoint.cargo.weight}</th>
                                <td scope="row"> ${waypoint.action.name()} </td>
                                <td>
                                    <div class="input-group-prepend">
                                        <div class="input-group-text">
                                            <input type="checkbox" aria-label="Checkbox for following text input">
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </span>
        </div>
    </div>
    <nav class="navbar navbar-light" style="background: rgba(67,41,28,0.99)">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarToggleExternalContent"
                aria-controls="navbarToggleExternalContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
            <span class="text-white">Waypoints of order №${driver.currentOrder.id}</span>
        </button>
    </nav>
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
                    <form action="/driver/edit_telephoneNumber/${driver.id}" method="get" class="formWithValidation" role="form">
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
    </body><div class="modal fade" id="edit_state" tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editLabel2"> What are you going to do? </h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form action="/driver/edit_state/${driver.id}" method="get" class="formWithValidation2" role="form">
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="stateInput">State</label>
                            <div class="col-sm-9">
                                <select class="col-sm-20 state field" name="state" id="stateInput">
                                    <c:forEach var="state" items="${drierState}">
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
    <div class="modal fade" id="finish_order" tabindex="-1" role="dialog" aria-labelledby="allertModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="allertModalLabel">Finish working</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form action="/driver/finish_order/${driver.currentOrder.id}" method="get" class="formWithValidation3" role="form">
                        Are you sure, that order №${driver.currentOrder.id} is completed?
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal"> No </button>
                            <button type="submit" class="btn btn-success"> Yes </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    </c:otherwise>
</c:choose>
<script>
    $("#edit_phone").on('show.bs.modal', function (e) {
        var telephone = $(e.relatedTarget).data('driver-telephone');
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

        if (!telephone.value.match("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")) {
            errors_counter += 1
            var error = document.createElement('div')
            error.className = 'error'
            error.style.color = 'red'
            error.innerHTML = 'Invalid format of telephone number'
            telephone.parentElement.insertBefore(error, telephone)
        }

        if (errors_counter < 1) {
            form.submit()
        }
    })
</script>
<script>
    $("#edit_state").on('show.bs.modal', function (e) {
        var state = $(e.relatedTarget).data('driver-state');
        $('#phoneInput').val(state);
    });
    $("#edit_state").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });
</script>
<script>
    // $("#finish_order").on('show.bs.modal', function (e) {
    //     var state = $(e.relatedTarget).data('driver-state');
    //     $('#phoneInput').val(state);
    // });
    $("#finish_order").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });
</script>
</html>