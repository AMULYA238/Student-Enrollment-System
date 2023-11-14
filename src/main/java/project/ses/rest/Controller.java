package project.ses.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import project.ses.entities.Batch;
import project.ses.entities.BatchRepo;
import project.ses.entities.Course;
import project.ses.entities.CourseRepo;
import project.ses.entities.Payment;
import project.ses.entities.PaymentRepo;
import project.ses.entities.Student;
import project.ses.entities.StudentPaymentDTO;
import project.ses.entities.StudentRepo;

@RestController
public class Controller {

	@Autowired
	CourseRepo courseRepo;

	@Autowired
	private BatchRepo batchRepo;

	@Autowired
	private StudentRepo studentRepo;

	@Autowired
	private PaymentRepo paymentRepo;

	// 1
	@PostMapping("/courses/add/{code}")
	@Operation(summary = "Add a new Course", description = "Adding a new course details")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Added new Course Successfully"),
			@ApiResponse(responseCode = "400", description = "Course Code is Already Present"),
			@ApiResponse(responseCode = "500", description = "An issue occurred on the server while processing a request ") })
	public Object addNewCourse(@Valid @PathVariable("code") String code, @Valid @RequestBody Course newCourse) {
		var course = courseRepo.findById(code);
		try {
			if (course.isPresent()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CourseCode Already Present");
			}
			courseRepo.save(newCourse);
			return newCourse;
		}
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@PutMapping("/courses/update/{code}")
	@Operation(summary = "Update a Course By CourseCode", description = "Given a CourseCode it Updates the existing course by the given CourseCode")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Updated Course Successfully"),
			@ApiResponse(responseCode = "404", description = "CourseCode Not Found"),
			@ApiResponse(responseCode = "500", description = "an issue occurred on the server while processing a request") })
	public Object updateCourse(@PathVariable("code") String code, @Valid @RequestBody Course newCourse) {
		var optCourse = courseRepo.findById(code);
		try {
			if (optCourse.isPresent()) {
				Course course = optCourse.get();
				course.setName(newCourse.getName());
				course.setDuration(newCourse.getDuration());
				course.setFee(newCourse.getFee());
				return courseRepo.save(course);
			} 
			else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Name Not Found!");
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@DeleteMapping("/courses/delete/{code}")
	@Operation(summary = "Delete a Course By CourseCode", description = "Given a CourseCode it deletes the existing Course by the given CourseCde")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted Course Successfully"),
			@ApiResponse(responseCode = "404", description = "Given CourseCode not found"),
			@ApiResponse(responseCode = "500", description = "an issue occurred on the server while processing a request") })
	public void deleteOneCourse(@PathVariable("code") String code) {
		var course = courseRepo.findById(code);
		try {
			if (course.isPresent()) {
				courseRepo.deleteById(code);
			} 
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course code Not Found!");
			}
		} 
		catch(ResponseStatusException ex) {
			throw ex;
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

//	// 2
	@PostMapping("/batches/add")
	@Operation(summary = "Add a new Batch", description = "Adding a new batch details")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Added new Batch Successfully"),
			@ApiResponse(responseCode = "500", description = "an issue occurred on the server while processing a request") })
	public Batch addNewBatch(@Valid @RequestBody Batch newBatch) {
		try {
			batchRepo.save(newBatch);
			return newBatch;
		}
		
		catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@PutMapping("/batches/update/{batchid}")
	@Operation(summary = "Update a Batch By Batch Id", description = "Updating an existing batch by the given Batch Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Updated Batch Successfully"),
			@ApiResponse(responseCode = "404", description = "BatchId Not Found"),
			@ApiResponse(responseCode = "500", description = "an issue occurred on the server while processing a request") })
	public Object updateBatch(@PathVariable("batchid") int batchid, @Valid @RequestBody Batch newBatch) {
		var batch = batchRepo.findById(batchid);
		try {
			if (batch.isPresent()) {
				Batch batches = batch.get();
				batches.setCode(newBatch.getCode());
				batches.setStart(newBatch.getStart());
				batches.setEnd(newBatch.getEnd());
				batches.setTimings(newBatch.getTimings());
				batches.setDuration(newBatch.getDuration());
				batches.setFee(newBatch.getFee());
				return batchRepo.save(batches);
			} 
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch id not found");
			}
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@DeleteMapping("/batches/delete/{batchid}")
	@Operation(summary = "Delete a Batch By Batch Id", description = "Deleting an existing batch by the given Batch Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted Batch Successfully"),
			@ApiResponse(responseCode = "404", description = "BatchId not found"),
			@ApiResponse(responseCode = "500", description = "an issue occurred on the server while processing a request") })
	public void deleteBatch(@PathVariable("batchid") int batchid) {
		var batch = batchRepo.findById(batchid);
		try {
			if (batch.isPresent()) {
				batchRepo.deleteById(batchid);
			} 
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch id not found");
			}
		}
		catch(ResponseStatusException ex) {
			throw ex;
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 3
	@PostMapping("/addstudentandpayment")
	@Operation(summary = "Add Student and Payment", description = "while adding new student,add payment details to that student")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Added new Student And Payment Successfully"),
			@ApiResponse(responseCode = "500", description = "an issue occurred on the server while processing a request") })
	@Transactional
	public Object addNewStudentPayment(@Valid @RequestBody StudentPaymentDTO studentpayment) {
		try {
			Student st = studentRepo.findByEmail(studentpayment.getEmail());
			if (st != null) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Already Present");
			}
			Student s = new Student();
			s.setName(studentpayment.getName());
			s.setEmail(studentpayment.getEmail());
			s.setMobile(studentpayment.getMobile());
			s.setBatch_id(studentpayment.getBatchid());
			s.setDoj(studentpayment.getDoj());
			List<Student> students = studentRepo.findAll();
			boolean exist = students.contains(s);
			if (exist)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student Already Present");
			studentRepo.save(s);
			Payment p = new Payment();
			p.setId(s.getId());
			p.setAmount(studentpayment.getAmount());
			p.setPaydate(studentpayment.getPaydate());
			p.setPaymode(studentpayment.getPaymode());
			paymentRepo.save(p);
			return studentpayment;
		}
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@PutMapping("/updatestudentandpayment/{id}")
	@Operation(summary = "Update a Student and Payment By Student Id", description = "While updating an existing Student,update Payment details if requires")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated Student And Payment Successfully"),
			@ApiResponse(responseCode = "404", description = "Student Id Not Found"),
			@ApiResponse(responseCode = "500", description = "An issue occurred on the server while processing a request") })
	@Transactional
	public Object updateStudentAndPayment(@PathVariable("id") Integer id,
			@Valid @RequestBody StudentPaymentDTO studentandpayment) {
		var student = studentRepo.findById(id);
		try {
			if (student.isPresent()) {
				Student s = student.get();
				s.setName(studentandpayment.getName());
				s.setEmail(studentandpayment.getEmail());
				s.setMobile(studentandpayment.getMobile());
				s.setBatch_id(studentandpayment.getBatchid());
				s.setDoj(studentandpayment.getDoj());
				studentRepo.save(s);
				Optional<Payment> payment = paymentRepo.findById(id);
				Payment p = payment.get();
				p.setAmount(studentandpayment.getAmount());
				p.setPaydate(studentandpayment.getPaydate());
				p.setPaymode(studentandpayment.getPaymode());
				paymentRepo.save(p);
			} 
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student Id Not Found!");
			}
			return studentandpayment;
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An issue occurred on the server while processing the request");
		}
	}

	@DeleteMapping("/students/delete/{id}")
	@Operation(summary = "Delete a Student and Payment By Student Id", description = "While deleting an existing Student,also delete Payment details of that Student")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Deleted Student By Student Id Successful"),
			@ApiResponse(responseCode = "404", description = "Student Id not found"),
			@ApiResponse(responseCode = "500", description = "An issue occurred on the server while processing a request") })
	public void deleteStudent(@PathVariable("id") int id) {
		var student = studentRepo.findById(id);
		try {
			if (student.isPresent())
				studentRepo.deleteById(id);
			else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student Id Not Found!");
		} 
		catch(ResponseStatusException ex) {
			throw ex;
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 4
	@PutMapping("/payments/update/{payid}")
	@Operation(summary = "Update a Payment By Pay Id", description = "Updating an existing Payment by the given Pay Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Updated Payment Successful"),
			@ApiResponse(responseCode = "404", description = "PayId Not Found"),
			@ApiResponse(responseCode = "500", description = "An issue occurred on the server while processing a request") })
	public Object UpdatePament(@Valid @PathVariable("payid") int payid, @Valid @RequestBody Payment newPayment) {
		var payment = paymentRepo.findById(payid);
		try {

			if (payment.isPresent()) {
				Payment payments = payment.get();
				payments.setAmount(newPayment.getAmount());
				payments.setPaydate(newPayment.getPaydate());
				payments.setPaymode(newPayment.getPaymode());
				return paymentRepo.save(payments);
			} 
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment id not found");
			}
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		 catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 5
	@GetMapping("/courses")
	@Operation(summary = "Get All Courses", description = "Get All Courses")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieved All Courses Successfully"),
			@ApiResponse(responseCode = "500", description = "An issue occurred on the server while processing a request") })
	public List<Course> getAllCourses() {
		try {
			return courseRepo.findAll();
		} 
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/courses/bycode/{code}")
	public Object getOneCourse(@PathVariable("code") String code) {
		try {
			// Your code to retrieve and return the course
			var course = courseRepo.findById(code);
			if (course.isPresent()) {
				return course.get();
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coursecode Not Found!");
			}
		}
			catch(ResponseStatusException ex) {
				return ex.getMessage();
			}
		 catch (Exception e) {
			return e.getMessage(); // Handle and return the exception message
		}
	}

	// 6
	@GetMapping("/batches/running")
	@Operation(summary = "Get All Current Running Batches", description = "Get All Batches That Currently Runnig")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieved All Batches Successfully"),
			@ApiResponse(responseCode = "500", description = "An issue occurred on the server while processing a request") })
	public Object getCurrentRunningBatches() {
		LocalDate currentDate = LocalDate.now();
		List<Batch> batches = batchRepo.findAllRunningBatches(currentDate);
		try {
			if (batches.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Running Batches are present");
			}
			return batches;
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 7
	@GetMapping("/batches/completed")
	@Operation(summary = "Get All Completed Batches", description = "Get All Batches That Are Completed")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieved All Batches Successfully"),
			@ApiResponse(responseCode = "500", description = "An issue occurred on the server while processing a request") })
	public Object getCompletedBatches() {
		LocalDate currentDate = LocalDate.now();
		List<Batch> batches = batchRepo.findAllCompletedBatches(currentDate);
		try {
			if (batches.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No completed Batches are there");
			}
			return batches;
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 8
	@GetMapping("/students/running")
	@Operation(summary = "Get All Running Batch's Students", description = "Get All Students Who Are In Current Running Batches ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieved All Students Successfully"),
			@ApiResponse(responseCode = "500", description = "An issue occurred on the server while processing a request") })
	public Object getCurrentRunningStudents() {
		LocalDate currentDate = LocalDate.now();
		List<Student> students = studentRepo.findAllCurrentRunningStudents(currentDate);
		try {
			if (students.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						"No students are there in current running batches");
			}
			return students;
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 9
	@GetMapping("/students/coursecode/{code}")
	@Operation(summary = "Get All Courses By CourseCode", description = "Get All Courses By Given CourseCode")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieved ALl Studetns Successful"),
			@ApiResponse(responseCode = "404", description = "CourseCode Not Found"),
			@ApiResponse(responseCode = "500", description = "An issue occurred on the server while processing a request") })
	public Object getStudentsByCourse(@PathVariable String code) {
		List<Student> students = studentRepo.findAllStudentsByCourse(code);
		try {
			if (students.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No students are there in specific coursecode");
			}
			return students;
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 10
	@GetMapping("/students/batchid/{batchid}")
	@Operation(summary = "Get All Students By Batch Id", description = "Get All Students By Given Batch Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retreived All Studetns Successful"),
			@ApiResponse(responseCode = "404", description = "BatchId Not Found"),
			@ApiResponse(responseCode = "500", description = "An issue occurred on the server while processing a request") })
	public Object getStudentsByBatch(@PathVariable int batchid) {
		List<Student> students = studentRepo.findAllStudentsByBatch(batchid);
		try {
			if (students.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No studetns are there in specific batch ");
			}
			return students;
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 11
	@GetMapping("/batches/coursecode/{courseCode}")
	@Operation(summary = "Get All Batches By CourseCode", description = "Get All Batches By CourseCode")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieved All Batches Successful"),
			@ApiResponse(responseCode = "404", description = "CourseCode Not Found"),
			@ApiResponse(responseCode = "500", description = "An issue occurred on the server while processing a request") })
	public Object getBatchesByCourse(@PathVariable String courseCode) {
		List<Batch> batches = batchRepo.findAllBatchesByCourse(courseCode);
		try {
			if (batches.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No batches are there in this " + courseCode);
			}
			return batches;
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 12
	@GetMapping("students/search")
	@Operation(summary = "Get All Students By Partial Name", description = "Get All Students By Given Partial Name")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieved All Students Successfully"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	public Object getNameByPartialName(@RequestParam("string") String string) {
		List<Student> students = studentRepo.findByNameContaining(string);
		try {
			if (students.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No students found with that partial name");
			}
			return students;
		}
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 13
	@GetMapping("/batches/between")
	@Operation(summary = "Get All Batches Between Two Dates", description = "Get All Batches Started Between Given Two Dates")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieved All Batches Successfully"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	public Object getBatchesBetweenDates(@RequestParam("date1") LocalDate date1,
			@RequestParam("date2") LocalDate date2) {
		List<Batch> batches = batchRepo.getAllBatchesBetweenDates(date1, date2);
		try {
			if (batches.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No batches are there between specified dates");
			}
			return batches;
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 14
	@GetMapping("/payments/batchid/{batchid}")
	@Operation(summary = "Get All Payments By Batch Id", description = "Get All Payments By Given Batch Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieved All Payments Successful"),
			@ApiResponse(responseCode = "404", description = "BatchId Not Found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	public Object getPaymentsByBatchId(@PathVariable("batchid") Integer batchid) {
		List<Payment> payments = paymentRepo.findPaymentsByBatchId(batchid);
		try {
			if (payments.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No payments are there in specified batch id");
			}
			return payments;
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 15
	@GetMapping("/payments/between")
	@Operation(summary = "Get All Payments Between Two Dates", description = "Get All Payments That Are Done Between Given Two Dates")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieved All Payments Successfully"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	public Object getPaymentBetweenDates(@RequestParam("startDate") LocalDate startDate,
			@RequestParam("endDate") LocalDate endDate) {
		List<Payment> payments = paymentRepo.findPaymentsBetweenDates(startDate, endDate);
		try {
			if (payments.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No payments are there in specified dates");
			}
			return payments;
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 16
	@GetMapping("/payments/paymode/{paymode}")
	@Operation(summary = "Get All Payments By PayMode", description = "Get All Payments By Given PayMode")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieved All Payments Successful"),
			@ApiResponse(responseCode = "404", description = "Invalid PayMode"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	public Object getOneBatch(@PathVariable("paymode") Character paymode) {
		var payment = paymentRepo.findByPaymode(paymode);
		try {
			if (payment.isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment Mode Not Found!");
			return payment;
		} 
		catch(ResponseStatusException ex) {
			return ex.getMessage();
		}
		catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}
}
