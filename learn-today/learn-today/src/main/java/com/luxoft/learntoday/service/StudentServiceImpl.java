package com.luxoft.learntoday.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luxoft.learntoday.entity.Course;
import com.luxoft.learntoday.entity.Student;
import com.luxoft.learntoday.entity.StudentDTO;
import com.luxoft.learntoday.exception.ResourceAlreadyExistException;
import com.luxoft.learntoday.exception.ResourseNotFoundException;
import com.luxoft.learntoday.repository.AdminRepository;
import com.luxoft.learntoday.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public Student enrollStudent(Student student) {

		Optional<Student> existStudent = studentRepository.findById(student.getEnrollmentId());
		if (!existStudent.isEmpty()) {
			throw new ResourceAlreadyExistException(
					"Student is found with enrollment id: " + student.getEnrollmentId());
		}
		Student saved = studentRepository.save(student);
		return saved;
	}

	@Override
	public List<Course> getAllCourses() {
		List<Course> list;
		try {
			List<Course> listOfCourses = adminRepository.findAll();
			list = listOfCourses.stream().sorted((stud1, stud2) -> stud1.getStartDate().compareTo(stud2.getStartDate()))
					.collect(Collectors.toList());
		} catch (ResourseNotFoundException exception) {
			throw new ResourseNotFoundException("Course list is not found");
		}

		return list;
	}

	@Override
	public boolean deleteStudentEnrollment(Integer enrollmentId) {

		boolean student = studentRepository.findById(enrollmentId).isPresent();
		if (student) {
			studentRepository.deleteById(enrollmentId);
			return true;
		} else {
			throw new ResourseNotFoundException("Student not found");
		}
	}

	@Override
	public Student addStudent(StudentDTO dto) {
		Course course = adminRepository.findById(dto.courseId)
				.orElseThrow(() -> new ResourseNotFoundException("Course not found"));
		Student student = new Student();
		student.setEnrollmentId(dto.enrollmentId);
		student.setStudentId(dto.studentId);
		student.setStudentName(dto.studentName);
		student.setCourse(course);
		Student savedStudent = studentRepository.save(student);
		return savedStudent;

	}

}
