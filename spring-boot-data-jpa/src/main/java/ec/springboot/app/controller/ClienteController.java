package ec.springboot.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ec.springboot.app.models.entity.Cliente;
import ec.springboot.app.models.services.IclienteService;
import ec.springboot.app.util.render.PageRender;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("clientes")
public class ClienteController {

	@Autowired
	private IclienteService clienteService;

	/*
	 * Implementando un paginador para dar orden a las pages 
	 * con JpaRepository 
	 * se anade esto @RequestParam(name = "page", defaultValue = "0")
	 */
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0")
							int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 5);
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
														//Se pasa URL
		PageRender<Cliente> pageRender = new PageRender<>("/listar",clientes);
		model.addAttribute("titulo", "Lista de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page",pageRender);

		return "listar";
	}

	@RequestMapping("/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Lista de clientes");
		return "form";
	}

	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El cliente no existe en la BBDD");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El cliente debe tener un Id valido");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");
		return "form";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("error", "Cliente eliminado correctamente");
		}
		return "redirect:/listar";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Lista de clientes");
			return "form";
		}
		
		String mensajeFlash = (cliente.getId() != null) ? "Cliente editado exitosamente" : "Cliente creado exitosamente";
		
		clienteService.save(cliente);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	/*
	 * Para no utilizar el hidden de id podriamos utilizar anotaciones
	 * como @SESSIONATRIBUTE(NOMRE OBJETOCLIENTE) y en el metodo guardar cuando
	 * termine de invocar metodos como editar o listar del metodo cliente y ponemos
	 * sessionstatus status en parametros y luego aniadimos una linea de codigo
	 * llamado sessioncomplete
	 */
}
