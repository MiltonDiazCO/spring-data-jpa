package com.milton.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.milton.springboot.app.models.dao.IClienteDao;
import com.milton.springboot.app.models.entity.Cliente;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	
	private final String usuariosTitulo = "Clientes";
	
	@Autowired
	private IClienteDao clienteDao;
	
	@GetMapping({"", "/", "/listar"})
	public String listar(Model model) {
		model.addAttribute("titulo", usuariosTitulo);
		model.addAttribute("clientes", clienteDao.findAll());
		return "clientes/listar";
	}
	
	@GetMapping({"/form"})
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		
		model.put("titulo", "Agregar nuevo cliente");
		model.put("cliente", cliente);
		return "clientes/form";
	}
	
	@PostMapping({"/form"})
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Agregar nuevo cliente");
			return "clientes/form";
		}
		
		clienteDao.save(cliente);
		return "redirect:/clientes/listar";
	}

}
