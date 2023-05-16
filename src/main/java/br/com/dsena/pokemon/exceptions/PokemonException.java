package br.com.dsena.pokemon.exceptions;

import br.com.dsena.pokemon.model.ErrorMsg;
import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class PokemonNotFoundException {
    @ExceptionHandler(value = FeignException.class)
    public ResponseEntity<ErrorMsg> handlePokemonNotFoundException(final FeignException exception) {

        log.error(exception.getMessage());
        return ResponseEntity
                .status(exception.status())
                .body(ErrorMsg.builder()
                        .status(exception.status())
                        .message(exception.getMessage())
                        .field(exception.request().toString())
                        .build());
    }
}
