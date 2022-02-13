package qlpk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import qlpk.dto.UserDTO;
import qlpk.entity.*;
import qlpk.entity.enums.Role;
import qlpk.modelUtil.YtaLuong;
import qlpk.service.*;

import javax.validation.Valid;
import java.util.*;

@Controller
public class YTaController {
	@Autowired
	private YTaService yTaService;
	@Autowired
	private DonThuocService donThuocService;
	@Autowired
	private BenhAnService benhAnService;
	@Autowired
	private UserService userService;
	@Autowired
	private BenhService benhService;

	public YTaController(YTaService yTaService, BenhAnService benhAnService,
						   UserService userService, DonThuocService donThuocService,
						   BenhService benhService) {
		this.yTaService = yTaService;
		this.benhAnService = benhAnService;
		this.userService = userService;
		this.benhService = benhService;
		this.donThuocService = donThuocService;
	}

	@GetMapping("/qlns/yta/ds-yta")
	public String showListYTa(Model model) {
		return "redirect:/qlns/yta/ds-yta/1";
	}
	@GetMapping("/qlns/yta/ds-yta/{page}")
	public String showListYTaPage(@PathVariable("page") long currentPage, Model model){
		long totalPage = yTaService.getTotalPage();
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("currentPage", currentPage);
		List<YTa> dsYTa = yTaService.getByPage(currentPage);
		model.addAttribute("dsYTa", dsYTa);
		return "QuanLyNhanSu/ListNurse";
	}
	@GetMapping("/qlns/yta/add")
	public String showAddFormYTa(Model model) {
		YTa yTa = new YTa();
		UserDTO user = new UserDTO();;

		model.addAttribute("yTa", yTa);
		model.addAttribute("taikhoan", user);
		return "QuanLyNhanSu/AddNurse";

	}

	@PostMapping("/qlns/yta/add")
	public String handleAddYTa(
			@Valid @ModelAttribute("yTa") YTa yTa,
			BindingResult result,
			@ModelAttribute("taikhoan") UserDTO userDTO) {

		if (result.hasErrors()) {
			return "QuanLyNhanSu/AddNurse";
		}
		userDTO.setRole(Role.YTA);
		userService.save(userDTO);
		yTaService.saveYTa(yTa, userDTO);
		return "redirect:/qlns/yta/ds-yta";

	}

	@GetMapping("/qlns/yta/edit/{id}")
	public String showEditFormYTa(@PathVariable int id, Model model) {
		Optional<YTa> optYTa = yTaService.getById(id);
		// get TaiKhoan map voi Bac Sy
//		model.addAttribute("taikhoan",  taiKhoanService.getByUsername(String username).get());
		UserDTO userDTO;

		if (optYTa.isPresent()) {
			userDTO = userService.getUserByID(optYTa.get().getUser().getId());
			model.addAttribute("yTa", optYTa.get());
			model.addAttribute("taikhoan", userDTO);
			return "QuanLyNhanSu/EditNurse";
		}

		// 404
		return "404";
	}

	@PostMapping("/qlns/yta/edit/{id}")
	public String handleEditYTa(@PathVariable int id, @Valid @ModelAttribute("yTa") YTa yTa, BindingResult result,
								@Valid @ModelAttribute("taikhoan") UserDTO userDTO /*, Model model*/, Errors errors) {

		if (errors.hasErrors()) {
//			Optional<YTa> optYTa = yTaService.getById(id);
//			model.addAttribute("yTa", optYTa.get());
//			model.addAttribute("taikhoan", taiKhoan);
			return "QuanLyNhanSu/EditNurse";
		} else {
//			userService.save(userDTO);
			yTaService.updateYTa(yTa);
			return "redirect:/qlns/yta/ds-yta";
		}

	}

	@GetMapping("/qlns/yta/delete/{id}")
	public RedirectView handleDeleteBacSi(@PathVariable int id, Model model) {
		Optional<YTa> optYTa = yTaService.getById(id);

		RedirectView redirectView = new RedirectView();

		if (optYTa.isPresent()) {
			redirectView.setUrl("/qlns/yta/ds-yta");
			yTaService.deleteYTa(id);
			return redirectView;
		}

		// 404
		redirectView.setUrl("/404");
		return redirectView;
	}
}
