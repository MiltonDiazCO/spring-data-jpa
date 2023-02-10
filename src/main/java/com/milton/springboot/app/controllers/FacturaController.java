package com.milton.springboot.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.milton.springboot.app.models.entity.Cliente;
import com.milton.springboot.app.models.service.IClienteService;

@Controller
@RequestMapping("/facturas")
public class FacturaController {
	
	@Autowired
	public IClienteService clienteService;
	
	@GetMapping("/list/{id}")
	public String facturasByCliente(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(id);
		
		String titulo = "Facturas del cliente ".concat(cliente.getNombre().concat(" ").concat(cliente.getApellido()));
		
		if(!cliente.getFacturas().isEmpty()) {
			titulo = "Facturas del cliente ".concat(cliente.getNombre().concat(" ").concat(cliente.getApellido()));
		} else {
			titulo = "El cliente ".concat(cliente.getNombre().concat(" ").concat(cliente.getApellido().concat(", no cuenta con facturas registradas")));
		}
		
		model.put("cliente", cliente);
		model.put("titulo", titulo);
		
		return "facturas/listar";
	}

}
