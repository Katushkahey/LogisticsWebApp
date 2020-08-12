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
            width: 60%;
            height: 400px;
            overflow-y: auto;
            overflow-x: auto;
            margin-left: 1rem;
        }

        .btn-success {
            margin-left: 1rem;
        }

        .nav-item {
            position: absolute;
            top: 0.3rem;
            right: 0.5rem;
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
    <a class="btn btn-success" data-toggle="modal" data-target="#create_truck">Create Driver</a>
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
                <form action="/drivers/edit_driver/" method="get" class="formWithValidation" role="form">
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
                            <input type="text" class="name2 field" name="name" id="nameInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="surnameInput">Surname</label>
                        <div class="col-sm-9">
                            <input type="text" class="surname field" name="surname" id="surnameInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="telephoneInput">Telephone</label>
                        <div class="col-sm-9">
                            <input type="text" class="telephone field" name="telephoneNumber" id="telephoneInput"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="cityInput">City</label>
                        <div>
                            <select class="col-sm-6 city field" name="city" id="cityInput">
                                <c:forEach var="city" items="${listOfCities}">
                                    <option value=${city.name}>${city.name}</option>
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
<div class="modal fade" id="create_truck" tabindex="-1" aria-labelledby="createLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="createabel"> Create Driver </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="/drivers/create_driver/" method="get" class="formCreateWithValidation" role="form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="input1">Name</label>
                        <div class="col-sm-9">
                            <input type="text" class="name field" name="name" id="input1"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="input2">Surname</label>
                        <div class="col-sm-9">
                            <input type="text" class="surname field" name="surname" id="input2"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="input3">Telephone number</label>
                        <div class="col-sm-9">
                            <input type="text" class="telephone field" name="telephone" id="input3"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="input4">City</label>
                        <div>
                            <select class="col-sm-6 city field" name="city" id="input4">
                                <option></option>
                                <c:forEach var="city" items="${listOfCities}">
                                    <option value=${city.name}>${city.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="input5">UserName</label>
                        <div class="col-sm-9">
                            <input type="text" class="user field" name="userName" id="input5"/>
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
    $("#edit_driver").on('show.bs.modal', function (e) {
        var driverId = $(e.relatedTarget).data('driver-id');
        var cols = $('#driver-' + driverId + ' td');
        var id = $(cols[0]).text();
        var name = $(cols[1]).text();
        var surname = $(cols[2]).text();
        var telephone = $(cols[3]).text();
        var city = $(cols[5]).text();
        $('#idInput').val(id);
        $('#nameInput').val(name);
        $('#surnameInput').val(surname);
        $('#telephoneInput').val(telephone);
        $('#cityInput').val(city);
    });
    $("#edit_driver").on('hidden.bs.modal', function () {
        var form = $(this).find('form');
        form[0].reset();
    });
    var form = document.querySelector('.formWithValidation')
    var name2 = form.querySelector('.name2')
    var surname = form.querySelector('.surname')
    var telephone = form.querySelector('.telephone')
    var city = form.querySelector('.city')
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
        if (!name2.value.match("^[A-ZА-Я'][a-zа-я-' ]+[a-zа-я']?$")) {
            errors_counter += 1
            var error2 = document.createElement('div')
            error2.className = 'error'
            error2.style.color = 'red'
            error2.innerHTML = 'No numbers. First letter in upper case'
            name2.parentElement.insertBefore(error2, name2)
        }

        if (!surname.value.match("^[A-ZА-Я'][a-zа-я-' ]+[a-zа-я']?$")) {
            errors_counter += 1
            var error3 = document.createElement('div')
            error3.className = 'error'
            error3.style.color = 'red'
            error3.innerHTML = 'No numbers. First letter in upper case'
            surname.parentElement.insertBefore(error3, surname)
        }

        if (!telephone.value.match("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")) {
            errors_counter += 1
            var error4 = document.createElement('div')
            error4.className = 'error'
            error4.style.color = 'red'
            error4.innerHTML = 'Invalid format of telephone number'
            telephone.parentElement.insertBefore(error4, telephone)
        }

        if (errors_counter < 1) {
            form.submit()
        }
    })
</script>
<script>
    $("#create_driver").on('show.bs.modal', function (e) {
        $('#input1').val(null);
        $('#input2').val(null);
        $('#input3').val(null);
        $('#input4').val(null);
        $('#input5').val(null);
    });
    $("#create_driver").on('hidden.bs.modal', function () {
        // alert("Изменения будут отменены");
        var form = $(this).find('form');
        form[0].reset();
    });

    var form2 = document.querySelector('.formCreateWithValidation')
    var name3 = form2.querySelector('.name')
    var surname2 = form2.querySelector('.surname')
    var telephone2 = form2.querySelector('.telephone')
    var userName = form2.querySelector('.user')
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
        if (!name3.value.match("^[A-ZА-Я'][a-zа-я-']+[a-zа-я']?$")) {
            errors_counter2 += 1
            var error6 = document.createElement('div')
            error6.className = 'error'
            error6.style.color = 'red'
            error6.innerHTML = 'No numbers. First letter in upper case'
            name3.parentElement.insertBefore(error6, name3)
        }

        if (!surname2.value.match("^[A-ZА-Я'][a-zа-я-']+[a-zа-я']?$")) {
            errors_counter2 += 1
            var error7 = document.createElement('div')
            error7.className = 'error'
            error7.style.color = 'red'
            error7.innerHTML = 'No numbers. First letter in upper case'
            surname2.parentElement.insertBefore(error7, surname2)
        }

        if (!telephone2.value.match("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")) {
            errors_counter2 += 1
            var error8 = document.createElement('div')
            error8.className = 'error'
            error8.style.color = 'red'
            error8.innerHTML = 'Invalid format of telephone number'
            telephone2.parentElement.insertBefore(error8, telephone2)
        }

        if (!userName.value.match("[a-z']+[a-z0-9']?$")) {
            errors_counter2 += 1
            var error9 = document.createElement('div')
            error9.className = 'error'
            error9.style.color = 'red'
            error9.innerHTML = 'Only English letters in lower case'
            userName.parentElement.insertBefore(error9, userName)
        }

        if (errors_counter2 < 1) {
            form2.submit()
        }
    })
</script>
</html>