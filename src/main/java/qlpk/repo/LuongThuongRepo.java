package qlpk.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import qlpk.entity.LuongThuong;

import java.util.Date;
import java.util.List;

@Repository
public interface LuongThuongRepo extends JpaRepository<LuongThuong, Integer> {

    // tinh luong
    @Query(value = "CALL get_salary_doctor(:did, :sdate, :edate);", nativeQuery = true)
    public List<Float> tinhLuongBacSy(@RequestParam("did") int did, @RequestParam("sdate") Date sdate,
                                      @RequestParam("edate") Date edate);
    @Query(value = "CALL get_salary_nurse(:nid, :sdate, :edate);", nativeQuery = true)
    public List<Float> tinhLuongYta(@RequestParam("nid") int nid, @RequestParam("sdate") Date sdate, @RequestParam("edate") Date edate);

}
