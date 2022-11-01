package com.milton.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.milton.springboot.app.models.dao.IClienteDao;

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

}
