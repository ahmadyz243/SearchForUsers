$(document).ready(function () {
        var allExamQuestionId = [];
        var studentRequests = [];
        var masterRequests = [];
        var studentSignUpRequest = "";
        var masterRequest = "";
        var coursesCode = "";
        $("#donthaveaccount").click(function () {
            window.location.href = "/signup";
        })
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

        //Student ---------------------------------------------------------------------------
        $(".dropdown").click(function () {
            var studentCoursesCode = "";
            var studentCourses = getStudentCourses();
            for (var i = 0; i < studentCourses.length; i++) {
                studentCoursesCode = studentCoursesCode.concat("<button class=\"studentCourse\" value=\"" + studentCourses[i].id + "\">" + studentCourses[i].title + ": " + studentCourses[i].description + "</button>\n");
            }
            $(this).html("<button class=\"dropbtn\">My Courses</button>\n" +
                "        <div class=\"dropdown-content\">\n" +
                studentCoursesCode +
                "        </div>");
            $(".studentCourse").click(function () {
                var courseId = $(this).val();
                var studentCourse = {};
                for (let i = 0; i < studentCourses.length; i++) {
                    if (studentCourses[i].id == courseId) {
                        studentCourse = studentCourses[i];
                        break;
                    }
                }
                viewStudentCourse(studentCourse);
            })
        })

        function checkForEnd(examId) {
            var check = true;
            $.ajax({
                url: "/api/student/course/exam/check-for-end/" + examId,
                method: "GET",
                async: false,
                contentType: "application/json",
                success: function (response) {
                    check = response;
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
            return check;
        }

        function viewStudentCourse(course) {
            var viewStudentCourseCode = " <table>\n" +
                "        <caption><b>My Exams</b></caption>\n" +
                "        <tr>\n" +
                "          <th>#</th>\n" +
                "          <th>title</th>\n" +
                "          <th>description</th>\n" +
                "          <th>start date</th>\n" +
                "          <th>exam time(min)</th>\n" +
                "          <th>status</th>\n" +
                "          <th>grade</th>\n" +
                "        </tr>\n";
            for (let i = 0; i < course.examList.length; i++) {
                var status = "";
                var check = checkForEnd(course.examList[i].id);
                if (check) {
                    status = "<td> you joined! </td>";
                } else if (course.examList[i].enabled === true) {
                    status = "<td><button value=\"" + course.examList[i].id + "\" class=\"startExam\">start exam</button></td>\n"
                } else {
                    status = "<td> not available </td>";
                }
                viewStudentCourseCode = viewStudentCourseCode +
                    "        <tr>\n" +
                    "          <td>" + (i + 1) + "</td>\n" +
                    "          <td>" + course.examList[i].title + "</td>\n" +
                    "          <td>" + course.examList[i].description + "</td>\n" +
                    "          <td>" + course.examList[i].startDateAndTime + "</td>\n" +
                    "          <td>" + course.examList[i].time + "</td>\n" +
                    status +
                    "          <td>"+course.examList[i].score+"</td>\n" +
                    "        </tr>\n";
            }
            $("article").html(viewStudentCourseCode + "</table>");
            $(".startExam").click(function () {
                var examId = $(this).val();
                var exam = {};
                var questionList = findQuestionsByExamId(examId);
                for (let i = 0; i < course.examList.length; i++) {
                    if (examId == course.examList[i].id) {
                        exam = course.examList[i];
                        break;
                    }
                }
                startExam(questionList, exam);
            })
        }

        function getStudentAnswers(examId) {
            var answers = [];
            $.ajax({
                url: "/api/student/course/exam/get-student-answers/" + examId,
                method: "GET",
                contentType: "application/json",
                async: false,
                dataType: "json",
                success: function (response) {
                    answers = response;
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
            return answers;
        }

        function findQuestionsByExamId(examId) {
            var questions = [];
            $.ajax({
                url: "/api/student/course/exam/get-questions/" + examId,
                method: "GET",
                contentType: "application/json",
                async: false,
                dataType: "json",
                success: function (response) {
                    questions = response;
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
            return questions;
        }

        function startExam(questionList, exam) {
            var allAnswers = getStudentAnswers(exam.id);
            var allQuestions = createAllQuestions(questionList, allAnswers);
            viewExamQuestion(allQuestions, exam, 0, allAnswers, questionList);
        }


        function createAllQuestions(questionList, allAnswers) {
            allExamQuestionId = [];
            allQuestions = [];
            var answer = "";
            var question = "";
            var items = "";
            for (let i = 0; i < questionList.length; i++) {
                allExamQuestionId.push(questionList[i].examQuestionId);
                if (questionList[i].question.questionItemList !== null) {
                    allExamQuestionId.push(questionList[i].examQuestionId);
                    items = "<div class=\"multipleQuestionOptions\">\n";
                    answer = "";
                    for (let j = 0; j < allAnswers.length; j++) {
                        if (allAnswers[j].examQuestionId === questionList[i].question.id) {
                            answer = allAnswers[j].answer;
                        }
                    }
                    for (let j = 0; j < questionList[i].question.questionItemList.length; j++) {
                        var count = j + 97;
                        if (answer === questionList[i].question.questionItemList[j].answer) {
                            items = items.concat("<p><input  name='item' checked='true' type=\"radio\" value=\"" + questionList[i].examQuestionId + "-" + questionList[i].question.questionItemList[j].answer + "\">" + String.fromCharCode(count) + ") " + questionList[i].question.questionItemList[j].answer + "</p>\n")
                        } else {
                            items = items.concat("<p><input name='item' type=\"radio\" value=\"" + questionList[i].examQuestionId + "-" + questionList[i].question.questionItemList[j].answer + "\">" + String.fromCharCode(count) + ") " + questionList[i].question.questionItemList[j].answer + "</p>\n")
                        }

                    }
                    items = items.concat("</div>\n")
                    question = " <p><b>" + (i + 1) + ". " + questionList[i].question.question + "(score: " + questionList[i].score + ")</b></p>\n" + items;
                } else {
                    answer = "";
                    for (let j = 0; j < allAnswers.length; j++) {
                        if (allAnswers[j].examQuestionId === questionList[i].question.id) {
                            answer = allAnswers[j].answer;
                        }
                    }

                    question = " <p><b>" + (i + 1) + ". " + questionList[i].question.question + "(score: " + questionList[i].score + ")</b> </p>\n" +
                        "<textarea name=''  class=\"detailQuestionAnswers\" placeholder=\"write your answer here\">" + answer + "</textarea>\n" +
                        "<button class='hidenHelper' value='" + questionList[i].examQuestionId + "' ></button>";
                }
                allQuestions.push(question);
                question = "";
            }

            return allQuestions;
        }

        function viewExamQuestion(questions, exam, count, allAnswers, questionList) {
            var examQuestionId = null;
            $("article").html(" <div id=\"examination\">\n" +
                "            <h2 id=\"timeRemaining\">time remaining</h2>\n" +
                questions[count] +
                "<div id=\"buttons\">\n" +
                "<button class=\"previous paginationBtn\" value=\"\">previous</button>\n" +
                "<button class=\"finish paginationBtn\" value=\"\">Finish exam</button>\n" +
                "<button class=\"next paginationBtn\" value=\"\">next</button>\n" +
                "</div>\n" +
                "</div>"
            )
            $(".hidenHelper").hide();
            var finishDate = new Date(exam.endDate).getTime();
            // var min = exam.time;
            // var second = 0;
            var x = setInterval(function () {
                var now = new Date().getTime();
                var distance = finishDate - now;
                var days = Math.floor(distance / (1000 * 60 * 60 * 24));
                var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                var seconds = Math.floor((distance % (1000 * 60)) / 1000);
                $("#timeRemaining").html(hours + ":" + minutes + ":" + seconds);
                if (distance < 0) {
                    clearInterval(x);

                    var questionAnswer = "";
                    var questionAnswers = [];
                    var examQuestionId = "";
                    examQuestionId = $(".hidenHelper").val();
                    questionAnswer = $(".detailQuestionAnswers").val();
                    if (examQuestionId !== undefined) {
                        console.log("im detailed question");
                        studentAnswerReq.exam_id = exam.id;
                        studentAnswerReq.examQuestionId = parseInt(examQuestionId);
                        studentAnswerReq.answer = questionAnswer;
                    } else if (questionAnswer === "" || questionAnswer === null || questionAnswer === undefined) {
                        questionAnswer = $('input[name="item"]:checked').val();
                        // if (questionAnswer === "" || questionAnswer === undefined || questionAnswer === null) {
                        //     alert("answer the question first!")
                        // }
                        if (questionAnswer !== undefined) {
                            questionAnswers = questionAnswer.split("-");
                            studentAnswerReq.answer = questionAnswers[1];
                            studentAnswerReq.examQuestionId = parseInt(questionAnswers[0]);
                        } else {

                            studentAnswerReq.examQuestionId = allExamQuestionId[count];
                            studentAnswerReq.answer = "  ";
                        }
                        studentAnswerReq.exam_id = exam.id;
                    }
                    var flag = false;
                    for (let i = 0; i < allAnswers.length; i++) {
                        if (allAnswers[i].examQuestionId === studentAnswerReq.examQuestionId) {
                            console.log("im updating");
                            allAnswers[i].answer = studentAnswerReq.answer;
                            flag = true;
                        }
                    }
                    if (flag === false) {
                        var newAnswer = {
                            answer: studentAnswerReq.answer,
                            examQuestionId: studentAnswerReq.examQuestionId
                        }
                        allAnswers.push(newAnswer);
                        flag = false;
                    }
                    console.log(allAnswers);
                    setAnswerForStudent(studentAnswerReq)
                    $.ajax({
                        url: "/api/student/course/exam/finish/" + exam.id,
                        method: "POST",
                        data: JSON.stringify(allAnswers),
                        contentType: "application/json",
                        success: function (response) {
                            alert("good luck!")
                        },
                        error: function (erorMessage) {
                            console.log(erorMessage);
                        }
                    })
                    setArticleNull();
                }
            }, 1000);
            //     if (second <= 0) {
            //         if (min > 0) {
            //             min--;
            //             exam.time = exam.time - 1;
            //             second += 60;
            //         } else {
            //             clearInterval(x);
            //         }
            //     } else {
            //         second--;

            //     }

            var studentAnswerReq = {
                answer: "",
                examQuestionId: null,
                exam_id: null
            }
            $(".next").click(function () {
                var questionAnswer = "";
                var questionAnswers = [];
                var examQuestionId = "";
                examQuestionId = $(".hidenHelper").val();
                console.log(examQuestionId);
                if (count < questions.length - 1) {
                    count++;
                    questionAnswer = $(".detailQuestionAnswers").val();
                    if (examQuestionId !== undefined) {
                        console.log("im detailed question");
                        studentAnswerReq.exam_id = exam.id;
                        studentAnswerReq.examQuestionId = parseInt(examQuestionId);
                        studentAnswerReq.answer = questionAnswer;
                    } else if (questionAnswer === "" || questionAnswer === null || questionAnswer === undefined) {
                        questionAnswer = $('input[name="item"]:checked').val();
                        // if (questionAnswer === "" || questionAnswer === undefined || questionAnswer === null) {
                        //     alert("answer the question first!")
                        // }
                        if (questionAnswer !== undefined) {
                            questionAnswers = questionAnswer.split("-");
                            studentAnswerReq.answer = questionAnswers[1];
                            studentAnswerReq.examQuestionId = parseInt(questionAnswers[0]);
                        } else {
                            studentAnswerReq.examQuestionId = allExamQuestionId[count];
                            studentAnswerReq.answer = "  ";
                        }
                        studentAnswerReq.exam_id = exam.id;
                    }

                    var flag = false;
                    for (let i = 0; i < allAnswers.length; i++) {
                        console.log("im updating...");
                        if (allAnswers[i].examQuestionId === parseInt(studentAnswerReq.examQuestionId)) {
                            console.log("updated...");
                            allAnswers[i].answer = studentAnswerReq.answer;
                            flag = true;
                        }
                    }
                    if (flag === false) {
                        console.log("im adding...");
                        var newAnswer = {
                            answer: studentAnswerReq.answer,
                            examQuestionId: studentAnswerReq.examQuestionId
                        }
                        allAnswers.push(newAnswer);
                        flag = false;
                    }
                    console.log(allAnswers);
                    setAnswerForStudent(studentAnswerReq)
                    viewExamQuestion(createAllQuestions(questionList, allAnswers), exam, count, allAnswers, questionList);
                }
            })
            $(".previous").click(function () {
                var questionAnswer = "";
                var questionAnswers = [];
                var examQuestionId = "";
                examQuestionId = $(".hidenHelper").val();
                console.log(examQuestionId);
                if (count !== 0) {
                    count--;
                    questionAnswer = $(".detailQuestionAnswers").val();
                    if (examQuestionId !== undefined) {
                        console.log("im detailed question");
                        studentAnswerReq.exam_id = exam.id;
                        studentAnswerReq.examQuestionId = parseInt(examQuestionId);
                        studentAnswerReq.answer = questionAnswer;
                    } else if (questionAnswer === "" || questionAnswer === null || questionAnswer === undefined) {
                        questionAnswer = $('input[name="item"]:checked').val();
                        // if (questionAnswer === "" || questionAnswer === undefined || questionAnswer === null) {
                        //     alert("answer the question first!")
                        // }
                        console.log("im multy");
                        if (questionAnswer !== undefined) {
                            questionAnswers = questionAnswer.split("-");
                            studentAnswerReq.answer = questionAnswers[1];
                            studentAnswerReq.examQuestionId = parseInt(questionAnswers[0]);
                        } else {
                            studentAnswerReq.examQuestionId = allExamQuestionId[count];
                            studentAnswerReq.answer = "  ";
                        }
                        studentAnswerReq.exam_id = exam.id;
                    }

                    var flag = false;
                    for (let i = 0; i < allAnswers.length; i++) {
                        if (allAnswers[i].examQuestionId === studentAnswerReq.examQuestionId) {
                            console.log("im updating");
                            allAnswers[i].answer = studentAnswerReq.answer;
                            flag = true;
                        }
                    }
                    if (flag === false) {
                        console.log("im adding");
                        var newAnswer = {
                            answer: studentAnswerReq.answer,
                            examQuestionId: studentAnswerReq.examQuestionId
                        }
                        allAnswers.push(newAnswer);
                        flag = false;
                    }
                    console.log(allAnswers);

                    setAnswerForStudent(studentAnswerReq)
                    viewExamQuestion(createAllQuestions(questionList, allAnswers), exam, count, allAnswers, questionList);
                }
            })
            $(".finish").click(function () {
                var questionAnswer = "";
                var questionAnswers = [];
                var examQuestionId = "";
                examQuestionId = $(".hidenHelper").val();
                questionAnswer = $(".detailQuestionAnswers").val();
                if (examQuestionId !== undefined) {
                    console.log("im detailed question");
                    studentAnswerReq.exam_id = exam.id;
                    studentAnswerReq.examQuestionId = parseInt(examQuestionId);
                    studentAnswerReq.answer = questionAnswer;
                } else if (questionAnswer === "" || questionAnswer === null || questionAnswer === undefined) {
                    questionAnswer = $('input[name="item"]:checked').val();
                    // if (questionAnswer === "" || questionAnswer === undefined || questionAnswer === null) {
                    //     alert("answer the question first!")
                    // }
                    if (questionAnswer !== undefined) {
                        questionAnswers = questionAnswer.split("-");
                        studentAnswerReq.answer = questionAnswers[1];
                        studentAnswerReq.examQuestionId = parseInt(questionAnswers[0]);
                    } else {
                        studentAnswerReq.examQuestionId = allExamQuestionId[count];
                        studentAnswerReq.answer = "  ";
                    }
                    studentAnswerReq.exam_id = exam.id;
                }
                var flag = false;
                for (let i = 0; i < allAnswers.length; i++) {
                    if (allAnswers[i].examQuestionId === studentAnswerReq.examQuestionId) {
                        console.log("im updating");
                        allAnswers[i].answer = studentAnswerReq.answer;
                        flag = true;
                    }
                }
                if (flag === false) {
                    var newAnswer = {
                        answer: studentAnswerReq.answer,
                        examQuestionId: studentAnswerReq.examQuestionId
                    }
                    allAnswers.push(newAnswer);
                    flag = false;
                }
                console.log(allAnswers);
                setAnswerForStudent(studentAnswerReq)
                $.ajax({
                    url: "/api/student/course/exam/finish/" + exam.id,
                    method: "POST",
                    data: JSON.stringify(allAnswers),
                    contentType: "application/json",
                    success: function (response) {
                        alert("good luck!")
                    },
                    error: function (erorMessage) {
                        console.log(erorMessage);
                    }
                })
                setArticleNull();
            })
        }

        function setArticleNull() {
            $("article").html("");
        }

        function setAnswerForStudent(studentAnswerReq) {
            $.ajax({
                url: "/api/student/course/exam/set-answer",
                method: "POST",
                data: JSON.stringify(studentAnswerReq),
                contentType: "application/json",
                success: function (response) {
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
        }

        function getStudentCourses() {
            var studentCourses = [];
            $.ajax({
                url: "/api/student/course/all",
                method: "GET",
                contentType: "application/json",
                async: false,
                dataType: "json",
                success: function (response) {
                    studentCourses = response;
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
            return studentCourses;
        }


// MASTER ---------------------------------------------------------------------------
//GET TEACHER COURSES LIST
        $("#viewTeacherCourses").click(function () {
            var teacherCourses = getTeacherCourses();
            console.log(teacherCourses);
            viewTeacherCourses(teacherCourses);
            $(".masterCourse").hover(function () {
                $(this).css("background", "rgb(2, 45, 2)");
            }, function () {
                $(this).css("background", "rgb(0, 128, 0)");
            });
            $(".masterCourse").click(function () {
                var courseId = $(this).attr('value');
                var teacherCourse = getTeacherCourse(courseId, teacherCourses);
                viewTeacherCourse(teacherCourse);
                $("#addTestButton").click(function () {
                    addNewExam(courseId);
                })
                $(".test").click(function () {
                    var examId = $(this).attr('value');
                    var exam = getExamById(examId);
                    viewTeacherExam(exam, courseId);
                    $("#addNewMultipleQuestion").click(function () {
                        addNewMultipleOptionQuestion(exam, courseId);
                    })
                    $("#addNewDetailedQuestion").click(function () {
                        addNewDetailQuestion(exam, courseId);
                    })
                })
            })
        })

        function viewAddNewDetailQuestionPage(exam, courseId) {
            $("article").html("<h2>add new detail question</h2>\n" +
                "    <form id=\"newQuestionForm\">\n" +
                "        <div><b>question text:</b><textarea id=\"questionText\"></textarea></div>\n" +
                "        <div><b>question title:</b><input id=\"questionTitle\" type='text' placeholder='title'></input></div>\n" +
                "        <div>\n" +
                "            <b>enter a default grade for question:  </b><input required id=\"questionDefaultGrade\" type=\"number\" step=\"0.01\" placeholder=\"grade\">\n" +
                "        </div>\n" +
                "        <input id=\"saveDetailQuestion\" type=\"submit\" value=\"save question\">\n" +
                "    </form>");
            $("#saveDetailQuestion").click(function (event) {
                event.preventDefault();
                var detailQuestion = {
                    courseId: courseId,
                    title: $("#questionTitle").val(),
                    examId: exam.id,
                    question: $("#questionText").val(),
                    score: $("#questionDefaultGrade").val()
                }
                saveDetailQuestion(detailQuestion);
                viewTeacherExam(exam, courseId);
            })
        }

        function addNewMultipleOptionQuestion(exam, courseId) {
            var multipleOptionQuestion = {
                examId: exam.id,
                question: "",
                title: "",
                courseId: courseId,
                score: 0,
                questionItemList: []

            }
            viewAddNewMultipleOptionQuestionpage(multipleOptionQuestion, exam);
        }

        function saveMultipleOptionQuestion(multipleOptionQuestion) {
            $.ajax({
                url: "/api/teacher/course/exam/add-multiple",
                method: "POST",
                data: JSON.stringify(multipleOptionQuestion),
                contentType: "application/json",
                success: function (response) {
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
        }

        function viewAddNewMultipleOptionQuestionpage(multipleOptionQuestion, exam) {
            var itemsCode = "";
            for (var i = 0; i < multipleOptionQuestion.questionItemList.length; i++) {
                var count = i + 97;
                itemsCode = itemsCode.concat("<p><input class='newQuestionItem' type=\"radio\" name=\"newQuestionItem\" value=\"" + i + "\" required> " + String.fromCharCode(count) + ") " + multipleOptionQuestion.questionItemList[i].answer + "</p>");
            }
            $("article").html("<h2>add new multiple option question</h2>\n" +
                "    <form id=\"newQuestionForm\">\n" +
                "        <div><b>question text:</b><textarea id=\"questionText\"></textarea></div>\n" +
                "        <div><b>question title:</b><input id=\"questionTitle\" type='text' placeholder='title'></input></div>\n" +
                "        <div>\n" +
                "            <b>enter a default garde for question:  </b><input id=\"questionDefaultGrade\"  type=\"number\" step=\"0.01\" placeholder=\"grade\">\n" +
                "        </div>\n" +
                "        <div>\n" +
                itemsCode +
                "            <p class=\"greenText largerText\">select the right answer</p>\n" +
                "        </div>\n" +
                "        <div>\n" +
                "            <input id=\"addNewItem\" type=\"text\" placeholder=\"enter new item's text here\">\n" +
                "            <button type=\"button\" id=\"newItemButton\">add an item</button>\n" +
                "        </div>\n" +
                "        <input id=\"saveMultipleQuestion\" type=\"submit\" value=\"save question\">\n" +
                "    </form>");
            $("#questionTitle").val(multipleOptionQuestion.title);
            $("#questionText").html(multipleOptionQuestion.question);
            $("#questionDefaultGrade").val(multipleOptionQuestion.score);
            $("#newItemButton").click(function () {
                var questionItem = {
                    answer: $("#addNewItem").val(),
                    isRightAnswer: false
                }
                multipleOptionQuestion.title = $("#questionTitle").val();
                multipleOptionQuestion.question = $("#questionText").val();
                multipleOptionQuestion.score = $("#questionDefaultGrade").val();
                multipleOptionQuestion.questionItemList.push(questionItem);
                viewAddNewMultipleOptionQuestionpage(multipleOptionQuestion, exam);
            })
            $("#saveMultipleQuestion").click(function (event) {
                var trueAnswer = $('input[class="newQuestionItem"]:checked').val();
                event.preventDefault();
                multipleOptionQuestion.title = $("#questionTitle").val();
                multipleOptionQuestion.question = $("#questionText").val();
                multipleOptionQuestion.score = $("#questionDefaultGrade").val();
                multipleOptionQuestion.questionItemList[trueAnswer].isRightAnswer = true;
                saveMultipleOptionQuestion(multipleOptionQuestion);
                exam.examQuestionList.push(multipleOptionQuestion);
                viewTeacherExam(exam);
            })
        }

        function addNewDetailQuestion(exam, courseId) {
            viewAddNewDetailQuestionPage(exam, courseId);

        }

        function saveDetailQuestion(detailQuestion) {
            $.ajax({
                url: "/api/teacher/course/exam/add-detailed-exam",
                method: "POST",
                data: JSON.stringify(detailQuestion),
                contentType: "application/json",
                success: function (response) {
                    alert("saved successful...");
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
        }

        function getExamById(examId) {
            var exam;
            $.ajax({
                url: "/api/teacher/course/exam/get/" + examId,
                method: "GET",
                async: false,
                contentType: "application/json",
                success: function (response) {
                    exam = response;
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
            return exam;
        }

        function viewTeacherExam(exam, courseId) {
            var viewTeacherExamCode = " <div id=\"examSection\">\n" +
                "        <form id=\"examForm\">\n" +
                "            <input id=\"examTitle\" class=\"examDetails\" type=\"text\" value=\"" + exam.title + "\" disabled>\n" +
                "            <input disabled id=\"examStartDate\" class=\"examDetails\" type=\"text\" value=\"" + exam.startDateAndTime + "\" onclick=\"(this.type='datetime-local')\">\n" +
                "            <input id=\"examTime\" class=\"examDetails\" disabled value=\"" + exam.time + "\">\n" +
                "            <textarea id=\"examDescription\" class=\"examDetails\" disabled >" + exam.description + "</textarea>\n" +
                "            <button id=\"updateExam\" value=\"" + exam.id + "\">update</button>\n" +
                "        </form>" +
                "  <button id=\"editExam\">edit</button>\n" +
                "        <button id=\"deleteExam\" value=\"" + exam.id + "\">delete exam</button>\n" +
                "        <button id=\"viewStudentsResponses\" value=\"" + exam.id + "\">view answers</button>\n" +
                "        <button id=\"addFromBank\" value=\"" + exam.id + "\">add question from question bank</button>\n" +
                "        <div id=\"multipleQuestions\" class=\"questions\">\n" +
                "            <h2>multiple option questions</h2>";
            var counterM = 1;
            var counterD = 0;
            for (let i = 0; i < exam.examQuestionList.length; i++) {
                if (exam.examQuestionList[i].question.questionItemList != null) {
                    counterD++;
                }
            }
            var DetailedQuestions = [];
            var itemsCode = "";
            for (let i = 0; i < exam.examQuestionList.length; i++) {
                if (exam.examQuestionList[i].question.questionItemList != null) {
                    itemsCode = "";
                    for (let j = 0; j < exam.examQuestionList[i].question.questionItemList.length; j++) {
                        var count = j + 97;
                        itemsCode = itemsCode.concat(
                            " <div className=\"questionItem\">\n" +
                            "<p>" + String.fromCharCode(count) + ") " + exam.examQuestionList[i].question.questionItemList[j].answer + " </p>\n" +
                            "</div>\n");
                    }
                    viewTeacherExamCode = viewTeacherExamCode.concat("<div className=\"multipleQuestion question\">" +
                        "                        <div className=\"questionText\">\n" +
                        "                            <p><b>" + counterM + " : " + exam.examQuestionList[i].question.question +
                        "                     (score: " + exam.examQuestionList[i].score + ")</b></p></div>\n" +
                        itemsCode +
                        "</div>" +
                        "                <button id='editQuestion' value='" + exam.examQuestionList[i].question.id + "' class=\"editQuestion\">edit question</button>\n" +
                        "                <hr>");
                    counterM++;
                } else {
                    DetailedQuestions.push(exam.examQuestionList[i]);
                }
            }
            viewTeacherExamCode = viewTeacherExamCode.concat("<button id=\"addNewMultipleQuestion\">add a new multiple option question</button></div>" +
                "            <div id=\"detaledQuestions\" class=\"questions\">\n" +
                "                <h2>detailed questions</h2>");
            for (let x = 0; x < DetailedQuestions.length; x++) {
                viewTeacherExamCode = viewTeacherExamCode.concat("  <div class=\"detailedQuestion question\">\n" +
                    "                    <div class=\"questionText\">\n" +
                    "                        <p><b>" + counterD + " : " + DetailedQuestions[x].question.question + "(score:" + DetailedQuestions[x].score + ")</b></p>\n" +
                    "                    </div>\n" +
                    "                </div>\n" +
                    "                <button value='" + DetailedQuestions[x].id + "' class=\"editQuestion\">edit question</button>\n" +
                    "                <hr>")
                counterD++;
            }
            viewTeacherExamCode = viewTeacherExamCode.concat("<button id=\"addNewDetailedQuestion\">add a new detailed question</button>\n" +
                "\n" +
                "   </div>\n" +
                "    </div>")
            $("article").html(viewTeacherExamCode);
            $("#updateExam").hide();
            $("#editExam").click(function () {
                $("#updateExam").show();
                $("#examTitle").removeAttr("disabled").css("background-color", "white");
                $("#examStartDate").removeAttr("disabled").css("background-color", "white");
                $("#examTime").removeAttr("disabled").css("background-color", "white");
                $("#examDescription").removeAttr("disabled").css("background-color", "white");
                $("#updateExam").click(function (event) {
                    event.preventDefault();
                    var examTitle = $("#examTitle").val();
                    var examStartDate = $("#examStartDate").val();
                    var examTime = $("#examTime").val();
                    var examDescription = $("#examDescription").val();
                    if (examTitle !== null && examTitle !== "" && examStartDate !== null && examStartDate !== "" && examTime !== null && examTime !== "" && examDescription !== null && examDescription !== "") {
                        exam.time = examTime;
                        exam.title = examTitle;
                        exam.description = examDescription;
                        exam.startDateAndTime = examStartDate;
                        updateExam(exam)
                    } else {
                        alert("fields can not be null!")
                    }
                    viewTeacherExam(exam, courseId);
                });
            });

            $("#deleteExam").click(function () {
                var examId = $("#deleteExam").val();
                deleteExam(examId);
                $("article").html("");
            })
            $("#viewStudentsResponses").click(function () {
                var examStudents = getExamStudents(exam.id);
                var students = "<div id=\"teacherViewStudentsAnswers\">\n";
                students = students.concat("<button id='setAutoGrade'> click here to set grade for multiple questions</button>")
                for (let i = 0; i < examStudents.length; i++) {
                    students = students.concat("<button value='" + examStudents[i].student.id + "' class=\"studentsAnswers\">\n" +
                        "            " + examStudents[i].student.name + " " + examStudents[i].student.lastname +
                        "        </button>\n")
                }
                students = students.concat("</div>");
                $("article").html(
                    students
                );
                $(".studentsAnswers").click(function () {
                    var allExamQuestions = getExamById(exam.id);
                    var multipleQuestions = [];
                    var detailedQuestions = [];
                    var studentId = $(this).val();
                    var questionAndAnswer = "";
                    var studentFullAnswers = getStudentAnswersByStudentId(studentId, examStudents);
                    console.log(studentFullAnswers);
                    questionsCode = "<div id=\"teacherViewStudentAnswers\">" +
                        "        <h2>" + studentFullAnswers.student.name + " " + studentFullAnswers.student.lastname +
                        "</h2>\n";
                    for (let i = 0; i < allExamQuestions.examQuestionList.length; i++) {
                        if (allExamQuestions.examQuestionList[i].question.questionItemList !== null) {
                            multipleQuestions.push(allExamQuestions.examQuestionList[i]);
                        } else {
                            detailedQuestions.push(allExamQuestions.examQuestionList[i]);
                        }
                    }
                    var counter = 1;
                    var flag = false;
                    for (let i = 0; i < multipleQuestions.length; i++) {
                        for (let j = 0; j < studentFullAnswers.studentAnswers.answers.length; j++) {
                            if (multipleQuestions[i].question.question === studentFullAnswers.studentAnswers.answers[j].examQuestion.question.question) {
                                console.log("now they fucking me")
                                var answer = studentFullAnswers.studentAnswers.answers[j].answer;
                                var score = 0;
                                for (let k = 0; k < multipleQuestions[i].question.questionItemList.length; k++) {
                                    if (answer === multipleQuestions[i].question.questionItemList[k].answer && multipleQuestions[i].question.questionItemList[k].isRightAnswer === true)
                                        score = multipleQuestions[i].score;
                                }
                                questionAndAnswer = questionAndAnswer.concat("<div class=\"teacherViewMultipleAnswers\">\n" +
                                    "            <p><b>Question " + counter + ":</b> " + multipleQuestions[i].question.question + "(score: " + multipleQuestions[i].score + ")</p>\n" +
                                    "            <p><b>Student Answer:</b>" + answer +
                                    "</p>\n" +
                                    "            <p><b>earned score:</b>" + score + "</p>\n" +
                                    "            <hr>\n" +
                                    "        </div>\n"
                                );
                                counter++
                                flag = true;
                            }
                        }
                        if (flag === false) {
                            questionAndAnswer = questionAndAnswer.concat("<div class=\"teacherViewMultipleAnswers\">\n" +
                                "            <p><b>Question " + counter + ":</b> " + multipleQuestions[i].question.question + "(score: " + multipleQuestions[i].score + ")</p>\n" +
                                "            <p><b>Student Answer:</b>" + "not answerd!" +
                                "</p>\n" +
                                "            <p><b>earned score:</b>" + "0" + "</p>\n" +
                                "            <hr>\n" +
                                "        </div>\n"
                            );
                            counter++;
                        }

                        flag = false;
                    }
                    var detailedFlag = false;
                    for (let i = 0; i < detailedQuestions.length; i++) {

                        for (let j = 0; j < studentFullAnswers.studentAnswers.answers.length; j++) {
                            var Danswer = studentFullAnswers.studentAnswers.answers[j].answer;
                            if (detailedQuestions[i].question.question === studentFullAnswers.studentAnswers.answers[j].examQuestion.question.question) {
                                questionAndAnswer = questionAndAnswer.concat("        <div class=\"teacherViewDetailAnswers\">\n" +
                                    "            <p><b>Question " + counter + ":</b> " + detailedQuestions[i].question.question + "(score:" + DetailedQuestions[i].score +
                                    ")</p>\n" +
                                    "    <p><b>Student Answer:</b> " + Danswer + " </p>\n" +
                                    "    <b>earned score:</b> <form><input value='" + studentFullAnswers.studentAnswers.answers[j].earnedScore + "' id='detailedGrade' type=\"number\"> <button type=\"submit\" id='addGrade' value=" + studentFullAnswers.studentAnswers.answers[j].questionAnswerId + ">save</button></form>\n" +
                                    "    <hr>\n" +
                                    "</div>\n");
                                detailedFlag = true;
                            }
                        }
                        if (detailedFlag === false) {
                            questionAndAnswer = questionAndAnswer.concat("<div class=\"teacherViewDetailAnswers\">\n" +
                                "            <p><b>Question " + counter + ":</b> " + detailedQuestions[i].question.question + "(score:" + DetailedQuestions[i].score +
                                ")</p>\n" +
                                "            <p><b>Student Answer:</b> " + "not answerd!" + " </p>\n" +
                                "            <b>earned score:</b> <form><input id='detailedGrade' value='0' type=\"number\"> </form>\n" +
                                "            <hr>\n" +
                                "        </div>\n");
                        }
                        detailedFlag = false;
                        counter++;
                    }
                    questionsCode = questionsCode.concat(questionAndAnswer + "</div>")
                    $("article").html(
                        questionsCode
                    );
                    $("#addGrade").click(function (event) {
                            event.preventDefault();
                            var grade = $("#detailedGrade").val();
                            var questionAnswerId = $(this).val();
                            console.log(grade);
                            console.log(questionAnswerId);
                            setGrade(questionAnswerId, grade);
                        }
                    )
                })
                $("#setAutoGrade").click(function () {
                    setAutoGrade(exam.id, courseId);
                })
                $("#editQuestion").click(function () {
                    var questionId = $(this).val();
                    editQuestion(questionId, exam);

                })
                $("#addFromBank").click(function () {
                    addQuestionFromBank(exam, courseId);
                })

            })
        }


        function getStudentAnswersByStudentId(studentId, examStudents) {
            for (let i = 0; i < examStudents.length; i++) {
                if (examStudents[i].student.id === parseInt(studentId)) {
                    return examStudents[i];
                }
            }
        }

        function setGrade(questionAnswerId, grade) {
            $.ajax({
                url: "/api/teacher/course/exam/set-grade/" + questionAnswerId + "/" + grade,
                method: "PUT",
                contentType: "application/json",
                success: function (response) {
                    alert("done!")
                },
                error: function (erorMessage) {
                    alert("score is so high");
                }
            })
        }

        function setAutoGrade(examId, courseId) {
            $.ajax({
                url: "/api/teacher/course/exam/auto-set-grade/" + examId + "/" + courseId,
                method: "POST",
                contentType: "application/json",
                success: function (response) {
                    alert("done!")
                },
                error: function (erorMessage) {
                    alert("you have to do this after the exam");
                }
            })
        }

        function getExamStudents(examId) {
            var examStudents = [];
            $.ajax({
                url: "/api/teacher/course/exam/get-students-answers/" + examId,
                method: "GET",
                async: false,
                contentType: "application/json",
                success: function (response) {
                    examStudents = response;
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
            return examStudents;
        }

        function updateExam(exam) {
            $.ajax({
                url: "/api/teacher/exam/update/" + exam.id,
                method: "PUT",
                contentType: "application/json",
                data: JSON.stringify(exam),
                success: function (response) {
                    alert("exam updated successfully");
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
        }

        function deleteExam(examId) {
            $.ajax({
                url: "/api/teacher/exam/remove/" + examId,
                method: "DELETE",
                contentType: "application/json",
                success: function (response) {
                    alert("deleted successfully");
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
        }

        function addQuestionFromBank(exam, courseId) {
            var questionBank = getQuestionsFromBank(courseId);
            viewQuestionBank(questionBank, exam);
        }

        function viewQuestionBank(questionBank, exam) {
            var viewQuestionBankCode = "<div id=\"questionBank\">\n" +
                "<h2>multiple option questions</h2>";
            var multipleQuestions = [];
            var detailQuestions = [];
            for (let i = 0; i < questionBank.length; i++) {
                if (questionBank[i].questionItemList != null) {
                    multipleQuestions.push(questionBank[i]);
                } else {
                    detailQuestions.push(questionBank[i]);
                }
            }
            for (let i = 0; i < multipleQuestions.length; i++) {
                var itemsCode = "";
                for (let j = 0; j < multipleQuestions[i].questionItemList.length; j++) {
                    var count = j + 97;
                    if (multipleQuestions[i].questionItemList[j].isRightAnswer === true) {
                        itemsCode = itemsCode.concat(
                            "<p class=\"greenText\">" + String.fromCharCode(count) + ") " + multipleQuestions[i].questionItemList[j].answer + " </p>"
                        );
                    } else {
                        itemsCode = itemsCode.concat(
                            "<p>" + String.fromCharCode(count) + ") " + multipleQuestions[i].questionItemList[j].answer + " </p>"
                        );
                    }
                }
                viewQuestionBankCode = viewQuestionBankCode.concat("<div class=\"multipleQuestion\">\n" +
                    "    <h4>" + multipleQuestions[i].title + "</h4>\n" +
                    "    <p><b>" + multipleQuestions[i].question + "</b></p>\n" +
                    "    <div class=\"multipleQuestionItems\">\n" +
                    itemsCode +
                    "    </div>\n" +
                    "    <button class=\"addQuestionFromBank\" value=\"" + multipleQuestions[i].id + "\">add to exam</button>\n" +
                    "</div>\n" +
                    "<hr>\n"
                )
            }
            viewQuestionBankCode = viewQuestionBankCode.concat("<h2>detail questions</h2>");
            for (let i = 0; i < detailQuestions.length; i++) {
                viewQuestionBankCode = viewQuestionBankCode.concat("<div class=\"detailQuestion\">\n" +
                    "            <h4>" + detailQuestions[i].title + "</h4>\n" +
                    "            <p><b>" + detailQuestions[i].question + "</b></p>\n" +
                    "            <button class=\"addQuestionFromBank\" value=\"" + detailQuestions[i].id + "\">add to exam</button>\n" +
                    "        </div>\n" +
                    "        <hr>\n");
            }
            viewQuestionBankCode = viewQuestionBankCode.concat("<div/>");
            $("article").html(viewQuestionBankCode);
            $(".addQuestionFromBank").click(function () {
                var questionInfo = {
                    questionId: $(this).val(),
                    score: $(".grade").val(),
                    examId: exam.id
                }

                $("article").html("score: <input class='grade'  type='number' step=\"0.01\"> <br> " +
                    "<button id='submitForScore'>add score</button>");
                $("#submitForScore").click(function () {
                    var grade = $(".grade").val();
                    if (grade === null || grade === undefined || grade === "") {
                        alert("enter score please")
                    } else {
                        questionInfo.score = grade;
                        for (var i = 0; i < questionBank.length; i++) {
                            if (questionBank[i].id === questionInfo.questionId) {
                                exam.examQuestionList.push(questionBank[i]);
                                break;
                            }
                        }
                        addFromQuestionBank(questionInfo);
                        viewTeacherExam(exam)
                    }
                })
            })
        }

        function addFromQuestionBank(questionInfo) {
            $.ajax({
                url: "/api/teacher/course/exam/add-question",
                method: "PUT",
                contentType: "application/json",
                data: JSON.stringify(questionInfo),
                success: function (response) {
                    alert("question added successfully");
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
        }

        function getQuestionsFromBank(courseId) {
            var questionBank;
            $.ajax({
                url: "/api/teacher/course/exam/find-questions/" + courseId,
                method: "GET",
                async: false,
                contentType: "application/json",
                success: function (response) {
                    questionBank = response;
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
            return questionBank;
        }

        function editQuestion(questionId, exam) {
            var question = findQuestionById(questionId);
            viewQuestion(question, exam)
        }

        function viewQuestion(question, exam) {
            if (question.question.questionItemList !== null) {
                var itemsCode = "";
                for (var i = 0; i < question.question.questionItemList.length; i++) {
                    var count = i + 97;
                    itemsCode = itemsCode.concat("<p><input type=\"radio\" name=\"newQuestionItem\" value=\"\" required> " + String.fromCharCode(count) + ") " + question.question.questionItemList[i].answer + "</p>");
                }
                $("article").html("<h2>edit multiple option question</h2>\n" +
                    "    <form id=\"editQuestionForm\">\n" +
                    "        <div><b>question text:</b><textarea id=\"questionText\"></textarea></div>\n" +
                    "        <div><b>question title:</b><input id=\"questionTitle\" type='text' placeholder='title'></input></div>\n" +
                    "        <div>\n" +
                    "            <b>question grade:  </b><input id=\"questionDefaultGrade\"  type=\"number\" step=\"0.01\" placeholder=\"grade\">\n" +
                    "        </div>\n" +
                    "        <div>\n" +
                    itemsCode +
                    "            <p class=\"greenText largerText\">select the right answer</p>\n" +
                    "        </div>\n" +
                    "        <div>\n" +
                    "            <input id=\"addNewItem\" type=\"text\" placeholder=\"enter new item's text here\">\n" +
                    "            <button type=\"button\" id=\"newItemButton\">add an item</button>\n" +
                    "        </div>\n" +
                    "        <input id=\"saveMultipleQuestion\" type=\"submit\" value=\"save edit\">\n" +
                    "    </form>");
                $("#questionTitle").val(question.title);
                $("#questionText").html(question.question);
                $("#questionDefaultGrade").val(question.score);
                $("#newItemButton").click(function () {
                    var questionItem = {
                        answer: $("#addNewItem").val(),
                        isRightAnswer: false
                    }
                    question.title = $("#questionTitle").val();
                    question.question = $("#questionText").val();
                    question.score = $("#questionDefaultGrade").val();
                    question.questionItemList.push(questionItem);
                    viewQuestion(question);
                })
                $("#saveMultipleQuestion").click(function (event) {
                    event.preventDefault();
                    question.title = $("#questionTitle").val();
                    question.question = $("#questionText").val();
                    question.score = $("#questionDefaultGrade").val();
                })
            } else {
                $("article").html("<h2>edit detail question</h2>\n" +
                    "    <form id=\"newQuestionForm\">\n" +
                    "        <div><b>question text:</b><textarea id=\"questionText\">" + question.question + "</textarea></div>\n" +
                    "        <div><b>question title:</b><input id=\"questionTitle\" type='text' placeholder='title'></input></div>\n" +
                    "        <div>\n" +
                    "            <b>enter a default grade for question:  </b><input id=\"questionDefaultGrade\" type=\"number\" step=\"0.01\" placeholder=\"grade\">\n" +
                    "        </div>\n" +
                    "        <input id=\"saveDetailQuestion\" type=\"submit\" value=\"update question\">\n" +
                    "    </form>");
                $("#questionTitle").val(question.title);
                $("#questionDefaultGrade").val(question.score);
                $("#saveDetailQuestion").click(function (event) {
                    event.preventDefault();
                    question.title = $("#questionTitle").val();
                    question.question = $("#questionText").val();
                    question.score = $("#questionDefaultGrade").val();
                })
            }
            updateQuestion(question);
            exam.examQuestionList.push(multipleOptionQuestion);
            viewTeacherExam(exam);
        }

        function updateQuestion(question) {
            $.ajax({
                url: "",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(question),
                success: function (response) {
                    console.log(response);
                    alert("question updated successfully");
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
        }

        function findQuestionById(questionId) {
            var question = {};
            $.ajax({
                url: "/api/teacher/course/exam/find-by-id/" + questionId,
                method: "GET",
                contentType: "application/json",
                async: false,
                dataType: "json",
                success: function (response) {
                    question = response;
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
            return question;
        }

        function addNewExam(courseId) {
            $("article").html("<form id=\"newExamForm\">\n" +
                "        <div>exam time(per minute):<br><input id='time' type=\"number\" max=\"180\" min=\"1\" placeholder=\"time\"></div>\n" +
                "        <div>start date:<br><input id='start' type=\"datetime-local\"></div>\n" +
                "        <div>title:<br><input id=\"title\" type=\"text\" placeholder=\"title\"></div>\n" +
                "        <div>description:<br><textarea id=\"description\" placeholder=\"description\"></textarea></div>\n" +
                "        <input type=\"submit\" value=\"save\">\n" +
                "    </form>")
            $("#newExamForm").submit(function (event) {
                event.preventDefault();
                var exam = {
                    time: 1,
                    title: "",
                    description: "",
                    startDateAndTime: null,
                    courseId: 0
                }
                exam.time = $("#time").val();
                exam.title = $("#title").val();
                exam.description = $("#description").val();
                exam.startDateAndTime = $("#start").val();
                exam.courseId = courseId;
                $.ajax({
                    url: "/api/teacher/exam/create",
                    method: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(exam),
                    success: function (response) {
                        console.log(response);
                        alert("exam saved successfully");
                    },
                    error: function (erorMessage) {
                        console.log(erorMessage);
                    }
                })
            })
        }

        function viewTeacherCourse(course) {
            var exam = course.exam;
            var viewTeacherCourseCode = "<section>\n" +
                "                    <button id=\"addTestButton\">\n" +
                "                        <svg xmlns=\"http://www.w3.org/2000/svg\" class=\"bi bi-file-earmark-plus\" viewBox=\"0 0 16 16\">\n" +
                "                            <path\n" +
                "                                d=\"M8 6.5a.5.5 0 0 1 .5.5v1.5H10a.5.5 0 0 1 0 1H8.5V11a.5.5 0 0 1-1 0V9.5H6a.5.5 0 0 1 0-1h1.5V7a.5.5 0 0 1 .5-.5z\"/>\n" +
                "                            <path\n" +
                "                                d=\"M14 4.5V14a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2h5.5L14 4.5zm-3 0A1.5 1.5 0 0 1 9.5 3V1H4a1 1 0 0 0-1 1v12a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V4.5h-2z\"/>\n" +
                "                        </svg>\n" +
                "                    </button>";
            for (var i = 0; i < exam.length; i++) {
                viewTeacherCourseCode = viewTeacherCourseCode.concat("<button class=\"test\" name=\"testId\" value=\"" + exam[i].id + "\">\n" +
                    "                        <p><b>" + exam[i].title + "</b></p>\n" +
                    "                        <p>" + exam[i].time + " minutes" + "</p>\n" +
                    "                    </button>");
            }
            viewTeacherCourseCode = viewTeacherCourseCode.concat("</section>");
            $("article").html(viewTeacherCourseCode);
        }

        function getTeacherCourse(courseId, courseList) {
            var teacherCourse;
            for (let i = 0; i < courseList.length; i++) {
                if (courseId == courseList[i].id) {
                    teacherCourse = courseList[i];
                }
            }
            return teacherCourse;
        }

        function viewTeacherCourses(teacherCourses) {
            var teacherCoursesCode = "";
            for (let i = 0; i < teacherCourses.length; i++) {
                teacherCoursesCode = teacherCoursesCode.concat(" <button class=\"masterCourse\" value=\"" + teacherCourses[i].id + "\">\n" +
                    "        <span>" + teacherCourses[i].title + "</span>\n" +
                    "        <span>" + teacherCourses[i].startDate + "</span>\n" +
                    "        <span>" + teacherCourses[i].endDate + "</span>\n" +
                    "    </button>")
            }
            $("article").html(teacherCoursesCode);
        }

        function getTeacherCourses() {
            var teacherCourses = [];
            $.ajax({
                url: "/api/teacher/course/find",
                method: "GET",
                contentType: "application/json",
                async: false,
                dataType: "json",
                success: function (response) {
                    teacherCourses = response;
                },
                error: function (erorMessage) {
                    console.log(erorMessage);
                }
            })
            return teacherCourses;
        }

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
                    roles: [{name: role}],
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
            coursesCode = "";
            let viewCourseCode = {
                studentsCode: "",
                masterCode: "",
                otherStudents: "",
            };
            getCourses();

            function getCourses() {
                $.ajax({
                    url: "/api/admin/course/find-all",
                    method: "GET",
                    contentType: "application/json",
                    dataType: "json",
                    success: function (response) {
                        for (var i = 0; i < response.length; i++) {
                            coursesCode = coursesCode.concat("<button value=\"" + response[i].id + "\" class=\"course\">\
                            <span>" + response[i].title + "</span><span>" + response[i].startDate + "</span><span>" + response[i].endDate + "</span>\
                            </button>");
                        }
                        viewCourses(coursesCode, "", "", "");
                        $(".courseSpec").hide();
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
                    <button id=\"addNewStudentToCourse\" value=''><b>add another student</b></button>" +
                    otherStudentsCode +
                    "</div>\
                    </div>\
                ")
                $(".course").click(function () {
                    var courseId = $(this).attr('value');
                    viewSingleCourse(courseId);
                })
            }

            function getCourseById(id) {
                $.ajax({
                    url: "/api/admin/course/find-by-id/" + id,
                    async: false,
                    method: "GET",
                    contentType: "application/json",
                    dataType: "json",
                    success: function (response) {
                        var otherMastersCode = "<select id='otherMasters'>";
                        for (var i = 0; i < response.teachersNotInCourse.length; i++) {
                            otherMastersCode = otherMastersCode.concat("<option value='" + response.teachersNotInCourse[i].id + "'>"
                                + response.teachersNotInCourse[i].name + " " + response.teachersNotInCourse[i].lastname + "</option>")
                        }
                        otherMastersCode = otherMastersCode.concat("</select>");
                        viewCourseCode.masterCode = "<div class=\"courseDetail\">\
                        <div class=\"name\">" + response.teacherDto.name + " " + response.teacherDto.lastname + "</div>\
                        <button id=\"changeMaster\" value=\"" + response.teacherDto.id + "\">change</button>" +
                            otherMastersCode +
                            "<button id=\"changeMasterForThisCourse\" value=\"" + response.teacherDto.id + "\">ok</button>\
                        </div>";
                        console.log(response);
                        console.log(viewCourseCode.masterCode);
                        for (var j = 0; j < response.studentDtoList.length; j++) {
                            viewCourseCode.studentsCode = viewCourseCode.studentsCode.concat("<div class=\"courseDetail\">\
                        <div class=\"name\">" + response.studentDtoList[j].name + " " + response.studentDtoList[j].lastname + "</div>\
                        <button id=\"removeStudentFromCourse\" value=\"" + response.studentDtoList[j].id + "\">remove from course</button>\
                        </div>");
                        }
                        response.studentsNotInCourse.forEach(s => {
                            viewCourseCode.otherStudents = viewCourseCode.otherStudents.concat("<button class=\"courseDetail otherStudent\" value=\"" + s.id + "\">\
                    <div class=\"details\">" + s.name + " " + s.lastname + "</div>\
                    </button>")
                        });
                    }, error: function (erorMessage) {
                        console.log(erorMessage);
                    }
                })
                return viewCourseCode;
            }

            function viewSingleCourse(courseId) {
                viewCourseCode.otherStudents = "";
                viewCourseCode.studentsCode = "";
                viewCourseCode.masterCode = "";
                viewCourseCode = getCourseById(courseId);
                console.log(viewCourseCode)
                viewCourses(coursesCode, viewCourseCode.masterCode, viewCourseCode.studentsCode, viewCourseCode.otherStudents);
                $(".courseSpec").show();
                $("#otherMasters").hide();
                $("#changeMasterForThisCourse").hide();
                $("#changeMaster").click(function () {
                    $("#otherMasters").show();
                    $("#changeMasterForThisCourse").show();
                })
                $("#changeMasterForThisCourse").click(function () {
                    var newMasterId = $("#otherMasters").val();
                    changeCourseMasteer(courseId, newMasterId);
                })
                $("#removeStudentFromCourse").click(function () {
                    var studentId = $(this).attr('value');
                    removeStudentFromCourse(courseId, studentId);
                    viewSingleCourse(courseId);
                })
                $(".otherStudent").click(function () {
                    $("#addNewStudentToCourse").val($(this).attr('value'));
                    $(".otherStudent").css("background", "rgb(150, 17, 162)");
                    $(this).css("background", "rgb(102,7,108)");
                })
                $("#addNewStudentToCourse").click(function () {
                    var newStudentId = $(this).val();
                    if (newStudentId != null) {
                        addStudentToCourse(courseId, newStudentId);
                    } else {
                        alert("please select a student first!");
                    }
                })
            }

            function changeCourseMasteer(courseId, newMasterId) {
                $.ajax({
                    url: "/api/admin/course/add-teacher/" + courseId + "/" + newMasterId,
                    method: "PUT",
                    contentType: "application/json",
                    success: function (response) {
                        viewSingleCourse(courseId);
                        alert("master for this course has changed...");
                    }, error: function (erorMessage) {
                        console.log(erorMessage);
                    }
                })
            }

            function addStudentToCourse(courseId, newStudentId) {
                $.ajax({
                    url: "/api/admin/course/add-student/" + courseId + "/" + newStudentId,
                    method: "PUT",
                    contentType: "application/json",
                    success: function (response) {
                        alert("student added to this course");
                    }, error: function (erorMessage) {
                        console.log(erorMessage);
                    }
                })
            }

            function removeStudentFromCourse(courseId, studentId) {
                $.ajax({
                    url: "/api/admin/course/remove-student/" + courseId + "/" + studentId,
                    method: "DELETE",
                    contentType: "application/json",
                    success: function (response) {
                        alert("student removed from this course");
                    }, error: function (erorMessage) {
                        console.log(erorMessage);
                    }
                })
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

    }
)