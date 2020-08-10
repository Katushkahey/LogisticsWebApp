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

        .nav-item {
            position: relative;
            left: 55em ;
        }

    </style>
</head>
<body>
<div>
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: #ffcc88">
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a class="nav-link" href="/admin/"><strong>Главная </strong></a>
                <a class="nav-link" href="/truck/info"><strong>Фуры </strong></a>
                <a class="nav-link active" href="/drivers/info"><strong><u>Водители </u></strong><span
                        class="sr-only">(current)</span></a>
                <a class="nav-link" href="/order/info"><strong>Заказы </strong></a>
                <a class="nav-item">
                    <form action="/logout" method="post">
                        <input type="submit" class="btn btn-danger" value="Logout"/>
                    </form>
                </a>
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
                            <tr id="driver-${driver.id}">
                                <td scope="row" align="center">${driver.id}</td>
                                <td scope="row" align="center">${driver.name}</td>
                                <td scope="row" align="center">${driver.surname}</td>
                                <td scope="row" align="center">${driver.telephoneNumber}</td>
                                <td scope="row" align="center">${driver.hoursThisMonth}</td>
                                <td scope="row" align="center">${driver.currentCity.name}</td>
                                <td scope="row" align="center">${driver.currentOrder.id}</td>
                                <td scope="row" align="center"><button type="button" class="btn btn-secondary"
                                                                       data-toggle="modal" data-target="#edit_driver"
                                                                       data-driver-id="${driver.id}"> Edit </button>
                                <td scope="row" align="center"><a class="btn btn-danger"
                                                                  href="/drivers/delete_driver/${driver.id}"> Delete </a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </span>
            </table>
        </div>
    </div>
</div>
<div class="modal fade" id="edit_driver" tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editLabel"> Edit Driver </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="nameInput">Name</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="nameInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="surnameInput">Surname</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="surnameInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="telephoneInput">Telephone</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="telephoneInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="cityInput">City</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="cityInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-10">
                            <button type="submit" class="btn btn-default">Save</button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    $("#edit_driver").on('show.bs.modal', function (e) {
        var driverId = $(e.relatedTarget).data('driver-id');
        var cols = $('#driver-' + driverId + ' td');
        var name = $(cols[1]).text();
        var surname = $(cols[2]).text();
        var telephone = $(cols[3]).text();
        var city = $(cols[5]).text();
        $('#nameInput').val(name);
        $('#surnameInput').val(surname);
        $('#telephoneInput').val(telephone);
        $('#cityInput').val(city);
    });
    $("#edit_driver").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });
</script>
</html>