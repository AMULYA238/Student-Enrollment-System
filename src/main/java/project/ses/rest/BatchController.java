package project.ses.rest;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import project.ses.entities.Batch;
import project.ses.entities.BatchRepo;

//@RestController
//@RequestMapping("/batches")
public class BatchController {
	
	@Autowired
	private BatchRepo batchRepo;

//2
	@PostMapping("/batches/add")
	public Batch addNewBatch(@RequestBody Batch newBatch) {
		batchRepo.save(newBatch);
		return newBatch;
	}
	
	@PutMapping("/batches/update/{batchid}")
	public void updateBatch(@PathVariable("batchid") int batchid,@RequestBody Batch newBatch) {
		var batch=batchRepo.findById(batchid);
		if(batch.isPresent()) {
			Batch batches=batch.get();
			batches.setCode(newBatch.getCode());
			batches.setStart(newBatch.getStart());
			batches.setEnd(newBatch.getEnd());
			batches.setTimings(newBatch.getTimings());
			batches.setDuration(newBatch.getDuration());
			batches.setFee(newBatch.getFee());
			batchRepo.save(batches);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch id not found");
		}
	}
	
	@DeleteMapping("/batches/delete/{batchid}")
	public void deleteBatch(@PathVariable("batchid") int batchid) {
		var batch=batchRepo.findById(batchid);
		if(batch.isPresent()) {
			batchRepo.deleteById(batchid);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch id not found");
		}
	}
	
//6
	@GetMapping("/batches/running")
	public List<Batch> getCurrentRunningBatches(){
		LocalDate currentDate=LocalDate.now();
		List <Batch> batches=batchRepo.findAllRunningBatches(currentDate);
		if(batches.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Running Batches are present");
		}
		return batches;
	}
	
//7
	@GetMapping("/batches/completed")
	public List<Batch> getCompletedBatches(){
		LocalDate currentDate=LocalDate.now();
		List <Batch> batches=batchRepo.findAllCompletedBatches(currentDate);
		if(batches.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No completed Batches are there");
		}
		return batches;
	}
	
//11
	@GetMapping("/batches/coursecode/{courseCode}")
	public List<Batch> getBatchesByCourse(@PathVariable String courseCode){
		List <Batch> batches=batchRepo.findAllBatchesByCourse(courseCode);
		if(batches.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No batches are there in this " + courseCode);
		}
		return batches;
	}
	
//13
	@GetMapping("/batches/between")
	public List<Batch> getBatchesBetweenDates(@RequestParam("date1") LocalDate date1,@RequestParam("date2") LocalDate date2){
		List <Batch> batches=batchRepo.getAllBatchesBetweenDates(date1,date2);
		if(batches.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No batches are there between specified dates");
		}
		return batches;
	}
	
	

}
