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
            width: 50%;
            height: 400px;
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
                <h5 class="text-black h4" style="background: rgba(150,214,132,0.93)" align="center"> Details of order № ${order}  </h5>
                <span class="text-black">
                    <thead style="background: rgba(150,214,132,0.93)" align="center">
                            <tr>
                                <th scope="col"> № </th>
                                <th scope="col"> Cargo </th>
                                <th scope="col"> Weight </th>
                                <th scope="col"> City </th>
                                <th scope="col"> Action </th>
                                <th scope="col"> Status </th>
                                <th scope="col"> Edit </th>
                            </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="waypoint" items="${waypoints}" varStatus="loop">
                            <tr id="waypoint-${waypoint.id}">
                                <td scope="row" align="center">${loop.count}</td>
                                <td scope="row" align="center">${waypoint.cargo.name}</td>
                                <td scope="row" align="center">${waypoint.cargo.weight}</td>
                                <td scope="row" align="center">${waypoint.city.name}</td>
                                <td scope="row" align="center">${waypoint.action}</td>
                                <td scope="row" align="center">${waypoint.status}</td>
                                <td scope="row" align="center"><button type="button" class="btn btn-secondary"
                                                                       data-toggle="modal" data-target="#edit_waypoint"
                                        <c:if test="${(order_status=='IN_PROGRESS')or
                                                                        (order_status=='COMPLETED')or(order_status=='WAITING')}">
                                            <c:out value="disabled='disabled'"/></c:if>
                                                                       data-waypoint-id="${waypoint.id}"> Edit </button>
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
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="cargoInput">Cargo</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="cargoInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="weightInput">Weight</label>
                        <div class="col-sm-9">
                            <input type="number" class="form-control" id="weightInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="cityInput">City</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="cityInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="actionInput">Action</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="actionInput"/>
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
    $("#edit_waypoint").on('show.bs.modal', function (e) {
        var waypointId = $(e.relatedTarget).data('waypoint-id');
        var cols = $('#waypoint-' + waypointId + ' td');
        var cargo = $(cols[1]).text();
        var weight = $(cols[2]).text();
        var city = $(cols[3]).text();
        var action = $(cols[4]).text();
        $('#cargoInput').val(cargo);
        $('#weightInput').val(weight);
        $('#cityInput').val(city);
        $('#actionInput').val(action);
    });
    $("#edit_waypoint").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });
</script>
</html>
