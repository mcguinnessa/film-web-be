package alex.filmweb.Films.controller;

import alex.filmweb.Films.repository.FilmRepository;
import alex.filmweb.Films.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import jakarta.validation.Valid;



import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

//CrossOrigin configures the allowed origins for CORS (cross access)
//RestController defines the rest interface, responses should be bound to web response body
//RequestMapping means that all APIs URL will start with the given prefix
//Autowired injects the FileRepository Bean to the local variable
//GetMapping defines the route
//@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.121:3000", "http://192.168.0.121:80"})
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin
@RestController
@RequestMapping("api")

public class FilmController {

    //Autowired
    @Autowired
    FilmRepository filmRepository;

    @GetMapping("/films")
    public ResponseEntity<List<Film>> getAllFilms(@RequestParam(name="sort", defaultValue="title") String sort,
                                                  @RequestParam(name="asc", defaultValue="true") boolean asc,
                                                  @RequestParam(name="limit", required = false) String limit){
        System.out.println("Called for /films");
        System.out.println("Sort:" + sort);
        System.out.println("asc:" + asc);
        System.out.println("Limit:" + limit);
        try {
            Sort.Direction sort_direction = Sort.Direction.DESC;
            if(asc){
                sort_direction = Sort.Direction.ASC;
            }
            List<Film> films = new ArrayList<Film>();

            //Pageable pageable = Pageable.unpaged();
            if (null != limit){
                Pageable pageable = PageRequest.of(0, Integer.parseInt(limit), Sort.by(sort_direction, sort));
                filmRepository.findAll(pageable).forEach(films::add);
            } else {
                filmRepository.findAll(Sort.by(sort_direction, sort)).forEach(films::add);
            }

            if(films.isEmpty()){
                System.out.println("Films is empty");
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(films, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error on Films " + e.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/film/{imdbid}")
    public ResponseEntity<Film> getFilmByImdbId(@PathVariable("imdbid") String imdbid){
        System.out.println("Called for /film/{imdbid}");
        try{

            Optional<Film> film = filmRepository.findByImdbid(imdbid);
            //List<Film> film = filmRepository.findByImdbid(imdbid);

            //if(!film.isEmpty()){
            if(film.isPresent()){
                return new ResponseEntity<>(film.get(), HttpStatus.OK);
                //return new ResponseEntity<>(film.get(0), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/film")
    public ResponseEntity<List<Film>> getFilmByTitleAndYear(@RequestParam("title") String title, @RequestParam("year") Short year) {
//        Short year_s = Short.parseShort(year);
        System.out.println("Called for /film GET " + title + " " + year.toString());
        try {

            //Film _film = filmRepository.findByTitleAndYear(film.getTitle(), film.getYear());
            //List<Film> _films = filmRepository.findByTitleAndYearIgnoreCase(title, year);
            List<Film> _films = filmRepository.findByTitleIgnoreCaseAndYear(title, year);


//            Film _film = filmRepository.save(new Film(film.getTitle(), film.getImdbid(), film.getYear(),
//                    film.getRuntime(), film.getImdb_rating(), film.getClassification(), film.getMedia_type(), film.getWatched()));

            System.out.println("_film:" + _films.toString());
            return new ResponseEntity<>(_films, HttpStatus.OK);
        } catch (Exception e){
            System.out.println("Error:" + e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/unwatched")
    public ResponseEntity<List<Film>> getUnwatchedFilms(){
        System.out.println("Called for /unwatched");
        try{
            //List<Film> films = filmRepository.findByWatchedIs(false);
            List<Film> films = filmRepository.findByWatchedFalseOrderByImdbRatingDesc(false);

            if(films.isEmpty()){
                System.out.println("Films is empty");
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(films, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error on Films " + e.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/owned")
    public ResponseEntity<List<Film>> getOwnedFilms(){
        System.out.println("Called for /owned");
        try{
            //List<Film> films = filmRepository.findByWatchedIs(false);
            List<Film> films = filmRepository.findOwned();

            if(films.isEmpty()){
                System.out.println("Films is empty");
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(films, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error on Films " + e.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/film")
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        System.out.println("Called for /film POST:" + film.toString());
        try {
            Film _film = filmRepository.save(new Film(film.getTitle(), film.getImdbid(), film.getYear(),
                    film.getRuntime(), film.getImdb_rating(), film.getClassification(), film.getMedia_type(), film.getWatched()));
            return new ResponseEntity<>(_film, HttpStatus.CREATED);
        } catch (Exception e){
            System.out.println("Error updating record " + e.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/film/{imdbid}")
    public ResponseEntity<Film> updateFilm(@PathVariable("imdbid") String imdbid, @RequestBody Film new_details) {
       System.out.println("Called for /film PUT " + imdbid + " " + new_details.toString() );


       Optional<Film> found = filmRepository.findByImdbid(imdbid);
       //List<Film> found = filmRepository.findByImdbid(imdbid);
       ///if(!found.isEmpty()){
       if(found.isPresent()){
          System.out.println("Found");
          System.out.println("Watched Boolean:" + new_details.getWatched());
          Film film = found.get();
          //Film _film = found.get(0);
          //System.out.println("  Found Film:" + film.toString()); 
          System.out.println("  Found Film " +  film.toString());
//          _film.setTitle(new_details.getTitle());
//          _film.setYear(new_details.getYear());
//          _film.setImdbid(imdbid);
//          _film.setClassification(new_details.getClassification());
//          _film.setImdb_rating(new_details.getImdb_rating());
//          _film.setRuntime(new_details.getRuntime());

          if (new_details.getWatched() != null){
             film.setWatched(new_details.getWatched());
          }

          System.out.println("  Put Film:" + film.toString()); 
          System.out.println(" Before saving imdbid:" + film.getImdbid().toString());
          System.out.println(" Before saving id:" + film.getId().toString());
          Film updated = filmRepository.save(film);
          System.out.println("Updated film: " +  updated.toString());
          return ResponseEntity.ok(updated);
       } else {
          System.out.println("Not found");
          return ResponseEntity.notFound().build();
       }

   }

}
