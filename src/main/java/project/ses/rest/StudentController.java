package project.ses.rest;

import java.time.LocalDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import project.ses.entities.Student;
import project.ses.entities.StudentRepo;

public class StudentController {

	@Autowired
	private StudentRepo studentRepo;
	
//8
	@GetMapping("/students/running")
	public List<Student> getCurrentRunningStudents(){
		LocalDate currentDate=LocalDate.now();
		List<Student> students=studentRepo.findAllCurrentRunningStudents(currentDate);
		if(students.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No students are there in current running batches");
		}
		return students;
	}
	
//9
	@GetMapping("/students/coursecode/{code}")
	public List<Student> getStudentsByCourse(@PathVariable String code){
		List<Student> students=studentRepo.findAllStudentsByCourse(code);
		if(students.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No students are there in specific coursecode");
		}
		return students;
	}
	
//10
	@GetMapping("/students/batchid/{batchid}")
	public List<Student> getStudentsByBatch(@PathVariable int batchid){
		List<Student> students=studentRepo.findAllStudentsByBatch(batchid);
		if(students.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No studetns are there in specific batch ");
		}
		return students;
	}
	
//12
	@GetMapping("students/search")
	public List<Student> getNameByPartialName(@RequestParam("string") String string){
		List<Student> students=studentRepo.findByNameContaining(string);
		if(students.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No students found with that partial name");
		}
		return students;
	}
	
	
}
