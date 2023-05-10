package br.com.dsena.pokemon.service;

import br.com.dsena.pokemon.model.PokemonResponse;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pokeapi", url = "https://pokeapi.co/api/v2/")
public interface PokemonClient {
    @GetMapping("/pokemon/{name}")
    PokemonResponse getPokemon(@PathVariable("name") String name);
}
