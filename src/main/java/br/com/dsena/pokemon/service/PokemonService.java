package br.com.dsena.pokemon.service;

import br.com.dsena.pokemon.model.PokemonRequest;
import br.com.dsena.pokemon.model.PokemonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PokemonService {

    private final PokemonClient pokemonClient;

    public PokemonResponse getPokemonClient(String name) {
         var pokemon1 = pokemonClient.getPokemon(name);

        return PokemonResponse.builder()
                .height(pokemon1.getHeight())
                .name(pokemon1.getName())
                .weight(pokemon1.getWeight())
                .build();
    }

    public String getPokemonWinner(PokemonRequest pokemonRequest) {
        PokemonResponse pokemon1 = pokemonClient.getPokemon(pokemonRequest.getChallenger());
        PokemonResponse pokemon2 = pokemonClient.getPokemon(pokemonRequest.getChallenged());

        if(pokemon1.getWeight() > pokemon2.getWeight() && pokemon1.getHeight() > pokemon2.getHeight()) {
            return "Winner: " + pokemon1;
        }if(pokemon1.getWeight() < pokemon2.getWeight() && pokemon1.getHeight() < pokemon2.getHeight()){
            return "Winner: " + pokemon2;
        }return "DRAW";
    }
}
