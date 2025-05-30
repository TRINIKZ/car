package com.example.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class CarController {

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // Página inicial redireciona para a lista de carros
    @GetMapping("/")
    public String index() {
        return "redirect:/carros";
    }

    // Mostrar lista de carros
    @GetMapping("/carros")
    public String showCarros(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        return "carros"; // template: carros.html
    }

    // Formulário de cadastro de carro
    @GetMapping("/cadastro-carro")
    public String showCadastroForm(Model model) {
        model.addAttribute("car", new CarModel());
        return "cadastro-carro"; // template renomeado para cadastro-carro.html
    }

    // Processar cadastro de carro
    @PostMapping("/cadastro-carro")
    public String processCadastro(@ModelAttribute CarModel car, Model model) {
        carRepository.save(car);
        return "redirect:/carros";
    }

    // Formulário para editar carro
    @GetMapping("/editar-carro/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<CarModel> car = carRepository.findById(id);
        if (car.isPresent()) {
            model.addAttribute("car", car.get());
            return "editar-carro"; // template renomeado para editar-carro.html
        } else {
            return "redirect:/carros";
        }
    }

    // Atualizar dados do carro
    @PostMapping("/editar-carro/{id}")
    public String updateCar(@PathVariable Long id, @ModelAttribute CarModel updatedCar) {
        Optional<CarModel> existing = carRepository.findById(id);
        if (existing.isPresent()) {
            CarModel car = existing.get();
            car.setAno(updatedCar.getAno());
            car.setModelo(updatedCar.getModelo());
            car.setPotencia(updatedCar.getPotencia());
            carRepository.save(car);
        }
        return "redirect:/carros";
    }

    // Excluir carro
    @GetMapping("/excluir-carro/{id}")
    public String deleteCar(@PathVariable Long id) {
        carRepository.deleteById(id);
        return "redirect:/carros";
    }
}
