package project.ses.entities;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BatchRepo extends JpaRepository<Batch, Integer> {
	
	//6
	@Query("SELECT b FROM Batch b WHERE :currentDate BETWEEN b.start AND b.end")
	List<Batch> findAllRunningBatches(@Param("currentDate") LocalDate currentDate);
	
	// 7
	@Query("SELECT b FROM Batch b WHERE b.end < :currentDate")
	List<Batch> findAllCompletedBatches(@Param("currentDate") LocalDate currentDate);
	
	//11
	@Query("SELECT b FROM Batch b WHERE b.code = :code")
    List<Batch> findAllBatchesByCourse(@Param("code") String code);
	
	//13
	@Query("SELECT b FROM Batch b WHERE b.start BETWEEN :date1 AND :date2")
	List<Batch> getAllBatchesBetweenDates(@Param("date1") LocalDate date1, @Param("date2") LocalDate date2);
}