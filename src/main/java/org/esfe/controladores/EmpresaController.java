package org.esfe.controladores;

import org.esfe.modelos.Empresa;
import org.esfe.servicios.interfaces.IEmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {
    @Autowired
    private IEmpresaService empresaService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1) - 1; // Si no está seteado, se asigna 0
        int pageSize = size.orElse(5); // Tamaño de la página, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Empresa> empresas = empresaService.buscarTodosPaginados(pageable);
        model.addAttribute("empresas", empresas);

        int totalPages = empresas.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "empresa/index";
    }

    @GetMapping("/create")
    public String create (Empresa empresa){
        return "empresa/create";
    }

    @PostMapping("/save")
    public String save(Empresa empresa, BindingResult result, Model model, RedirectAttributes attributes){
        if (result.hasErrors()){
            model.addAttribute(empresa);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "empresa/create";
        }

        empresaService.crearOEditar(empresa);
        attributes.addFlashAttribute("msg", "Empresa creada con exito.");
        return "redirect:/empresas";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        Empresa empresa = empresaService.buscarPorId(id).get();
        model.addAttribute("empresa", empresa);
        return "empresa/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        Empresa empresa = empresaService.buscarPorId(id).get();
        model.addAttribute("empresa", empresa);
        return "empresa/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        Empresa empresa = empresaService.buscarPorId(id).get();
        model.addAttribute("empresa", empresa);
        return "empresa/delete";
    }

    @PostMapping("/delete")
    public String delete(Empresa empresa, RedirectAttributes attributes){
        empresaService.eliminarPorId(empresa.getId());
        attributes.addFlashAttribute("msg", "Empresa eliminada correctamente");
        return "redirect:/empresas";
    }
}
