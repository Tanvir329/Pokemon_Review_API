package com.pokemonreview.api.service.impl;

import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.exceptions.PokemonNotFoundException;
import com.pokemonreview.api.exceptions.ReviewNotFoundException;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.repository.ReviewRepository;
import com.pokemonreview.api.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
    private PokemonRepository pokemonRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, PokemonRepository pokemonRepository) {
        this.reviewRepository = reviewRepository;
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public ReviewDto createReview(int pokemonId, ReviewDto reviewDto) {
        Review review = mapToEntity(reviewDto);
        Pokemon pokemon = pokemonRepository.findById(pokemonId).orElseThrow(() -> new PokemonNotFoundException("Pokemon with associated Id could not be found"));
        review.setPokemon(pokemon);
        Review newreview = reviewRepository.save(review);
        return mapToDto(newreview);

    }

    @Override
    public List<ReviewDto> getReviewsByPokemonId(int pokemonId) {
        List<Review> reviews = reviewRepository.findByPokemonId(pokemonId);

        return reviews.stream().map(review -> mapToDto(review)).collect(Collectors.toList());
    }


    @Override
    public ReviewDto getReviewById(int pokemonId, int reviewId) {
        Pokemon pokemon = pokemonRepository.findById(pokemonId).orElseThrow(() -> new PokemonNotFoundException("Pokemon with associated Id could not be found"));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("Review with associated Id could not be found"));

        if(review.getPokemon().getId() != pokemon.getId()){
            throw new PokemonNotFoundException("This Pokemon does not belong to this Review");
        }
        return mapToDto(review);
    }

    @Override
    public ReviewDto updateReview(int pokemonId, int reviewId, ReviewDto reviewDto) {
        Pokemon pokemon = pokemonRepository.findById(pokemonId).orElseThrow(() -> new PokemonNotFoundException("Pokemon with associated Id could not be found"));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("Review with associated Id could not be found"));

        if(review.getPokemon().getId() != pokemon.getId()){
            throw new PokemonNotFoundException("This Pokemon does not belong to this Review");
        }

        review.setTitle(reviewDto.getTitle());
        review.setContent(reviewDto.getContent());
        review.setStars(reviewDto.getStars());

        Review updatedReview = reviewRepository.save(review);
        return mapToDto(updatedReview);
    }

    @Override
    public void deleteReview(int pokemonId, int reviewId) {
        Pokemon pokemon = pokemonRepository.findById(pokemonId).orElseThrow(() -> new PokemonNotFoundException("Pokemon with associated Id could not be found"));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("Review with associated Id could not be found"));

        if(review.getPokemon().getId() != pokemon.getId()){
            throw new PokemonNotFoundException("This Pokemon does not belong to this Review");
        }
        reviewRepository.delete(review);
    }

    private ReviewDto mapToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setId(review.getId());
        reviewDto.setTitle(review.getTitle());
        reviewDto.setContent(review.getContent());
        review.setStars(review.getStars());

        return reviewDto;
    }

    private Review mapToEntity(ReviewDto reviewDto) {
        Review review = new Review();

        review.setId(reviewDto.getId());
        review.setTitle(reviewDto.getTitle());
        review.setContent(reviewDto.getContent());
        review.setStars(reviewDto.getStars());

        return review;
    }
}
