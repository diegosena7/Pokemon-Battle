package br.com.dsena.pokemon.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PokemonResponse implements Serializable {

    private String name;
    private Long height;
    private Long weight;

    private List<GameIndex> game_indices;

    @Override
    public String toString() {
        return "PokemonResponse{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                '}';
    }
}
