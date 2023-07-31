package com.prajyot.excel.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "folio", schema = "mydatabase")
public class FolioEntity {

	@Id
	@Column(name = "STOCK")
	private String stock;
	
	@Column(name = "QTY")
	private Integer qty;
	
	@Column(name = "AVG")
	private Float avgAmt;
	
	@Column(name = "AMT")
	private Float totalAmt;
	
	@Column(name = "weightage")
	private Float weightage;
	
	@Column(name = "SECTOR")
	private String sector;
	
	@Column(name = "CAP")
	private String cap;
}
