package qlpk.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qlpk.dto.UserDTO;

import qlpk.entity.YTa;

import qlpk.repo.UserRepo;
import qlpk.repo.YTaRepo;
import qlpk.service.YTaService;

import java.util.List;
import java.util.Optional;

@Service
public class YTaServiceImp implements YTaService {
    @Autowired
    private YTaRepo yTaRepo;
    @Autowired
    private UserRepo userRepo;
    final long pageSize = 5;

    public YTaServiceImp(YTaRepo yTaRepo, UserRepo userRepo) {
        this.yTaRepo = yTaRepo;
        this.userRepo = userRepo;
    }


    @Override
    public List<YTa> findAll() {
        return yTaRepo.findYTaByIsDelete(false);
    }

    @Override
    public void deleteYTa(int id) {
        YTa yTa = yTaRepo.getById(id);
        yTa.setDelete(true);
        updateYTa(yTa);
    }

    @Override
    public boolean saveYTa(YTa yTa, UserDTO userDTO) {
        yTa.setUser(userRepo.findByUserName(userDTO.getUserName(), false));
        yTaRepo.save(yTa);
        return true;
    }

    @Override
    public boolean updateYTa(YTa yTa) {
        yTaRepo.save(yTa);
        return true;
    }

    @Override
    public Optional<YTa> getById(int id) {
        return yTaRepo.findYTaByIdAndIsDelete(id, false);
    }

    @Override
    public YTa getByUsername(String username) {
        return yTaRepo.findYTaByUserAndIsDelete(userRepo.findByUserName(username, false), false);
        //return yTaRepo.findYTaByUser(userRepo.findByUserName(username));
    }

    @Override
    public long getTotalPage() {
        return (yTaRepo.countByIsDelete().get(0) % pageSize == 0) ? yTaRepo.countByIsDelete().get(0) / pageSize
                : (yTaRepo.countByIsDelete().get(0) / pageSize) + 1;
    }

    @Override
    public List<YTa> getByPage(long currentPage) {
        return yTaRepo.findByPage((currentPage - 1) * pageSize, currentPage * pageSize);
    }
}
