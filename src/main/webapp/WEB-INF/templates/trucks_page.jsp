<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
    <a class="btn btn-success" data-toggle="modal" data-target="#create_truck">Create Truck</a>
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
                <form action="/truck/edit_truck/" method="get" class="formWithValidation" role="form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="idInput">ID</label>
                        <div class="col-sm-9">
                            <input type="number" readonly
                                   class="id field" name="id" id="idInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="numberInput">Number</label>
                        <div class="col-sm-9">
                            <input type="text" class="number field" name="number" id="numberInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="capacityInput">Capacity</label>
                        <div class="col-sm-9">
                            <input type="number" class="capacity field" name="capacity" id="capacityInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="crewInput">Crew</label>
                        <div class="col-sm-9">
                            <input type="number" maxlength="1" name="crew" class="crew field" id="crewInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="stateInput">State</label>
                        <div>
                            <select class="col-sm-6 field" name="state" id="stateInput">
                                <option value="OK">OK</option>
                                <option value="BROKEN">BROKEN</option>
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
<div class="modal fade" id="create_truck" tabindex="-1" aria-labelledby="createLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="createabel"> Create Truck </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="/truck/create_truck/" method="get" class="formCreateWithValidation" role="form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="input1">Number</label>
                        <div class="col-sm-9">
                            <input type="text" class="number field" name="number" id="input1"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="input2">Crew Size</label>
                        <div class="col-sm-9">
                            <input type="number" class="crew field" name="crew" id="input2"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="input3">Capacity</label>
                        <div class="col-sm-9">
                            <input type="number" class="capacity field" name="capacity" id="input3"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="input4">State</label>
                        <div>
                            <select class="col-sm-6 field" name="state" id="input4">
                                <option value="OK">OK</option>
                                <option value="BROKEN">BROKEN</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="input5">City</label>
                        <div>
                            <select class="col-sm-6 field" name="city" id="input5">
                                <option value="1">Пермь</option>
                                <option value="2">Уфа</option>
                                <option value="4">Ростов-на-Дону</option>
                                <option value="5">Омск</option>
                                <option value="6">Казань</option>
                                <option value="7">Екатеринбург</option>
                                <option value="8">Санкт-Петербург</option>
                                <option value="9">Тобольск</option>
                                <option value="10">Бердск</option>
                                <option value="11">Калининград</option>
                                <option value="12">Ярославль</option>
                                <option value="13">Благовещенск</option>
                                <option value="14">Астрахань</option>
                                <option value="15">Воркута</option>
                                <option value="16">Краснодар</option>
                                <option value="18">Махачкала</option>
                                <option value="19">Тамбов</option>
                                <option value="20">Самара</option>
                                <option value="21">Саратов</option>
                                <option value="22">Тюмень</option>
                                <option value="23">Челябинск</option>
                                <option value="24">Якутск</option>
                                <option value="25">Березники</option>
                                <option value="26">Люберцы</option>
                                <option value="27">Салехард</option>
                                <option value="28">Владимир</option>
                                <option value="29">Сочи</option>
                                <option value="30">Магадан</option>
                                <option value="31">Архангельск</option>
                                <option value="32">Кострома</option>
                                <option value="33">Волгоград</option>
                                <option value="34">Сыктывкар</option>
                                <option value="35">Вологда</option>
                                <option value="36">Москва</option>
                                <option value="37">Новосибирск</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-10">
                            <button type="submit" class="saveBtn2 btn-success">Save</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    $("#edit_truck").on('show.bs.modal', function (e) {
        var truckId = $(e.relatedTarget).data('truck-id');
        var cols = $('#truck-' + truckId + ' td');
        var id = $(cols[0]).text();
        var number = $(cols[1]).text();
        var capacity = $(cols[2]).text();
        var crew = $(cols[3]).text();
        var state = $(cols[4]).text();
        $('#idInput').val(id);
        $('#numberInput').val(number);
        $('#capacityInput').val(capacity);
        $('#crewInput').val(crew);
        $('#stateInput').val(state);
    });
    $("#edit_truck").on('hidden.bs.modal', function () {
        // alert("Изменения будут отменены");
        var form = $(this).find('form');
        form[0].reset();
    });

    var form = document.querySelector('.formWithValidation')
    var number = form.querySelector('.number')
    var capacity = form.querySelector('.capacity')
    var crew = form.querySelector('.crew')
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
        if (!number.value.match("[A-Z]{2}[0-9]{5}")) {
            errors_counter += 1
            var error2 = document.createElement('div')
            error2.className = 'error'
            error2.style.color = 'red'
            error2.innerHTML = '2 English letters in UpperCase and 5 numbers'
            number.parentElement.insertBefore(error2, number)
        }

        if (capacity.value < 15 || capacity.value > 30) {
            errors_counter += 1
            var error3 = document.createElement('div')
            error3.className = 'error'
            error3.style.color = 'red'
            error3.innerHTML = 'should be between 15 and 30'
            capacity.parentElement.insertBefore(error3, capacity)
        }

        if (crew.value < 2 || crew.value > 3) {
            errors_counter += 1
            var error4 = document.createElement('div')
            error4.className = 'error'
            error4.style.color = 'red'
            error4.innerHTML = 'Can be 2 or 3'
            crew.parentElement.insertBefore(error4, crew)
        }

        if (errors_counter < 1) {
            form.submit()
        }
    })
