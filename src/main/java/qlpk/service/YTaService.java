package qlpk.service;

import qlpk.dto.UserDTO;
import qlpk.entity.BacSy;
import qlpk.entity.YTa;

import java.util.List;
import java.util.Optional;

public interface YTaService {
    List<YTa> findAll();
    void deleteYTa(int id);
    boolean saveYTa(YTa yTa, UserDTO userDTO);
    boolean updateYTa(YTa yTa);
    Optional<YTa> getById(int id);
    YTa getByUsername(String username);
    long getTotalPage();
    List<YTa> getByPage(long currentPage);
}
