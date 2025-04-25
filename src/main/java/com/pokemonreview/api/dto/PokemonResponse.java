package com.pokemonreview.api.dto;

import com.pokemonreview.api.models.Pokemon;
import lombok.Data;

import java.util.List;

@Data
public class PokemonResponse {

    public List<PokemonDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
