package alex.filmweb.Films.repository;

import alex.filmweb.Films.model.Film;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.mongodb.repository.Query;


import java.util.List;
import java.util.Optional;


//Now we can use MongoRepository’s methods: save(), findOne(),
// findById(), findAll(), count(), delete(), deleteById()…
// without implementing these methods.
public interface FilmRepository extends MongoRepository<Film, String> {

    List<Film> findByTitleAndYear(String title, Short year);

    List<Film> findByTitleIgnoreCaseAndYear(String title, Short year);

    Optional<Film> findByImdbid(String imdbid);

//    @Modifying
//    @Query("update Film ear set ear.status = ?1 where ear.id = ?2")
//    int setStatusForEARAttachment(Integer status, Long id);

    @Query(value = "{ 'watched': false, 'media_type': { $ne: 'N/A' } }", sort = "{ 'imdb_rating': -1 }")
    List<Film> findByWatchedFalseOrderByImdbRatingDesc(boolean isWatched);

    @Query(value = "{ 'media_type': { $ne: 'N/A' } }", sort = "{ 'imdb_rating': -1 }")
    List<Film> findOwned();

//    int setWatchedForId(String id)

//    Film findByImdbId(Integer id);

//    Film createFilm(Film film);
}
