package project.ses.entities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepo extends JpaRepository<Student,Integer>{
	
//3	
	Student findByEmail(String email);
	
//8
	@Query("SELECT s FROM Student s WHERE s.batch.start <= :currentDate AND s.batch.end >= :currentDate")
	List<Student> findAllCurrentRunningStudents(@Param("currentDate") LocalDate currentDate);
	
//9
	@Query("SELECT s FROM Student s WHERE s.batch.code = :code")
	List<Student> findAllStudentsByCourse(@Param("code") String courseCode);
	
//10
	@Query("SELECT s FROM Student s WHERE s.batch_id = :batchid")
	List<Student> findAllStudentsByBatch(@Param("batchid") int batch_id);
	
//12
	List<Student> findByNameContaining(String name);

	
	
}
