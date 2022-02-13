package qlpk.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import qlpk.entity.BenhAn;
import qlpk.entity.BenhNhan;
import qlpk.entity.DonThuoc;
import qlpk.entity.Thuoc;
import qlpk.modelUtil.DetailThuoc;
import qlpk.service.BenhAnService;
import qlpk.service.DonThuocService;
import qlpk.service.ThuocService;

@Controller
public class KeDonController {

	@Autowired
	private BenhAnService benhAnService;

	@Autowired
	private ThuocService thuocService;

	@Autowired
	private DonThuocService donThuocService;

	public KeDonController(BenhAnService benhAnService, ThuocService thuocService, DonThuocService donThuocService) {
		this.benhAnService = benhAnService;
		this.thuocService = thuocService;
		this.donThuocService = donThuocService;
	}

	@GetMapping("/bacsi/kedon/{id}")
	public String showViewKeDon(@PathVariable int id, Model model) {

		Optional<BenhAn> optBenhAn = benhAnService.getById(id);
		if (optBenhAn.isPresent()) {
			BenhAn benhAn = optBenhAn.get();
			BenhNhan benhNhan = benhAn.getBenhNhan();

			model.addAttribute("benhAn", benhAn);
			model.addAttribute("benhNhan", benhNhan);

			List<Thuoc> dsThuoc = thuocService.getAll();

			model.addAttribute("dsThuoc", dsThuoc);

			return "BacSi/KeDon";
		}
		return "redirect:/404";

	}

	@PostMapping("/bacsi/kedon/{id}")
	public String handleKeDon(@PathVariable int id, @RequestBody(required = false) String json, Model model) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			BenhAn benhAn = benhAnService.getById(id).get();
			String dsDonThuoc = "";
			List<DetailThuoc> listDetailThuoc = mapper.readValue(json, new TypeReference<List<DetailThuoc>>() {
			});
			for (DetailThuoc detailThuoc : listDetailThuoc) {
				DonThuoc donThuoc = new DonThuoc();
				donThuoc.setThuoc(thuocService.getById(detailThuoc.getIdThuoc()));
				donThuoc.setLieuLuong(detailThuoc.getLieuDung() + "");
				donThuoc.setCacDung(detailThuoc.getCachDung());
				donThuoc.setTongTien(
						thuocService.getById(detailThuoc.getIdThuoc()).getGia() * detailThuoc.getLieuDung());
				Integer iddonthuoc = donThuocService.saveThuoc(donThuoc);
				dsDonThuoc = dsDonThuoc + iddonthuoc + " ";
			}
			benhAn.setDsDonThuoc(dsDonThuoc);
			benhAn.setDaPhat(false);
			benhAnService.saveBenhAn(benhAn);
		} catch (Exception e) {
			System.out.println(e);
		}

		return "redirect:/bacsi/list-benhan";
	}

	@GetMapping(value = { "/bacsi/xemdonthuoc/{id}" })
	public String showBSDonThuoc(@PathVariable int id, Model model) {

		Optional<BenhAn> optBenhAn = benhAnService.getById(id);
		if (optBenhAn.isPresent()) {
			BenhAn benhAn = optBenhAn.get();
			BenhNhan benhNhan = benhAn.getBenhNhan();

			model.addAttribute("benhAn", benhAn);
			model.addAttribute("benhNhan", benhNhan);
			List<String> donthuoc = Arrays.asList(benhAn.getDsDonThuoc().split(" "));
			List<DonThuoc> listDonThuoc = new LinkedList<>();
			for (String s : donthuoc) {
				int idDonThuoc = Integer.parseInt(s);
				DonThuoc donThuoc = donThuocService.getByIdDelete(idDonThuoc);
				listDonThuoc.add(donThuoc);
			}
			model.addAttribute("donThuoc", listDonThuoc);

			return "Bacsi/ViewDonThuoc";
		}
		return "redirect:/404";
	}

	@GetMapping(value = { "/yta/xemdonthuoc/{id}" })
	public String showYTaDonThuoc(@PathVariable int id, Model model) {

		Optional<BenhAn> optBenhAn = benhAnService.getById(id);
		if (optBenhAn.isPresent()) {
			BenhAn benhAn = optBenhAn.get();
			BenhNhan benhNhan = benhAn.getBenhNhan();

			model.addAttribute("benhAn", benhAn);
			model.addAttribute("benhNhan", benhNhan);

			List<String> donthuoc = Arrays.asList(benhAn.getDsDonThuoc().split(" "));
			List<DonThuoc> listDonThuoc = new LinkedList<>();
			for (String s : donthuoc) {
				int idDonThuoc = Integer.parseInt(s);
				DonThuoc donThuoc = donThuocService.getByIdDelete(idDonThuoc);

				listDonThuoc.add(donThuoc);

			}
			model.addAttribute("donThuoc", listDonThuoc);

			return "YTa/ViewDonThuoc";
		}
		return "redirect:/404";
	}

	@PostMapping(value = { "/yta/xemdonthuoc/{id}" })
	public String handleYTaDonThuoc(@PathVariable int id, Model model) {

		Optional<BenhAn> optBenhAn = benhAnService.getById(id);
		if (optBenhAn.isPresent()) {

			optBenhAn.get().setDaPhat(true);
			List<String> donthuoc = Arrays.asList(optBenhAn.get().getDsDonThuoc().split(" "));
			for (String s : donthuoc) {
				int idDonThuoc = Integer.parseInt(s);
				Optional<DonThuoc> donThuoc = donThuocService.getById(idDonThuoc);

				if (donThuoc.isPresent()) {
					donThuocService.delete(donThuoc.get());
				}
			}
			optBenhAn.get().setDsDonThuoc(null);
			benhAnService.saveBenhAn(optBenhAn.get());
			return "redirect:/yta/list-benhan";
		}
		return "redirect:/404";
	}

}
