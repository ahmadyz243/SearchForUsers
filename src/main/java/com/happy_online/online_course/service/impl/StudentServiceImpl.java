package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.exception.NotFoundException;
import com.happy_online.online_course.mapper.ExamQuestionMapper;
import com.happy_online.online_course.mapper.StudentMapper;
import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.models.Student;
import com.happy_online.online_course.models.User;
import com.happy_online.online_course.payload.request.SignupRequest;
import com.happy_online.online_course.payload.response.*;
import com.happy_online.online_course.repository.StudentRepository;
import com.happy_online.online_course.repository.UserRepository;
import com.happy_online.online_course.service.CourseService;
import com.happy_online.online_course.service.ExamService;
import com.happy_online.online_course.service.StudentService;
import com.happy_online.online_course.service.base.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl extends BaseServiceImpl<Student, Long, StudentRepository> implements StudentService {
    public StudentServiceImpl(StudentRepository repository, ExamService examService, StudentMapper studentMapper, ExamQuestionMapper examQuestionMapper, UserRepository userRepository) {
        super(repository);
        this.examService = examService;
        this.studentMapper = studentMapper;
        this.examQuestionMapper = examQuestionMapper;
        this.userRepository = userRepository;
    }

    private final ExamService examService;
    private final StudentMapper studentMapper;
    private final ExamQuestionMapper examQuestionMapper;
    final UserRepository userRepository;

    @Lazy
    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    private CourseService courseService;

    @Override
    @Transactional
    public void saveStudentWithUser(SignupRequest signupRequest, User user) {
        userRepository.save(user);
        Student student = mapSignUpRequestToStudent(signupRequest);
        student.setUser(user);
        repository.save(student);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        repository.deleteByUsername(username);
    }

    @Override
    public List<StudentDto> findStudentsNotInCourse(Long courseId) {

        List<StudentDto> studentDtoList = new ArrayList<>();
        List<Student> students = repository.findByCourseListNotContaining(courseService.findById(courseId));
        students.forEach(student -> {
            StudentDto studentDto = new StudentDto();
            studentDto.setId(student.getId());
            BeanUtils.copyProperties(student, studentDto);
            studentDtoList.add(studentDto);
        });
        return studentDtoList;
    }

    @Override
    public Student findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new NotFoundException("user not found"));
    }

    @Override
    public List<ExamStudentsResponse> findAllStudentsWithAnswers(Long exam_id) {
        List<ExamStudentsResponse> examStudentsResponses = new ArrayList<>();
        Exam exam = examService.findById(exam_id);
        exam.getStudentAnswers().forEach(studentAnswers -> {
            ExamStudentsResponse examStudentsResponse = new ExamStudentsResponse();
            StudentResponse studentResponse = studentMapper.mapStudentToStudentResponse(studentAnswers.getStudent());
            examStudentsResponse.setStudent(studentResponse);
            LastStudentsAnswersResponse studentAnswersResponse = new LastStudentsAnswersResponse();
            studentAnswersResponse.setStudent(studentResponse);
            final ExamQuestionStudentAnswersResponse[] examQuestionStudentAnswer = {new ExamQuestionStudentAnswersResponse()};
            studentAnswers.getExamQuestionAnswerList().forEach(answer -> {
                examQuestionStudentAnswer[0] = new ExamQuestionStudentAnswersResponse();
                ExamQuestionResponse examQuestionResponse = examQuestionMapper.examQuestionToExamQuestionResponse(answer.getExamQuestion());
                examQuestionResponse.setExamQuestionId(answer.getExamQuestion().getId());
                examQuestionStudentAnswer[0].setAnswer(answer.getAnswer());
                examQuestionStudentAnswer[0].setExamQuestion(examQuestionResponse);
                studentAnswersResponse.setAnswers(examQuestionStudentAnswer[0]);
            });
            //---------------------IN THE END OF LOOP----------------------
            examStudentsResponse.setStudentAnswers(studentAnswersResponse);
            examStudentsResponses.add(examStudentsResponse);
        });

        return examStudentsResponses;
    }

    public Student mapSignUpRequestToStudent(SignupRequest signupRequest) {
        Student student = new Student();
        BeanUtils.copyProperties(signupRequest, student);
        return student;
    }
}