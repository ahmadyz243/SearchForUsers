$(document).ready(function () {
    $("#result-table").hide();
    $("#addNewPersonForm").submit(function (event){
        event.preventDefault();
        var person = {
            firstname: $("input[name=firstname]").val(),
            lastname: $("input[name=lastname]").val(),
            age: $("input[name=age]").val(),
            score: $("input[name=score]").val(),
            gender: $('input[name="gender"]:checked').val(),
            mobileNumber: $("input[name=mobileNumber]").val()
        }
        savePerson(person);
        $(':input','#addNewPersonForm')
            .not(':button, :submit, :reset, :hidden')
            .val('')
            .prop('checked', false)
            .prop('selected', false);
        $("#male").val("MALE");
        $("#female").val("FEMALE");
    })

    $("#nameStartsWitha").click(function (){
        var people = findPeopleWithA();
        viewResults(people);
    })
    $("#lastnameEndsWithC").click(function (){
        var people = findPeopleTheirLastnameEndsWithC();
        viewResults(people);
    })
    $("#fOrLnameContainsBAndAgeGreaterThanEighteen").click(function (){
        var people = firstOrLastnameContainsBAndAgeMoreThanEighteen();
        viewResults(people);
    })
    $("#findAllPeople").click(function (){
        var people = findAllPeople();
        viewResults(people);
    })
    $("#peopleGroupByAge").click(function (){
        var people = countPeopleGroupByAge();
        viewPeopleCountGroupByAge(people);
    })

    function savePerson(person){
        $.ajax({
            url: "/api/add-person",
            method: "PUT",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(person),
            success: function (response) {
                alert("saved successfully");
            },
            error: function (erorMessage) {
                alert("failed...");
                console.log(erorMessage);
            }
        })
    }
    function viewResults(people){
        $("#avg").html("average ages: " + getAvgAge());
        $("#young").html("&nbsp;  youngest: " + getYoungest());
        $("#oldest").html("&nbsp;  oldest: " + getOldest());
        $("#greaterThanEighteen").html(countPeopleGreaterThanEighteen() + " people are greater than 18 years old").css("color", "#00FFFFFF");
        $("#result-table").show();
        $("#result-table").html("");
        $("#result-table").append("<tr>\n" +
            "                <th>firstname</th>\n" +
            "                <th>lastname</th>\n" +
            "                <th>age</th>\n" +
            "                <th>gender</th>\n" +
            "                <th>mobile number</th>\n" +
            "                <th>score</th>\n" +
            "            </tr>");
        for (let i = 0; i < people.length; i++) {
            $("#result-table").append("<tr>\n" +
                "                <td>" + people[i].firstname + "</td>\n" +
                "                <td>" + people[i].lastname + "</td>\n" +
                "                <td>" + people[i].age + "</td>\n" +
                "                <td>" + people[i].gender + "</td>\n" +
                "                <td>" + people[i].mobileNumber + "</td>\n" +
                "                <td>" + people[i].score + "</td>\n" +
                "            </tr>");
        }
    }
    function findPeopleWithA(){
        var people = [];
        $.ajax({
            url: "/api/people-with-a",
            method: "GET",
            contentType: "application/json",
            async: false,
            dataType: "json",
            success: function (response) {
                people = response;
            },
            error: function (erorMessage) {
                console.log(erorMessage);
            }
        })
        return people;
    }
    function findPeopleTheirLastnameEndsWithC(){
        var people = [];
        $.ajax({
            url: "/api/people-with-c",
            method: "GET",
            contentType: "application/json",
            async: false,
            dataType: "json",
            success: function (response) {
                people = response;
            },
            error: function (erorMessage) {
                console.log(erorMessage);
            }
        })
        return people;
    }
    function firstOrLastnameContainsBAndAgeMoreThanEighteen(){
        var people = [];
        $.ajax({
            url: "/api/name-contains-b-and-age-greater-eighteen",
            method: "GET",
            contentType: "application/json",
            async: false,
            dataType: "json",
            success: function (response) {
                people = response;
            },
            error: function (erorMessage) {
                console.log(erorMessage);
            }
        })
        return people;
    }
    function findAllPeople(){
        var people = [];
        $.ajax({
            url: "/api/find-all-people",
            method: "GET",
            contentType: "application/json",
            async: false,
            dataType: "json",
            success: function (response) {
                people = response;
            },
            error: function (erorMessage) {
                console.log(erorMessage);
            }
        })
        return people;
    }
    function getAvgAge(){
        var avg = 0;
        $.ajax({
            url: "/api/get-avg-age",
            method: "GET",
            contentType: "application/json",
            async: false,
            dataType: "json",
            success: function (response) {
                avg = response;
            },
            error: function (erorMessage) {
                console.log(erorMessage);
            }
        })
        return avg;
    }
    function getYoungest(){
        var youngest = "";
        $.ajax({
            url: "/api/get-youngest",
            method: "GET",
            contentType: "application/json",
            async: false,
            dataType: "json",
            success: function (response) {
                youngest = response[0].firstname + " " + response[0].lastname;
            },
            error: function (erorMessage) {
                console.log(erorMessage);
            }
        })
        return youngest;
    }

    function getOldest(){
        var oldest = "";
        $.ajax({
            url: "/api/get-oldest",
            method: "GET",
            contentType: "application/json",
            async: false,
            dataType: "json",
            success: function (response) {
                oldest = response.firstname + " " + response.lastname;
            },
            error: function (erorMessage) {
                console.log(erorMessage);
            }
        })
        return oldest;
    }

    function countPeopleGreaterThanEighteen(){

    }

    function countPeopleGroupByAge(){
        var entities = [];
        $.ajax({
            url: "/api/count-people-group-by-age",
            method: "GET",
            contentType: "application/json",
            async: false,
            dataType: "json",
            success: function (response) {
                entities = response;
            },
            error: function (erorMessage) {
                console.log(erorMessage);
            }
        })
        return entities;
    }
    function viewPeopleCountGroupByAge(people){
        $("#result-table").show();
        $("#result-table").html("");
        $("#result-table").append("<tr>\n" +
            "                <th>count of people</th>\n" +
            "                <th>age</th>\n" +
            "            </tr>");
        for (let i = 0; i < people.length; i++) {
            $("#result-table").append("<tr>\n" +
                "                <td>" + people[i].count + "</td>\n" +
                "                <td>" + people[i].age + "</td>\n" +
                "            </tr>");
        }
    }
})