package br.com.dsena.pokemon.controller;

import br.com.dsena.pokemon.model.PokemonRequest;
import br.com.dsena.pokemon.model.PokemonResponse;
import br.com.dsena.pokemon.service.PokemonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class PokemonControllerTest {

    @InjectMocks
    private PokemonController controller;

    @Mock
    private PokemonService service;

    private MockMvc mockMvc;

    private PokemonResponse pokemonResponse;
    private PokemonRequest pokemonRequest;

    @BeforeEach
    public void setup() throws Exception {
        this.pokemonResponse = PokemonResponse.builder()
                .weight(16L)
                .name("Pikachu")
                .height(20L)
                .build();

        this.pokemonRequest = PokemonRequest.builder()
                .challenged("Pikachu")
                .challenger("Bulbasaur")
                .build();

        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
    }

    @Test
    public void getPokemonTest() throws Exception {
        when(service.getPokemonClient("Pikachu")).thenReturn(pokemonResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/pokemons/pokemon/{name}", "Pikachu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(pokemonResponse))
        ).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        Mockito.verify(service, Mockito.times(1)).getPokemonClient("Pikachu");
    }

    @Test
    public void testGetPokemonSuccess() {
        when(service.getPokemonClient("Pikachu")).thenReturn(pokemonResponse);

        ResponseEntity<PokemonResponse> responseEntity = controller.getPokemon("Pikachu");

        assertEquals(pokemonResponse, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testgetPokemonWinner() {
        when(service.getPokemonWinner(pokemonRequest)).thenReturn(asJsonString(pokemonResponse.toString()));

        ResponseEntity<String> responseEntity = controller.getPokemonWinner(pokemonRequest);

        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    @Test
    public void getPokemonWinnerTestWithSuccess() throws Exception {
        when(service.getPokemonWinner(pokemonRequest)).thenReturn("Winner: " + pokemonRequest);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(pokemonRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/pokemons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated());
        Mockito.verify(service, Mockito.times(1)).getPokemonWinner(pokemonRequest);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj).replaceAll("\"", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}