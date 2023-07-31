package com.prajyot.excel.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prajyot.excel.constants.ExcelConstant;
import com.prajyot.excel.entites.FolioEntity;
import com.prajyot.excel.repository.FolioRepository;

@RestController
@RequestMapping("/excel")
public class ExcelController {

	@Autowired
	private FolioRepository repository; 
	
	@GetMapping("/read")
	public void readExcel() throws IOException {

		FileInputStream file = new FileInputStream(new File(ExcelConstant.NAME));
		Workbook workBook = new XSSFWorkbook(file);
		DataFormatter dataFormatter = new DataFormatter();
		Iterator<Sheet> sheets = workBook.sheetIterator();
		FormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();
		
		FolioEntity entity = new FolioEntity();
		
		while (sheets.hasNext()) {
			Sheet sh = sheets.next();
			System.out.println("Sheet name is : " + sh.getSheetName());
			System.out.println(
					"-------------------------------------------------------------------------------------------------------------");
			Iterator<Row> iterator = sh.iterator();
			Integer columnCounter = 0;
			while (iterator.hasNext()) {
				Row row = iterator.next();
				Iterator<Cell> cellIterator = row.iterator();
				
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					String cellValue = dataFormatter.formatCellValue(cell);

					if (cell.getCellType() == CellType.NUMERIC && columnCounter == 1) {
						entity.setQty(Integer.parseInt(cellValue));
					} else if (cell.getCellType() == CellType.STRING) {
						switch (columnCounter) {
						case 0:
							entity.setStock(cellValue);
							break;
						case 5:
							entity.setSector(cellValue);
							break;
						case 6:
							entity.setCap(cellValue);
							break;
						default:
							break;
						}

					} else if (cell.getCellType() == CellType.FORMULA || cell.getCellType() == CellType.NUMERIC) {

						switch (evaluator.evaluateFormulaCell(cell)) {
						
						case BOOLEAN:
							System.out.println(cell.getBooleanCellValue());
							break;
						case NUMERIC:
						case _NONE:
							cellValue = String.format("%.2f", cell.getNumericCellValue());
							if(columnCounter == 2) {
								entity.setAvgAmt(Float.parseFloat(cellValue));
							} else if(columnCounter == 3) {
								entity.setTotalAmt(Float.parseFloat(cellValue));
							} if(columnCounter == 4) {
								entity.setWeightage(Float.parseFloat(cellValue));
							}
							break;
						case STRING:
							System.out.println(cell.getStringCellValue());
							break;
						}

					}
					System.out.print(cellValue + "\t");
					columnCounter++;
				}
				boolean isExisting = repository.existsById(entity.getStock());
				System.out.println(" stock exists "+isExisting);
				if(isExisting) {
					System.out.println(repository.update(entity.getStock(), entity.getQty(), entity.getAvgAmt(), entity.getTotalAmt(), entity.getWeightage(), entity.getSector() , entity.getCap()));
				} else
					repository.save(entity);
				
				System.out.println();
				columnCounter = 0;
			}	
		}
		workBook.close();
	}

	@GetMapping("/write")
	public void read() {
		System.out.println("Working");
	}

}
