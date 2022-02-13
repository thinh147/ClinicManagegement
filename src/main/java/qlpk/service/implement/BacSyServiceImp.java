package qlpk.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qlpk.dto.UserDTO;
import qlpk.entity.BacSy;
import qlpk.entity.Benh;
import qlpk.entity.BenhAn;
import qlpk.modelUtil.BacSyLuong;
import qlpk.repo.BacSyRepo;
import qlpk.repo.BenhAnRepo;
import qlpk.repo.BenhRepo;
import qlpk.repo.UserRepo;
import qlpk.service.BacSyService;
import qlpk.service.BenhAnService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BacSyServiceImp implements BacSyService {
	@Autowired
	private BacSyRepo bacSyRepo;
	@Autowired
	private BenhRepo benhRepo;
	@Autowired
	private UserRepo userRepo;
	
	final long pageSize = 5;

	@Override
	public void deleteBacSy(int id) {
		BacSy bacSy = bacSyRepo.getById(id);
		bacSy.setDelete(true);
		bacSyRepo.save(bacSy);
	}

	@Override
	public boolean saveBacSy(BacSy bacSy, UserDTO userDTO) {
		bacSy.setUser(userRepo.findByUserName(userDTO.getUserName(), false));
		bacSyRepo.save(bacSy);
		return true;
	}

	@Override
	public boolean updateBacSy(BacSy bacSy) {
		bacSyRepo.save(bacSy);
		return true;
	}

	
	@Override
	public List<BacSy> getAll() {
		return bacSyRepo.findBacSyByIsDelete(false);
	}

	@Override
	public Optional<BacSy> getById(int id) {
		return bacSyRepo.findBacSyByIdAndIsDelete(id, false);
	}

	@Override
	public List<Benh> getListBenhByBacSy(int id) {
		return benhRepo.findBenhByBacSy(bacSyRepo.findById(id));
	}

	@Override
	public BacSy getByUsername(String username) {
		return bacSyRepo.findBacSyByUserAndIsDelete(userRepo.findByUserName(username, false), false);
	}

	@Override
	public long getTotalPage() {
		return (bacSyRepo.countByIsDelete().get(0) % pageSize == 0) ? bacSyRepo.countByIsDelete().get(0) / pageSize
				: (bacSyRepo.countByIsDelete().get(0) / pageSize) + 1;
	}

	@Override
	public List<BacSy> getByPage(long currentPage) {
		return bacSyRepo.findByPage((currentPage - 1) * pageSize, currentPage * pageSize);
	}
	
	
	
	// for tinh luong
	

}
