package br.com.dsena.pokemon.service;

import br.com.dsena.pokemon.model.GameIndex;
import br.com.dsena.pokemon.model.PokemonRequest;
import br.com.dsena.pokemon.model.PokemonResponse;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PokemonServiceTest {

    @InjectMocks
    private PokemonService service;
    @Mock
    private PokemonClient pokemonClient;
    private PokemonResponse pokemonResponse;
    private PokemonRequest pokemonRequest;


    @BeforeEach
    public void setup() throws Exception {
        this.pokemonResponse = PokemonResponse.builder()
                .height(7L)
                .weight(1L)
                .name("Pikachu")
                .build();

        this.pokemonRequest = PokemonRequest.builder()
                .challenger("Pikachu")
                .challenged("Bulbassaur")
                .build();
    }

    @Test
    public void testGetPokemonClient() throws Exception {
        when(pokemonClient.getPokemon("Pikachu")).thenReturn(pokemonResponse);
        when(pokemonClient.getPokemon("Bulbassaur")).thenReturn(pokemonResponse);
        PokemonResponse responseService = service.getPokemonClient("Pikachu");

        assertEquals(pokemonResponse.getHeight(), responseService.getHeight());
        assertEquals(pokemonResponse.getName(), responseService.getName());
        assertEquals(pokemonResponse.getWeight(), responseService.getWeight());
    }

    @Test
    public void testGetPokemonClientNull() throws Exception {
        when(pokemonClient.getPokemon(anyString())).thenReturn(null);
        Exception exception = Assertions.assertThrows(
                Exception.class, () -> service.getPokemonClient("Pikac"));
        Assertions.assertSame(Exception.class, exception.getClass());
    }

    @Test
    public void testGetPokemonWinner_Success() throws Exception {
        String challenger = "Pikachu";
        String challenged = "Charmander";
        PokemonResponse pokemon1 = new PokemonResponse(challenger, 10L, 5L, List.of(GameIndex.builder()
                        .game_index(7)
                .build()));
        PokemonResponse pokemon2 = new PokemonResponse(challenged, 8L, 4L, List.of(GameIndex.builder()
                        .game_index(2)
                .build()));
        when(pokemonClient.getPokemon(challenger)).thenReturn(pokemon1);
        when(pokemonClient.getPokemon(challenged)).thenReturn(pokemon2);

        String winner = service.getPokemonWinner(new PokemonRequest(challenger, challenged));

        assertEquals("Winner: " + pokemon1, winner);
    }

    @Test
    public void testGetPokemonWinner_Draw() throws Exception {
        String challenger = "Pikachu";
        String challenged = "Charmander";
        PokemonResponse pokemon1 = new PokemonResponse(challenger, 10L, 5L, List.of(GameIndex.builder()
                .game_index(7)
                .build()));
        PokemonResponse pokemon2 = new PokemonResponse(challenged, 8L, 4L, List.of(GameIndex.builder()
                .game_index(7)
                .build()));
        when(pokemonClient.getPokemon(challenger)).thenReturn(pokemon1);
        when(pokemonClient.getPokemon(challenged)).thenReturn(pokemon2);

        String result = service.getPokemonWinner(new PokemonRequest(challenger, challenged));

        assertEquals("DRAW", result);
    }
}
