<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<head>
    <title>Trucks</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <style>
        html {
            height: 100%; /* Высота страницы */
        }

        body {
            background: rgb(255, 217, 240) url("../images/trucks.jpg"); /* путь к файлу */
            background-position: right;
            background-repeat: no-repeat;
            background-size: contain;
            color: #000;
        }

        .tableTab {
            width: 54%;
            height: 400px;
            overflow-x: auto;
            margin-left: 1rem;
        }

        .btn-success {
            margin-left: 1rem;
        }

        .nav-item {
            position: relative;
            left: 55em;
        }
    </style>
</head>
<body>
<div>
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: rgba(255,81,83,0.55)">
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a class="nav-link" href="/admin/"><strong>Главная </strong></a>
                <a class="nav-link active" href="/truck/info"><strong><u>Фуры </u></strong><span class="sr-only">(current)</span></a>
                <a class="nav-link" href="/drivers/info"><strong>Водители </strong></a>
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
    <a class="btn btn-success" href="/truck/create_truck">Create Truck</a>
    </br></br>
    <div class="mainDiv">
        <div class="tableTab">
            <table class="table">
                <h5 class="text-black h4" style="background: rgba(15,20,109,0.36)" align="center"> List of trucks </h5>
                <span class="text-black">
                    <thead style="background: rgba(15,20,109,0.36)" align="center">
                            <tr>
                                <th scope="col"> № </th>
                                <th scope="col"> Number </th>
                                <th scope="col"> Capacity </th>
                                <th scope="col"> Crew </th>
                                <th scope="col"> State </th>
                                <th scope="col"> Available </th>
                                <th scope="col"> Edit </th>
                                <th scope="col"> Delete </th>
                            </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="truck" items="${listOfTrucks}">
                            <tr id="truck-${truck.id}">
                                <td scope="row" align="center">${truck.id}</td>
                                <td scope="row" align="center">${truck.number}</td>
                                <td scope="row" align="center">${truck.capacity}</td>
                                <td scope="row" align="center">${truck.crewSize}</td>
                                <td scope="row" align="center">${truck.truckState}</td>
                                <td scope="row" align="center">${truck.order.id}</td>
                                <td scope="row" align="center"><button type="button" class="btn btn-secondary"
                                                                       data-toggle="modal" data-target="#edit_truck"
                                                                       data-truck-id="${truck.id}"> Edit </button>
                                <td scope="row" align="center"><a class="btn btn-danger"
                                                                  href="/truck/delete_truck/${truck.id}"> Delete </a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </span>
            </table>
        </div>
    </div>
</div>
<div class="modal fade" id="edit_truck" tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editLabel"> Edit Truck </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="numberInput">Number</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="numberInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="capacityInput">Capacity</label>
                        <div class="col-sm-9">
                            <input type="number" class="form-control" id="capacityInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="crewInput">Crew</label>
                        <div class="col-sm-9">
                            <input type="number" class="form-control" id="crewInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="stateInput">State</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="stateInput"/>
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
    $("#edit_truck").on('show.bs.modal', function (e) {
        var truckId = $(e.relatedTarget).data('truck-id');

        var cols = $('#truck-' + truckId + ' td');
        var number = $(cols[1]).text();
        var capacity = $(cols[2]).text();
        var crew = $(cols[3]).text();
        var state = $(cols[4]).text();

        $('#numberInput').val(number);
        $('#capacityInput').val(capacity);
        $('#crewInput').val(crew);
        $('#stateInput').val(state);
    });

    $("#edit_truck").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });
</script>
</html>
