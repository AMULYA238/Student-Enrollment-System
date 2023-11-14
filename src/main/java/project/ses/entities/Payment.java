package project.ses.entities;

import java.time.LocalDate;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Table(name = "payments")
@Entity
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payid")
	private int payid;

	@Column(name = "id")
	@NotBlank(message = "Student Id is required")
	private int id;

	@Column(name = "amount")
	@Positive(message = "Amount cannot be negative")
	private int amount;

	@Column(name = "paydate")
	@NotNull(message = "Payment date is required")
	private LocalDate paydate;

	@Column(name = "paymode")
	private char paymode;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", referencedColumnName = "id",insertable = false, updatable = false)
	@JsonIgnore
	private Student student;

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public int getAmount() {
		return amount;
	}

	public int getPayid() {
		return payid;
	}

	public void setPay_id(int payid) {
		this.payid = payid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmountt() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public LocalDate getPaydate() {
		return paydate;
	}

	public void setPaydate(LocalDate paydate) {
		this.paydate = paydate;
	}

	public char getPaymode() {
		return paymode;
	}

	public void setPaymode(char paymode) {
		this.paymode = paymode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Payment) {
			Payment other = (Payment) obj;
			return Objects.equals(id, other.id);
		} else
			return false;
	}

	@Override
	public String toString() {
		return "Payment [pay_id=" + payid + ", id=" + id + ", amount=" + amount + ", paydate=" + paydate + ", paymode="
				+ paymode + "]";
	}

}