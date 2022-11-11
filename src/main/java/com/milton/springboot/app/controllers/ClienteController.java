package com.milton.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.milton.springboot.app.models.dao.IClienteDao;
import com.milton.springboot.app.models.entity.Cliente;

@Controller
@RequestMapping("/clientes")
@SessionAttributes("cliente")
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
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Agregar nuevo cliente");
			return "clientes/form";
		}
		
		clienteDao.save(cliente);
		status.setComplete();
		return "redirect:/clientes/listar";
	}
	
	@GetMapping({"/form/{id}"})
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model) {
		
		Cliente cliente = null;
		
		if(id > 0) {
			cliente = clienteDao.findOne(id);
		} else {
			return "redirect:/clientes/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");
		return "clientes/form";
	}
	
	@RequestMapping({"/eliminar/{id}"})
	public String eliminar(@PathVariable(value="id") Long id) {
		
		if(id > 0) {
			clienteDao.delete(id);
		}
		return "redirect:/clientes/listar";
	}

}
