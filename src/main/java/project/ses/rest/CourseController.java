package project.ses.rest;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import project.ses.entities.Course;
import project.ses.entities.CourseRepo;

//@RestController
public class CourseController {

	@Autowired
	CourseRepo courseRepo;
	
//	@Autowired
//	StudentRepo studentRepo;
	
//5
	@GetMapping("/courses")
	public List<Course> getAllCourses() {
		return courseRepo.findAll();
	}
	
//	@GetMapping("/courses/{code}")
//	public Course getOneCourse(@PathVariable("code") String code) {
//		var course = courseRepo.findById(code);
//		if (course.isPresent())
//			return course.get();
//		else
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Code Not Found!");
//	}

//1
	@PostMapping("/courses/add/{code}")
	public Course addNewCourse(@PathVariable("code") String code,@RequestBody Course newCourse) {
		var course=courseRepo.findById(code);
		if(course.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"CourseCode Already Present");
		}
		courseRepo.save(newCourse);
		return newCourse;
	}
	

	@PutMapping("/courses/update/{code}")
	public void updateCourse(@PathVariable("code") String code, @RequestBody Course newCourse) {
		var optCourse = courseRepo.findById(code);
		if (optCourse.isPresent()) {
			Course course = optCourse.get();
			course.setName(newCourse.getName());
			course.setDuration(newCourse.getDuration());
			course.setFee(newCourse.getFee());
			courseRepo.save(course);
		} else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Name Not Found!");
	}
	
	@DeleteMapping("/courses/delete/{code}")
	public void deleteOneCourse(@PathVariable("code") String code) {
		var course=courseRepo.findById(code);
		if(course.isPresent()) {
			courseRepo.deleteById(code);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course code Not Found!");
		}
	}
	
//	@GetMapping("/students/{code}")
//	public List<Student> getStudentForCourse(@PathVariable String code) {
//		List<Student>students = studentRepo.findStudentsByCourse(code);
//		return students;
//	}	
	
}