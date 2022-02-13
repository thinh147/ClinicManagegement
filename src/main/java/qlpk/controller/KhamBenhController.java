package qlpk.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import qlpk.entity.BacSy;
import qlpk.entity.Benh;
import qlpk.entity.BenhAn;
import qlpk.entity.BenhNhan;
import qlpk.modelUtil.BenhDanhSachBenh;
import qlpk.security.CustomUserDetails;
import qlpk.service.BacSyService;
import qlpk.service.BenhAnService;
import qlpk.service.BenhService;

@Controller
public class KhamBenhController {

	@Autowired
	private BenhAnService benhAnService;

	@Autowired
	private BacSyService bacSyService;

	@Autowired
	private BenhService benhService;

	public KhamBenhController(BenhAnService benhAnService, BacSyService bacSyService, BenhService benhService) {
		this.benhAnService = benhAnService;
		this.bacSyService = bacSyService;
		this.benhService = benhService;
	}

	@GetMapping("/bacsi/khambenh/{id}")
	public String showViewKhamBenh(@PathVariable int id, Model model, Authentication authentication) {
		// get Benh An
		Optional<BenhAn> optBenhAn = benhAnService.getById(id);
		if (optBenhAn.isPresent()) {
			BenhAn benhAn = optBenhAn.get();
			BenhNhan benhNhan = benhAn.getBenhNhan();
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			BacSy bacSy = bacSyService.getByUsername(userDetails.getUsername());

			List<Benh> dsBenh = benhService.getBenhByBacSy(bacSy.getId());

			// model add benh an va benh nhanh
			model.addAttribute("benhAn", benhAn);
			model.addAttribute("benhNhan", benhNhan);

			Benh benh = new Benh();

			BenhDanhSachBenh benhDanhSachBenh = new BenhDanhSachBenh();
			benhDanhSachBenh.setIdBenh(benh.getId());
			benhDanhSachBenh.setDsBenh(dsBenh);

			model.addAttribute("benhDanhSachBenh", benhDanhSachBenh);
			return "BacSi/KhamBenh";
		}
		return "redirect:/404";

	}

	@PostMapping("/bacsi/khambenh/{id}")
	public String handleKhamBenh(@PathVariable int id, @ModelAttribute("benhAn") BenhAn benhAn,
			@ModelAttribute("benhNhan") BenhNhan benhNhan, Authentication authentication,
			@ModelAttribute("benhDanhSachBenh") BenhDanhSachBenh benhDanhSachBenh) {
		Optional<BenhAn> benhAn1 = benhAnService.getById(id);
		if (benhAn1.isPresent()) {
			benhAn1.get().setIdBenh(benhDanhSachBenh.getIdBenh());
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			BacSy bacSy = bacSyService.getByUsername(userDetails.getUsername());
			benhAn1.get().setBacSy(bacSy);
			benhAnService.updateBenhAn(benhAn1.get());

		}
		return "redirect:/bacsi/list-benhan";
	}
	

	@GetMapping(value = { "/bacsi/xembenhan/{id}" })
	public String showBSBenhAn(@PathVariable int id, Model model) {
		// get Benh An
		// get ds benh cua benh an do
		// check benh nao thuoc bac si hien tai
		// model add benh anh
		// model add ds benh cua benh an do ( tru benh cua bac si hien tai kham)
		// model add ("bs-benh") benh cua bac si hien tai kham ( gen ra nut xac nhan
		// khoi )

		Optional<BenhAn> optBenhAn = benhAnService.getById(id);
		if (optBenhAn.isPresent()) {
			BenhAn benhAn = optBenhAn.get();
			BenhNhan benhNhan = benhAn.getBenhNhan();
			// get All Thuoc
//			List<Thuoc> dsThuoc = thuocService.getAll();

			// model add benh an va benh nhanh
			model.addAttribute("benhAn", benhAn);
			model.addAttribute("benhNhan", benhNhan);

			
			Optional<Benh> benh1 = benhService.getById(benhAn.getIdBenh());
			List<Benh> dsBenh = Arrays.asList(benh1.get());

			// model add benh cua benh an
			model.addAttribute("dsBenh", dsBenh);


			return "Bacsi/ViewBenhAn";
		}
		return "redirect:/404";
	}

	@PostMapping("/bacsi/xembenhan/{id}")
	public String handleUpdateKhoiBenh(@PathVariable int id, @RequestBody(required = false) String json) {
		if (json != null) {
			Optional<BenhAn> benhAn = benhAnService.getById(id);
			benhAn.get().setDaKhoi(true);
			benhAn.get().setDelete(true);
			benhAnService.saveBenhAn(benhAn.get());

		}
		return "redirect:/bacsi/list-benhan";
	}
	
	
	// xem bệnh án
		@GetMapping(value = { "/yta/xembenhan/{id}" })
		public String shoYTBenhAn(@PathVariable int id, Model model) {

			Optional<BenhAn> optBenhAn = benhAnService.getById(id);
			if (optBenhAn.isPresent()) {
				BenhAn benhAn = optBenhAn.get();
				BenhNhan benhNhan = benhAn.getBenhNhan();
				// get All Thuoc
//				List<Thuoc> dsThuoc = thuocService.getAll();

				// model add benh an va benh nhanh
				model.addAttribute("benhAn", benhAn);
				model.addAttribute("benhNhan", benhNhan);


				Optional<Benh> benh1 = benhService.getById(benhAn.getIdBenh());
				List<Benh> dsBenh = Arrays.asList(benh1.get());

				// model add benh cua benh an
				model.addAttribute("dsBenh", dsBenh);

				return "YTa/ViewBenhAn";
			}
			return "redirect:/404";
		}
}
