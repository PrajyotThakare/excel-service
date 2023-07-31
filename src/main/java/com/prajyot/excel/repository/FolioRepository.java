package com.prajyot.excel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prajyot.excel.entites.FolioEntity;

import jakarta.transaction.Transactional;

@Repository
public interface FolioRepository extends JpaRepository<FolioEntity, String> {
	
	@Modifying
	@Transactional
	@Query("UPDATE FolioEntity f SET f.stock = ?1, f.qty = ?2, f.avgAmt = ?3 , f.totalAmt = ?4, f.weightage = ?5, f.sector = ?6, f.cap = ?7 WHERE f.stock = ?1 ")
	Integer update(String stock, Integer qty, Float avg, Float amt, Float weightage, String sector, String cap);

}
