$(document).ready(function () {
    var studentRequests = [];
    var masterRequests = [];
    var studentSignUpRequest = "";
    $(document).on("submit", "#loginform", function (event) {
        event.preventDefault();
        var username = $("#user").val();
        var password = $("#pass").val();
        console.log(username + password + JSON.stringify(loginRequest))
        var loginRequest = {
            username: username,
            password: password
        }

        $.ajax({
            url: "/api/auth/signin",
            method: "POST",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(loginRequest),
            success: function (response) {
                if (response[0] === "ROLE_ADMIN") {
                    window.location.href = "/admin-menu";
                } else if (response[0] === "ROLE_STUDENT") {
                    window.location.href = "/student-menu";
                } else if (response[0] === "ROLE_TEACHER") {
                    window.location.href = "/master-menu";
                }

            },
            error: function (erorMessage) {
                console.log(erorMessage);
            }
        })
    })
    //ADMIN ------------------------------------------------------------------------------------------------

    //view signup requests
    $("#viewSignupRequests").click(function () {
        var title = "<div id=\"requestsContaiter\">\
            <div class=\"requests\">\
                <h3>Students Requests</h3>";
        studentSignUpRequest = "";
        studentRequests = [];
        masterRequests = [];
        $.ajax({
            url: "/api/admin/user/not-actives",
            method: "GET",
            contentType: "application/json",
            dataType: "json",
            success: function (response) {
                for (let i = 0; i < response.length; i++) {
                    if (response[i].roles[0] === "ROLE_TEACHER") {
                        masterRequests.push(response[i])
                    } else {
                        studentRequests.push(response[i])
                    }
                }

                for (let i = 0; i < studentRequests.length; i++) {
                    studentSignUpRequest = studentSignUpRequest +
                        "<div class=\"signupRequest\"> <div class=\"details\">ahmad yazdi</div><button value=\"\">deny</button><button value=\"\">accept</button></div>"

                }
                studentSignUpRequest = studentSignUpRequest + "<div class=\"signupRequest\">\
                </div>\
            </div>\
            <div class=\"requests\">\
                <h3>Masters Requests</h3>\
            </div>\
        </div>\
        </div>";
                title = title + studentSignUpRequest;
            },
            error: function (erorMessage) {
                console.log(erorMessage);
            }
        })


        $("article").html(
            title
        );
        /*
        //url
        var u = "";
        var x;
        $ajax({
            url: u,
            type: "GET",
            beforeSend: function(){
                console.log("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
            },
            //-----------
            success: function(response){
                x = response.items;
                console.log(response.items);
            }
        })
        */
    })

    $("#denyRequest").click(function () {
        /*
        //url
        var u = "";
        var x;
        $ajax({
            url: u,
            type: "GET",
            beforeSend: function(){
                console.log("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
            },
            //-----------
            success: function(response){
                x = response.items;
                console.log(response.items);
                alert("request removed...");
            }
        })
        */
    })

    $("#acceptRequest").click(function () {
        /*
        //url
        var u = "";
        var x;
        $ajax({
            url: u,
            type: "GET",
            beforeSend: function(){
                console.log("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
            },
            //-----------
            success: function(response){
                x = response.items;
                console.log(response.items);
                alert("request accepted...");
            }
        })
        */
    })

    $("#addNewCourse").click(function () {

        $("article").html("\
        <h2>Add a Course</h2>\
        <form action=\"\" method=\"\">\
            <input type=\"text\" name=\"title\" placeholder=\"title\">\
            <span class=\"tip\">choose a master</span>\
            <select name=\"master\">\
                <option value=\"\">hasan</option>\
                <option value=\"\">ali</option>\
                <option value=\"\">abbas</option>\
                <option value=\"\">zahra</option>\
            </select>\
            <input type=\"submit\" value=\"add\">\
        </form>\
        ");

        /*
        //url
        var u = "";
        var x;
        $ajax({
            url: u,
            type: "GET",
            beforeSend: function(){
                console.log("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
            },
            //-----------
            success: function(response){
                x = response.items;
                console.log(response.items);
                alert("request accepted...");
            }
        })
        */
    })

    $("#searchByFields").click(function () {
        $("article").html("\
        <h2>search for users</h2>\
        <form action=\"\" method=\"post\">\
            <input type=\"text\" name=\"firstname\" placeholder=\"firstname\">\
            <input type=\"text\" name=\"lastname\" placeholder=\"lastname\">\
            <input class=\"radio\" type=\"radio\" name=\"userType\" value=\"student\"><span class=\"tip\">student</span>\
            <input class=\"radio\" type=\"radio\" name=\"userType\" value=\"master\"><span class=\"tip\">master</span>\
            <input type=\"submit\" value=\"search\">\
        </form>\
        ")
    })


    $("#viewCourses").click(function () {
        $("article").html("\
        <div class=\"coursesContainer\">\
            <div class=\"courses\">\
                <h3>Courses</h3>\
                <div class=\"titles\">\
                    <span>title</span><span>begin date</span><span>finish date</span>\
                </div>\
                <div class=\"course\">\
                    <span>java</span><span>1/1/1</span><span>2/2/2</span>\
                </div>\
            </div>\
            <div class=\"courseDetails courseSpec\">\
                <h3>Master</h3>\
                <div class=\"courseDetail\">\
                    <div class=\"name\">firstname lastname</div>\
                    <button id=\"removeMasterFromCourse\" value=\"\">change master</button>\
                </div>\
                <h3>Students</h3>\
                <div class=\"courseDetail\">\
                    <div class=\"name\">ahmad yazdi</div>\
                    <button id=\"removeStudentFromCourse\" value=\"\">remove from course</button>\
                </div>\
            </div>\
            <div class=\"courseDetails courseSpec\">\
                <button id=\"addNewStudentToCourse\"><b>add another student</b></button>\
                <div class=\"courseDetail\">\
                    <div class=\"details\">ahmad yazdi</div>\
                </div>\
            </div>\
        </div>\
        ")
        $(".courseSpec").hide();
        $(".course").click(function () {
            $(".courseSpec").show();
        })
    })
    // LOGOUT----------------------------------------------------------------------------
    $("#logout").click(function () {
        $.ajax({
            url: "/api/auth/signout",
            method: "POST",
            contentType: "application/json",
            dataType: "json",
            success: function (response) {
                window.location.href = "/login";
            }
        })

    })

    // MASTER ---------------------------------------------------------------------------
    $(".masterCourse").hover(function () {
        $(this).css("background", "rgb(2, 45, 2)");
    }, function () {
        $(this).css("background", "rgb(0, 128, 0)");
    });

    $(".masterCourse").click(function () {
        $("article").html("<p>hiiiiiiiiiiiiiiiiiiii</p>")
    })
})