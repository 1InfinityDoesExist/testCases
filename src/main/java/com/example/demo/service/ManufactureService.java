package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.MyException;
import com.example.demo.beans.ManufacturingUnit;
import com.example.demo.repository.ManufactureRepository;

@Service
public class ManufactureService {

	private static final Logger logger = LoggerFactory.getLogger(ManufactureService.class);

	@Autowired
	private ManufactureRepository manufactureRepository;

	public ManufacturingUnit saveManufactureData(ManufacturingUnit manuUnit) {
		ManufacturingUnit manufacturingUtil = null;
		try {
			manufacturingUtil = manufactureRepository.save(manuUnit);
			if (manufacturingUtil == null) {
				throw new MyException("Sorry Could Not Persiste Data In The Database");
			}
		} catch (final MyException ex) {
			logger.info("Exception:- " + ex.getMessage());
		}
		return manufacturingUtil;
	}

	public ManufacturingUnit getManufacturingUnitById(Long id) {
		ManufacturingUnit manufacturingUnit = null;
		try {
			manufacturingUnit = manufactureRepository.getManuUnitById(id);
			if (manufacturingUnit == null) {
				throw new MyException("Sorry Could Not Retrieve Data From The DB");
			}
		} catch (final MyException ex) {
			logger.info("Exception:- " + ex.getMessage());
		}
		return manufacturingUnit;
	}

	public List<ManufacturingUnit> getAllManuUnit() {
		List<ManufacturingUnit> listOfmanufactuingUnit = null;

		try {
			listOfmanufactuingUnit = manufactureRepository.findAll();
			if (listOfmanufactuingUnit == null || listOfmanufactuingUnit.size() == 0) {
				throw new MyException("Sorry Nothing Found In The DB For Manufacturing Unit");
			}
		} catch (final MyException ex) {
			logger.info("Exceptioin :- " + ex.getMessage());
		}
		return listOfmanufactuingUnit;
	}

	public String deleteManufaturingUnit(Long id) {
		String response = null;
		try {
			manufactureRepository.deleteById(id);
			response = "SuccessFully Deleted";
		} catch (final MyException ex) {
			logger.info("Exception :- " + ex.getMessage());
		}
		return response;
	}

}
