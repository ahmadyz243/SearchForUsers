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

        //Add new Course-------------------------------------

        $("#addNewCourse").click(function () {
            getMasters();
        })

        function getMasters() {
            //var masters = new Object();
            $.ajax({
                url: "api/admin/teacher/find-all-actives",
                method: "GET",
                contentType: "application/json",
                dataType: "json",
                success: function (response) {
                    addCourse(response);
                }, error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
        }

        function addCourse(masters) {
            console.log(masters);
            var mastersCode = "";
            masters.forEach(master => {
                mastersCode = mastersCode + ("<option value=\"" + master.id + "\">" + master.name + " " + master.lastname + "</option>")
            });
            $("article").html("\
                <h2>Add a Course</h2>\
                <form action=\"\" method=\"\">\
                <input id=\"title\" type=\"text\" name=\"title\" placeholder=\"title\">\
                <input id=\"description\" type=\"text\" name=\"description\" placeholder=\"description\">\
                <span class=\"tip\">choose a master</span>\
                <select id=\"master\" name=\"master\">" +
                mastersCode +
                "</select>\
                <input id=\"startDate\" type=\"date\">\
                <input id=\"endDate\" type=\"date\">\
                <input id=\"addCourse\" type=\"submit\" value=\"add\">\
                </form>\
            ")
            $("#addCourse").click(function (event) {
                event.preventDefault();
                var title = $("#title").val();
                var description = $("#description").val();
                var masterId = $("#master").val();
                var startDate = $("#startDate").val();
                var endDate = $("#endDate").val();
                var course = {
                    title: title,
                    masterId: masterId,
                    description: description,
                    startDate: startDate,
                    endDate: endDate
                }
                $.ajax({
                    url: "api/admin/course/create",
                    method: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(course),
                    success: function (response) {
                        alert("course saved successfully");
                    },
                    error: function (erorMessage) {
                        console.log(erorMessage);
                    }
                })
            })
        }

        $("#searchByFields").click(function () {

            $("article").html("\
                <h2>search for users</h2>\
                <form action=\"\" method=\"post\">\
                <input id=\"firstname\" type=\"text\" name=\"firstname\" placeholder=\"firstname\">\
                <input id=\"lastname\" type=\"text\" name=\"lastname\" placeholder=\"lastname\">\
                <input id=\"nationalCode\" type=\"number\" name=\"nationalCode\" placeholder=\"nationalCode\">\
                <input class=\"radio\" type=\"radio\" name=\"role\" value=\"ROLE_STUDENT\"><span class=\"tip\">student</span>\
                <input class=\"radio\" type=\"radio\" name=\"role\" value=\"ROLE_TEACHER\"><span class=\"tip\">master</span>\
                <input id=\"search\" type=\"submit\" value=\"search\">\
                </form>\
            ")
            $("#search").click(function (event) {
                event.preventDefault();
                var firstname = $("#firstname").val();
                var lastname = $("#lastname").val();
                var nationalCode = $("#nationalCode").val();
                var role = $('input[name="role"]:checked').val();
                var userSearch = {
                    name: firstname,
                    lastname: lastname,
                    roles: [{name:role}],
                    nationalCode: nationalCode
                }
                console.log(userSearch);
                search(userSearch);

            })

            function search(userSearch) {

                $.ajax({
                    url: "/api/admin/search",
                    method: "POST",
                    contentType: "application/json",
                    dataType: "json",
                    data: JSON.stringify(userSearch),
                    success: function (response) {
                        showSearchResult(response);
                    },
                    error: function (erorMessage) {
                        console.log(erorMessage);
                    }
                })
            }
        })

        function showSearchResult(usersSearched) {
            var userSearchCode = "";
            usersSearched.forEach(user => {
                userSearchCode = userSearchCode.concat("<div class=\"user\">\
                    <span>" + user.name + "</span>\
                    <span>" + user.lastname + "</span>\
                    <span>" + user.nationalCode + "</span>\
                    <span>" + user.roles[0].name + "</span>\
                    </div>");
            });
            $("article").html("\
                    <div id=\"foundedUsers\">\
                    <div class=\"user\">\
                    <span><b>Fistname</b></span>\
                    <span><b>Lastname</b></span>\
                    <span><b>National Code</b></span>\
                    <span><b>Position</b></span>\
                    </div>" +
                userSearchCode +
                "</div>\
            ")
        }

        $("#viewCourses").click(function () {
            var courses = getCourses();
            var coursesCode = "";
            var masterCode = "";
            var studentsCode = "";
            var otherStudentsCode = "";
            courses.forEach(course => {
                coursesCode = coursesCode.concat("<button value=\"" + course.id + "\" class=\"course\">\
                <span>" + course.title + "</span><span>" + course.beginDate + "</span><span>" + course.finishDate + "</span>\
                </button>")
            });
            viewCourses(coursesCode, "", "", "");
            $(".courseSpec").hide();
            $(".course").click(function () {
                var courseId = $(this).attr('value');
                var course = getCourseById(courseId);
                masterCode = "<div class=\"courseDetail\">\
                <div class=\"name\">" + course.master.name + " " + course.master.lastname + "</div>\
                <button id=\"removeMasterFromCourse\" value=\"" + course.id + "\">change master</button>\
                </div>";
                var students = course.students;
                students.forEach(student => {
                    studentsCode = studentsCode.concat("<div class=\"courseDetail\">\
                    <div class=\"name\">" + student.name + " " + student.lastname + "</div>\
                    <button id=\"removeStudentFromCourse\" value=\"" + student.id + "\">remove from course</button>\
                    </div>")
                });
                var otherStudents = getStudentsNotInCourse(courseId);
                otherStudents.forEach(s => {
                    otherStudentsCode = otherStudentsCode.concat("<div class=\"courseDetail\">\
                    <div class=\"details\">" + s.name + " " + s.lastname + "</div>\
                    </div>")
                });
                viewCourses(coursesCode, masterCode, studentsCode, otherStudentsCode);
                $(".courseSpec").show();
            })

            function getCourses() {
                var courses = [];
                $.ajax({
                    url: "",
                    method: "GET",
                    contentType: "application/json",
                    dataType: "json",
                    success: function (response) {
                        courses = response;
                        return courses;
                    }, error: function (erorMessage) {
                        console.log(erorMessage);
                    }
                })
            }

            function getCourseById(id) {
                var course;
                $.ajax({
                    url: "",
                    method: "GET",
                    contentType: "application/json",
                    dataType: "json",
                    success: function (response) {
                        course = response;
                        return course;
                    }, error: function (erorMessage) {
                        console.log(erorMessage);
                    }
                })
            }

            function getStudentsNotInCourse(courseId) {
                var students = [];
                $.ajax({
                    url: "",
                    method: "GET",
                    contentType: "application/json",
                    dataType: "json",
                    success: function (response) {
                        students = response;
                        return students;
                    }, error: function (erorMessage) {
                        console.log(erorMessage);
                    }
                })
            }

            function viewCourses(coursesCode, masterCode, studentsCode, otherStudentsCode) {
                $("article").html("\
                <div class=\"coursesContainer\">\
                <div class=\"courses\">\
                <h3>Courses</h3>\
                <div class=\"titles\">\
                <span>title</span><span>begin date</span><span>finish date</span>\
                </div>" +
                    coursesCode +
                    "</div>\
                    <div class=\"courseDetails courseSpec\">\
                    <h3>Master</h3>" +
                    masterCode +
                    "<h3>Students</h3>" +
                    studentsCode +
                    "</div>\
                    <div class=\"courseDetails courseSpec\">\
                    <button id=\"addNewStudentToCourse\"><b>add another student</b></button>" +
                    otherStudentsCode +
                    "</div>\
                    </div>\
                ")
            }

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