package qlpk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import qlpk.dto.UserDTO;
import qlpk.entity.*;
import qlpk.entity.enums.Role;

import qlpk.service.*;

import javax.validation.Valid;
import java.util.*;

@Controller
public class BacSyController {

	@Autowired
	private BacSyService bacSyService;
	@Autowired
	private UserService userService;

	public BacSyController(BacSyService bacSyService, UserService userService) {
		this.bacSyService = bacSyService;
		this.userService = userService;

	}

	@GetMapping("/qlns/bacsi/ds-bacsi")
	public String showListBacSi(Model model) {
		return "redirect:/qlns/bacsi/ds-bacsi/1";
	}

	@GetMapping("/qlns/bacsi/ds-bacsi/{page}")
	public String showListBacSiPage(@PathVariable("page") long currentPage, Model model) {
		long totalPage = bacSyService.getTotalPage();

		model.addAttribute("totalPage", totalPage);
		model.addAttribute("currentPage", currentPage);
		List<BacSy> dsBacSi = bacSyService.getByPage(currentPage);
		model.addAttribute("dsBacSi", dsBacSi);

		return "QuanLyNhanSu/ListDoctor";
	}

	@GetMapping("/qlns/bacsi/add")
	public String showAddFormBacSi(Model model) {
		BacSy bacSi = new BacSy();
		UserDTO user = new UserDTO();
		model.addAttribute("bacsi", bacSi);
		model.addAttribute("taikhoan", user);
		return "QuanLyNhanSu/AddDoctor";

	}

	@PostMapping("/qlns/bacsi/add")
	public String handleAddBacSi(@Valid @ModelAttribute("bacsi") BacSy bacsi, BindingResult result,
			@ModelAttribute("taikhoan") UserDTO userDTO) {

		if (result.hasErrors()) {
			return "QuanLyNhanSu/AddDoctor";
		}

		userDTO.setRole(Role.BACSY);
		userService.save(userDTO);
		bacSyService.saveBacSy(bacsi, userDTO);

		return "redirect:/qlns/bacsi/ds-bacsi";
	}

	@GetMapping("/qlns/bacsi/edit/{id}")
	public String showEditFormBacSi(@PathVariable int id, Model model) {
		Optional<BacSy> optBacSi = bacSyService.getById(id);
		UserDTO userDTO;

		if (optBacSi.isPresent()) {
			userDTO = userService.getUserByID(optBacSi.get().getUser().getId());
			model.addAttribute("bacsi", optBacSi.get());
			model.addAttribute("taikhoan", userDTO);

			return "QuanLyNhanSu/EditDoctor";
		}

		// 404
		return "404";
	}

	@PostMapping("/qlns/bacsi/edit/{id}")
	public String handleEditBacSi(@Valid @ModelAttribute("bacsi") BacSy bacsi, BindingResult result,
			@ModelAttribute("taikhoan") UserDTO userDTO) {
		if (result.hasErrors()) {
			return "QuanLyNhanSu/EditDoctor";
		}

		bacSyService.saveBacSy(bacsi, userDTO);
		return "redirect:/qlns/bacsi/ds-bacsi/1";
	}

	@GetMapping("/qlns/bacsi/delete/{id}")
	public RedirectView handleDeleteBacSi(@PathVariable int id, Model model) {
		Optional<BacSy> optBacSi = bacSyService.getById(id);

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/qlns/bacsi/ds-bacsi/1");

		if (optBacSi.isPresent()) {
			bacSyService.deleteBacSy(id);
			User user = optBacSi.get().getUser();
			userService.delete(user);
			return redirectView;
		}

		redirectView.setUrl("/404");
		return redirectView;
	}

}
