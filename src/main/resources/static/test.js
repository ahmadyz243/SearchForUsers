$(document).ready(function () {
        var studentRequests = [];
        var masterRequests = [];
        var studentSignUpRequest = "";
        var masterRequest = "";
        $(document).on("submit", "#loginform", function (event) {
            event.preventDefault();
            var username = $("#user").val();
            var password = $("#pass").val();
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
        //SIGNUP---------------------------------------------------------
        $("#signup").click(function (event) {
            event.preventDefault();
            var name = $('input[name="name"]').val();
            var lastname = $('input[name="lastname"]').val();
            var username = $('input[name="username"]').val();
            var nationalCode = $('input[name="nationalCode"]').val();
            var password = $('input[name="password"]').val();
            var role = $('input[name="role"]:checked').val();
            var user = {
                name: name,
                lastname: lastname,
                username: username,
                nationalCode: nationalCode,
                password: password,
                role: [role]
            }
            console.log(user);
            $.ajax({
                url: "/api/auth/signup",
                method: "POST",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(user),
                success: function (response) {
                    window.location.href = "/login";
                    alert("signed up successfully")
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
        })
        //ADMIN ------------------------------------------------------------------------------------------------

        //view signup requests
        $("#viewSignupRequests").click(function () {
            viewSignUpRequest();
        })

        function viewSignUpRequest() {
            var title = "<div id=\"requestsContainer\">\
            <div class=\"requests\">\
                <h3>Students Requests</h3>";
            studentSignUpRequest = "";
            masterRequest = "";
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
                    for (let i = 0; i < masterRequests.length; i++) {
                        masterRequest = masterRequest +
                            "<div class=\"signupRequest\"> " +
                            "<div class=\"details\">" + masterRequests[i].name + " " + masterRequests[i].lastname + "</div>" +
                            "<button class=\"denyReq\" value=\"" + masterRequests[i].id + "\">deny</button>" +
                            "<button class=\"acceptReq\" value=\"" + masterRequests[i].id + "\">accept</button></div>"

                    }
                    for (let i = 0; i < studentRequests.length; i++) {
                        studentSignUpRequest = studentSignUpRequest +
                            "<div class=\"signupRequest\"> " +
                            "<div class=\"details\">" + studentRequests[i].name + " " + studentRequests[i].lastname + "</div>" +
                            "<button class=\"denyReq\" value=\"" + studentRequests[i].id + "\">deny</button>" +
                            "<button class=\"acceptReq\" value=\"" + studentRequests[i].id + "\">accept</button></div>"
                    }
                    studentSignUpRequest = studentSignUpRequest + "<div class=\"signupRequest\">\
                </div>\
            </div>\
            <div class=\"requests\">\
                <h3>Masters Requests</h3>"
                        + masterRequest +
                        "</div>\
                    </div>\
                    </div>";
                    title = title + studentSignUpRequest;
                    $("article").html(
                        title
                    )
                    $('.denyReq').click(function () {
                        var userId = $(this).attr('value');
                        reject(userId);
                    });
                    $('.acceptReq').click(function () {
                        var userId = $(this).attr('value');
                        accept(userId);
                    });
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
        }


        function reject(id) {
            $.ajax({
                url: "/api/admin/user/reject-by-id/" + id,
                method: "PUT",
                contentType: "application/json",
                dataType: "json",
                success: function (response) {
                    viewSignUpRequest();
                    alert("user rejected successfully");
                }, error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
        }
    function accept(id) {
        $.ajax({
            url: "/api/admin/user/active-by-id/" + id,
            method: "PUT",
            contentType: "application/json",
            dataType: "json",
            success: function (response) {
                viewSignUpRequest();
                alert("user accepted successfully");
            }, error: function (erorMessage) {
                console.log(erorMessage);
            }
        })
    }

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
    }
)