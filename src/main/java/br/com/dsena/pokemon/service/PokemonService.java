package br.com.dsena.pokemon.service;

import br.com.dsena.pokemon.model.GameIndex;
import br.com.dsena.pokemon.model.PokemonRequest;
import br.com.dsena.pokemon.model.PokemonResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PokemonService {

    private final PokemonClient pokemonClient;

    //testar usando assert da exception
    public PokemonResponse getPokemonClient(String name) throws Exception {
       try{
           var pokemon1 = pokemonClient.getPokemon(name);
           if (pokemon1 == null) {
               throw new Exception("O pokémon com o nome '" + name + "' não foi encontrado.");
           }
           return PokemonResponse.builder()
                   .height(pokemon1.getHeight())
                   .name(pokemon1.getName())
                   .weight(pokemon1.getWeight())
                   .build();
       } catch(FeignException feignException){
           throw feignException;
       }
    }

    public String getPokemonWinner(PokemonRequest pokemonRequest) throws Exception {
        try{
            PokemonResponse pokemon1 = pokemonClient.getPokemon(pokemonRequest.getChallenger());
            PokemonResponse pokemon2 = pokemonClient.getPokemon(pokemonRequest.getChallenged());

            Integer pokemonBattle1 = pokemon1.getGame_indices().stream()
                    .map(GameIndex::getGame_index)
                    .reduce(0, Integer::sum);

            Integer pokemonBattle2 = pokemon2.getGame_indices().stream()
                    .map(GameIndex::getGame_index)
                    .reduce(0, Integer::sum);

            if (pokemon1 == null) {
                throw new Exception("O pokémon com o nome '" + pokemonRequest.getChallenger().toString() + "' não foi encontrado.");
            } else if (pokemon2 == null) {
                throw new Exception("O pokémon com o nome '" + pokemonRequest.getChallenged().toString() + "' não foi encontrado.");
            }

            if(pokemonBattle1 > pokemonBattle2) {
                return "Winner: " + pokemon1;
            }if(pokemonBattle2 > pokemonBattle1){
                return "Winner: " + pokemon2;
            }return "DRAW";
        }catch(FeignException feignException){
            throw feignException;
        }
    }
}
