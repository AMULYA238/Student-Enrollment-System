package project.ses.entities;

import java.time.LocalDate;
public class StudentPaymentDTO {

	private String name;
	private String email;
	private String mobile;
	private int batchid;
	private LocalDate doj;
	private int amount;
	private LocalDate paydate;
	private Character paymode;
	public StudentPaymentDTO(String name, String email, String mobile, int batchid, LocalDate doj,
			int amount, LocalDate paydate, Character paymode) {
		super();
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.batchid = batchid;
		this.doj = doj;
		this.amount = amount;
		this.paydate = paydate;
		this.paymode = paymode;
	}
	public StudentPaymentDTO() {
		super();
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setBatchid(int batchid) {
		this.batchid = batchid;
	}
	public void setDoj(LocalDate doj) {
		this.doj = doj;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setPaydate(LocalDate paydate) {
		this.paydate = paydate;
	}
	public void setPaymode(Character paymode) {
		this.paymode = paymode;
	}
	public int getBatchid() {
		return batchid;
	}
	public LocalDate getDoj() {
		return doj;
	}
	public int getAmount() {
		return amount;
	}
	public LocalDate getPaydate() {
		return paydate;
	}
	public Character getPaymode() {
		return paymode;
	}
}




