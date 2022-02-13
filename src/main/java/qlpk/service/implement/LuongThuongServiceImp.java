package qlpk.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qlpk.entity.BacSy;
import qlpk.entity.YTa;
import qlpk.modelUtil.BacSyLuong;
import qlpk.modelUtil.YtaLuong;
import qlpk.repo.BacSyRepo;
import qlpk.repo.LuongThuongRepo;
import qlpk.repo.YTaRepo;
import qlpk.service.LuongThuongService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LuongThuongServiceImp implements LuongThuongService {
    @Autowired
    private BacSyRepo bacSyRepo;
    @Autowired
    private YTaRepo yTaRepo;
    @Autowired
    private LuongThuongRepo luongThuongRepo;
    @Override
    public List<BacSyLuong> tinhLuongBacSy(Date sdate, Date edate) {
        List<BacSy> listBacSi = bacSyRepo.findBacSyByIsDelete(false);;
        List<BacSyLuong> listBacSiLuong = new ArrayList<>();
        for (BacSy bacSy : listBacSi) {
            BacSyLuong bacSyLuong = new BacSyLuong();
            bacSyLuong.setBacSy(bacSy);
            bacSyLuong.setLuong(luongThuongRepo.tinhLuongBacSy(bacSy.getId(), sdate, edate).get(0));
            listBacSiLuong.add(bacSyLuong);
        }
        return listBacSiLuong;
    }
    @Override
    public List<YtaLuong> tinhLuongYta(Date sdate, Date edate) {
        List<YTa> listYta = yTaRepo.findYTaByIsDelete(false);
        List<YtaLuong> listYtaLuong = new ArrayList<>();
        for(YTa yta : listYta){
            YtaLuong ytaLuong = new YtaLuong();
            ytaLuong.setYta(yta);
            ytaLuong.setLuong(luongThuongRepo.tinhLuongYta(yta.getId(), sdate, edate).get(0));
            listYtaLuong.add(ytaLuong);
        }
        return listYtaLuong;
    }
}
