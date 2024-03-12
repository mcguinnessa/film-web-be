package alex.filmweb.Films.repository;

import alex.filmweb.Films.model.Film;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


//Now we can use MongoRepository’s methods: save(), findOne(),
// findById(), findAll(), count(), delete(), deleteById()…
// without implementing these methods.
public interface FilmRepository extends MongoRepository<Film, String> {

    Film findByTitleAndYear(String title, Short year);
//    Film findByImdbId(Integer id);

//    Film createFilm(Film film);
}
