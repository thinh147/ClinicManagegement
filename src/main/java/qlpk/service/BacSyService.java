package qlpk.service;

import qlpk.dto.UserDTO;
import qlpk.entity.BacSy;
import qlpk.entity.Benh;

import java.util.List;
import java.util.Optional;

public interface BacSyService {
    void deleteBacSy(int id);
    
    boolean saveBacSy(BacSy bacSy, UserDTO userDTO);

    boolean updateBacSy(BacSy bacSy);
    
    List<BacSy> getAll();
    
    Optional<BacSy> getById(int id);

    List<Benh> getListBenhByBacSy(int id);
    
    BacSy getByUsername(String username);
    
    long getTotalPage();
    
    List<BacSy> getByPage(long currentPage);

}
