package br.com.dsena.pokemon.controller;

import br.com.dsena.pokemon.model.PokemonRequest;
import br.com.dsena.pokemon.model.PokemonResponse;
import br.com.dsena.pokemon.service.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pokemons")
public class PokemonController {
    @Autowired
    private PokemonService pokemonService;

    @GetMapping("/pokemon/{name}")
    public ResponseEntity<PokemonResponse> getPokemon(@PathVariable String name) throws Exception {
        return ResponseEntity.ok(pokemonService.getPokemonClient(name));
    }

    @PostMapping
    public ResponseEntity<String> getPokemonWinner(@RequestBody PokemonRequest pokemonRequest) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pokemonService.getPokemonWinner(pokemonRequest));
    }
}