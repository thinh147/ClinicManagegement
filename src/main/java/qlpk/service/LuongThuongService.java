package qlpk.service;

import qlpk.modelUtil.BacSyLuong;
import qlpk.modelUtil.YtaLuong;

import java.util.Date;
import java.util.List;

public interface LuongThuongService {

    // for tinh luong

    List<BacSyLuong> tinhLuongBacSy(Date sdate, Date edate);
    List<YtaLuong> tinhLuongYta(Date sdate, Date edate);
}