</script>
<script>
    $("#create_truck").on('show.bs.modal', function (e) {
        $('#input1').val(null);
        $('#input2').val(null);
        $('#input3').val(null);
        $('#input4').val(null);
        $('#input5').val(null);
    });
    $("#create_truck").on('hidden.bs.modal', function () {
        // alert("Изменения будут отменены");
        var form = $(this).find('form');
        form[0].reset();
    });

    var form2 = document.querySelector('.formCreateWithValidation')
    var number2 = form2.querySelector('.number')
    var capacity2 = form2.querySelector('.capacity')
    var crew2 = form2.querySelector('.crew')
    var fields2 = form2.querySelectorAll('.field')

    form2.addEventListener("submit", function (event) {
        event.preventDefault()

        var errors2 = form2.querySelectorAll('.error')

        for (var i = 0; i < errors2.length; i++) {
            errors2[i].remove()
        }

        var errors_counter2 = 0
        for (var i = 0; i < fields2.length; i++) {
            if (!fields2[i].value) {
                errors_counter2 += 1
                var error5 = document.createElement('div')
                error5.className = 'error'
                error5.style.color = 'red'
                error5.innerHTML = 'Can`t be empty'
                form2[i].parentElement.insertBefore(error5, fields2[i])
            }
        }
        if (!number2.value.match("[A-Z]{2}[0-9]{5}")) {
            errors_counter2 += 1
            var error6 = document.createElement('div')
            error6.className = 'error'
            error6.style.color = 'red'
            error6.innerHTML = '2 English letters in UpperCase and 5 numbers'
            number2.parentElement.insertBefore(error6, number2)
        }

        if (capacity2.value < 15 || capacity2.value > 30) {
            errors_counter2 += 1
            var error7 = document.createElement('div')
            error7.className = 'error'
            error7.style.color = 'red'
            error7.innerHTML = 'should be between 15 and 30'
            capacity2.parentElement.insertBefore(error7, capacity2)
        }

        if (crew2.value < 2 || crew2.value > 3) {
            errors_counter2 += 1
            var error8 = document.createElement('div')
            error8.className = 'error'
            error8.style.color = 'red'
            error8.innerHTML = 'Can be 2 or 3'
            crew2.parentElement.insertBefore(error8, crew2)
        }

        if (errors_counter2 < 1) {
            form2.submit()
        }
    })
</script>
</html>