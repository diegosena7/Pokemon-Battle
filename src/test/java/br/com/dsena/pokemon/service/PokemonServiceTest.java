package br.com.dsena.pokemon.service;

import br.com.dsena.pokemon.model.PokemonRequest;
import br.com.dsena.pokemon.model.PokemonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPokemonClient(){
        when(pokemonClient.getPokemon("Pikachu")).thenReturn(pokemonResponse);
        when(pokemonClient.getPokemon("Bulbassaur")).thenReturn(pokemonResponse);
        PokemonResponse responseService = service.getPokemonClient("Pikachu");

        assertEquals(pokemonResponse.getHeight(), responseService.getHeight());
        assertEquals(pokemonResponse.getName(), responseService.getName());
        assertEquals(pokemonResponse.getWeight(), responseService.getWeight());
    }

    @Test
    public void testGetPokemonWinner_Success() {
        String challenger = "Pikachu";
        String challenged = "Charmander";
        PokemonResponse pokemon1 = new PokemonResponse(challenger, 10L, 5L);
        PokemonResponse pokemon2 = new PokemonResponse(challenged, 8L, 4L);
        when(pokemonClient.getPokemon(challenger)).thenReturn(pokemon1);
        when(pokemonClient.getPokemon(challenged)).thenReturn(pokemon2);

        String winner = service.getPokemonWinner(new PokemonRequest(challenger, challenged));

        assertEquals("Winner: " + pokemon1, winner);
    }

    @Test
    public void testGetPokemonWinner_Draw() {
        String challenger = "Pikachu";
        String challenged = "Charmander";
        PokemonResponse pokemon1 = new PokemonResponse(challenger, 10L, 5L);
        PokemonResponse pokemon2 = new PokemonResponse(challenged, 10L, 5L);
        when(pokemonClient.getPokemon(challenger)).thenReturn(pokemon1);
        when(pokemonClient.getPokemon(challenged)).thenReturn(pokemon2);

        String result = service.getPokemonWinner(new PokemonRequest(challenger, challenged));

        assertEquals("DRAW", result);
    }
}
