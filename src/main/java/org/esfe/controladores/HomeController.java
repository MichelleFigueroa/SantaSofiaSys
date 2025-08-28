package org.esfe.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String index(){
        return "home/index";
    }

    @GetMapping("/login")
    public String mostrarlogin(){
        // Spring Security maneja el parámetro 'error' directamente en la URL,
        // no necesitas inyectar Model aquí si solo usas th:if="${param.error}"
        return  "home/fromLogin";
    }

    // Si tienes un home page para después del login exitoso
    @GetMapping("/home")
    public String homePage() {
        return "home/homePage";
    }
}