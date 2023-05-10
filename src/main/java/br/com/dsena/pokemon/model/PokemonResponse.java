package br.com.dsena.pokemon.model;

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

    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                '}';
    }
}
