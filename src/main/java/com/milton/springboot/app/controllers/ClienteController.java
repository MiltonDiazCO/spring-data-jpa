package com.milton.springboot.app.controllers;

import java.io.IOException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.milton.springboot.app.models.entity.Cliente;
import com.milton.springboot.app.models.service.IClienteService;
import com.milton.springboot.app.models.service.IUploadFileService;
import com.milton.springboot.app.util.paginator.PageRender;

@Controller
@RequestMapping("/clientes")
@SessionAttributes("cliente")
public class ClienteController {

	private final String usuariosTitulo = "Clientes";

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;

	@GetMapping({ "", "/", "/listar" })
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 5);

		Page<Cliente> clientes = clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<Cliente>("/clientes/", clientes);

		model.addAttribute("titulo", usuariosTitulo);
		model.addAttribute("clientes", clientes);
		model.addAttribute("pageRender", pageRender);
		return "clientes/listar";
	}

	@GetMapping({ "/form" })
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();

		model.put("titulo", "Agregar nuevo cliente");
		model.put("cliente", cliente);
		return "clientes/form";
	}

	@PostMapping({ "/form" })
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash,
			@RequestParam("fotoPost") MultipartFile foto, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Agregar nuevo cliente");
			return "clientes/form";
		}

		if (!foto.isEmpty()) {

			if (cliente.getId() != null && cliente.getFoto() != null && cliente.getFoto().length() > 0) {
				uploadFileService.delete(cliente.getFoto());
			}

			try {
				String uniqueFileName = uploadFileService.copy(foto);
				cliente.setFoto(uniqueFileName);
			} catch (IOException e) {
				flash.addFlashAttribute("error", "No se pudo cargar la imagen");
				e.printStackTrace();
			}

		}

		String flashMesssage;

		if (cliente.getId() != null) {
			flashMesssage = "Se modifco al cliente correctamente!";
		} else {
			flashMesssage = "Cliente registrado con exito!";
		}

		flash.addFlashAttribute("success", flashMesssage);

		clienteService.save(cliente);
		status.setComplete();
		return "redirect:/clientes/listar";
	}

	@GetMapping({ "/ver/{id}" })
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = clienteService.findOne(id);

		if (cliente == null) {
			flash.addFlashAttribute("error", "Cliente desconocido");
			return "redirect:/clientes/listar";
		}

		model.put("titulo", "Detalle cliente");
		model.put("cliente", cliente);
		return "clientes/ver";
	}

	@GetMapping({ "/form/{id}" })
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);

			if (cliente == null) {
				flash.addFlashAttribute("error", "Cliente desconocido");
				return "redirect:/clientes/listar";
			}
		} else {
			flash.addFlashAttribute("error", "Cliente desconocido");
			return "redirect:/clientes/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");
		return "clientes/form";
	}

	@RequestMapping({ "/eliminar/{id}" })
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);

			uploadFileService.delete(cliente.getFoto());
			clienteService.delete(id);
		}

		flash.addFlashAttribute("success", "Se ha eliminado al cliente");
		return "redirect:/clientes/listar";
	}

}
