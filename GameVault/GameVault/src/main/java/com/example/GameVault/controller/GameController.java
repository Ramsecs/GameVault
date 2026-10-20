package com.example.GameVault.controller;


import com.example.GameVault.model.Juego;
import com.example.GameVault.model.Usuario;
import com.example.GameVault.service.JuegoService;
import com.example.GameVault.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {

    @Autowired
    JuegoService juegoService;

    @Autowired
    private UsuarioService usuarioService;


    private static List<Juego> juegosDb = new ArrayList<>();
    private static long idCounter = 1;
    public static final String UPLOAD_DIR = "src/main/resources/static/uploads";

    @GetMapping("/fragments-demo")
    public String fragments(){
        return "fragments-demo";
    }

    @GetMapping("/catalogo")
    public String catalogo(){
        return "catalogo";
    }

    @GetMapping({"/","/juegos"})
    public String juegos(Model model){
        model.addAttribute("juegos", juegoService.listarTodos());
        return "juegos";
    }

    @GetMapping({"/mis-juegos"})
    public String misJuegos(Model model){
        model.addAttribute("usuario", this.usuarioService.obtenerUsuarioLogueado());
        model.addAttribute("juegos", juegoService.listarTodos());
        return "mis-juegos";
    }

    @GetMapping("/juegos/nuevo")
    public String formulario(Model model){
        model.addAttribute("juego", new Juego());
        return "formulario";
    }



    @PostMapping("/juegos")
    public String guardarJuego(@RequestParam("titulo") String titulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("precio") double precio,
                               @RequestParam("categoria") String categoria,
                               @RequestParam("portada") MultipartFile portada) {

        Usuario usuario = this.usuarioService.obtenerUsuarioLogueado();

        if (usuario == null){
            return "redirect:/login";
        }


        Juego juego = new Juego();
        juego.setTitulo(titulo);
        juego.setPrecio(precio);
        juego.setCategoria(categoria);
        juego.setDescripcion(descripcion);

        juego.setUsuario(usuario);
        juegoService.guardarJuego(juego, portada);


        // Redirigimos a la lista de juegos (Patrón PRG - Post/Redirect/Get)
        return "redirect:/juegos";
    }


    @PostMapping("/juegos/eliminar/{id}")
    public String eliminarJuego(@PathVariable Long id){
        juegoService.eliminareJuego(id);

        return "redirect:/juegos";
    }

    @GetMapping("/juegos/editar/{id}")
    public String editarJuego(@PathVariable Long id, Model model){
        Juego juego = juegoService.buscarPorId(id);
        model.addAttribute("juego", juego);

        return "formulario";
    }

    @PostMapping("/juegos/editar/{id}")
    public String editarJuego(
            @PathVariable Long id,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") double precio,
            @RequestParam("categoria") String categoria,
            @RequestParam("portada") MultipartFile portada) {


        Juego juego = this.juegoService.buscarPorId(id);
        juego.setTitulo(titulo);
        juego.setPrecio(precio);
        juego.setCategoria(categoria);
        juego.setDescripcion(descripcion);

        juegoService.guardarJuego(juego, portada);


        // Redirigimos a la lista de juegos (Patrón PRG - Post/Redirect/Get)
        return "redirect:/juegos";
    }

}
