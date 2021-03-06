package com.fivos.thesuperherosquadmaker.api;

import com.fivos.thesuperherosquadmaker.data.CharacterResponse;
import com.fivos.thesuperherosquadmaker.data.ComicsResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MarvelAPI {

    @GET("/v1/public/characters")
    Single<CharacterResponse> getCharacters(
            @Query("ts") String timestamp,
            @Query("apikey") String publicKey,
            @Query("hash") String hash
    );

    @GET("/v1/public/characters/{id}")
    Single<CharacterResponse> getCharacter(
            @Path("id") int id,
            @Query("ts") String timestamp,
            @Query("apikey") String publicKey,
            @Query("hash") String hash
    );

    @GET("/v1/public/characters/{id}/comics")
    Single<ComicsResponse> getComics(
            @Path("id") int characterId,
            @Query("ts") String timestamp,
            @Query("apikey") String publicKey,
            @Query("hash") String hash
    );

    @GET("/v1/public/characters")
    Single<CharacterResponse> getCharactersPaged(
            @Query("ts") String timestamp,
            @Query("apikey") String publicKey,
            @Query("hash") String hash,
            @Query("limit") int pageSize,
            @Query("offset") int skip
    );

}
