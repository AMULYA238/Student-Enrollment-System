package project.ses.rest;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import project.ses.entities.Payment;
import project.ses.entities.PaymentRepo;

public class PaymentController {

		@Autowired
		private PaymentRepo paymentRepo;

//4
		@PutMapping("/payments/update/{payid}")
		public void UpdatePament(@PathVariable("payid") int payid,@RequestBody Payment newPayment) {
			var payment=paymentRepo.findById(payid);
			if(payment.isPresent()) {
				Payment payments = payment.get();
				payments.setId(newPayment.getId());
				payments.setAmount(newPayment.getAmount());
				payments.setPaydate(newPayment.getPaydate());
				payments.setPaymode(newPayment.getPaymode());
				paymentRepo.save(payments);
			}
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment id not found");
			}
		}
		
//14
		@GetMapping("/payments/batchid/{batchid}")
		public List<Payment> getPaymentsByBatchId(@PathVariable("batchid") Integer batchid){
			List<Payment> payments=paymentRepo.findPaymentsByBatchId(batchid);
			if(payments.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No payments are there in specified batch id");
			}
			return payments;
		}
		
//15
		@GetMapping("/payments/between")
		public List<Payment> getPaymentBetweenDates(@RequestParam("startDate") LocalDate startDate,
				@RequestParam("endDate") LocalDate endDate){
			List <Payment> payments=paymentRepo.findPaymentsBetweenDates(startDate,endDate);
			if(payments.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No payments are there in specified dates");
			}
			return payments;
		}
		
//16
		@GetMapping("/payments/paymode/{paymode}")
		public List<Payment> getOneBatch(@PathVariable("paymode") Character paymode) {
			var payment = paymentRepo.findByPaymode(paymode);
			if (payment.isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment Mode Not Found!");
			return payment;
		}
		
}
