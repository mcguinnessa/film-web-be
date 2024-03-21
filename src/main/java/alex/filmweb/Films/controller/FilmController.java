package alex.filmweb.Films.controller;

import alex.filmweb.Films.repository.FilmRepository;
import alex.filmweb.Films.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<List<Film>> getAllFilms(){
        System.out.println("Called for /films");
        try{
            List<Film> films = new ArrayList<Film>();

            filmRepository.findAll().forEach(films::add);

//            List<Film> all = filmRepository.findAll();
//            for(Film f : all){
//                System.out.println("Film:" + f.toString());
//                films.add(f);
//            }

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


    @GetMapping("/films/{id}")
    public ResponseEntity<Film> getFilmByImdbId(@PathVariable("id") String id){
        System.out.println("Called for /films/{id}");
        try{

            Optional<Film> film = filmRepository.findById(id);

            if(film.isPresent()){
                return new ResponseEntity<>(film.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

//            List<Film> films = new ArrayList<Film>();
//
//            myFilmRepository.findAll().forEach(films::add);
//
//            if(films.isEmpty()){
//                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
//            }

//            return new ResponseEntity<>(films, HttpStatus.OK);
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
            List<Film> _films = filmRepository.findByTitleAndYear(title, year);


//            Film _film = filmRepository.save(new Film(film.getTitle(), film.getImdbid(), film.getYear(),
//                    film.getRuntime(), film.getImdb_rating(), film.getClassification(), film.getMedia_type(), film.getWatched()));

            System.out.println("_film:" + _films.toString());
            return new ResponseEntity<>(_films, HttpStatus.OK);
        } catch (Exception e){
            System.out.println("Error:" + e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/film")
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        System.out.println("Called for /films POST:" + film.toString());
        try {
            Film _film = filmRepository.save(new Film(film.getTitle(), film.getImdbid(), film.getYear(),
                    film.getRuntime(), film.getImdb_rating(), film.getClassification(), film.getMedia_type(), film.getWatched()));
            return new ResponseEntity<>(_film, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
