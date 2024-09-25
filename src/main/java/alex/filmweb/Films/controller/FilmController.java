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

            //filmRepository.findAll().forEach(films::add);
            //filmRepository.findAll(Sort.by(Sort.Direction.DESC, sort)).forEach(films::add);
            //filmRepository.findAll(PageRequest.of(0, Integer.parseInt(limit), Sort.by(Sort.Direction.DESC, sort))).forEach(films::add);
            //filmRepository.findAllOrderByUpdated(Limit.of(10)).forEach(films::add);




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


    @GetMapping("/film/{imdbid}")
    public ResponseEntity<Film> getFilmByImdbId(@PathVariable("imdbid") String imdbid){
        System.out.println("Called for /film/{imdbid}");
        try{

            //Optional<Film> film = filmRepository.findById(id);
            List<Film> film = filmRepository.findByImdbid(imdbid);

//            if(film.isPresent()){
            if(!film.isEmpty()){
                return new ResponseEntity<>(film.get(0), HttpStatus.OK);
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
            List<Film> films = filmRepository.findByWatchedIs(false);

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

//    public ResponseEntity<Film> getFilmByImdbId(@PathVariable("id") String id){
//        System.out.println("Called for /films/{id}");

    @PatchMapping("/film/{imdbid}")
    public ResponseEntity<Film> setParam(@PathVariable("imdbid") String imdbid, @RequestBody Film partial) {
        System.out.println("Called for /film PATCH " + imdbid + " partial:" + partial.toString());

        try {
            List<Film> found = filmRepository.findByImdbid(imdbid);
            //Optional<Film> found = filmRepository.findById(id);
            if(found.isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            Film _found = found.get(0);
            System.out.println("Update Field(found):" + _found.getUpdated().toString());

            Class<?> filmClass= Film.class;
            Field[] filmFields=filmClass.getDeclaredFields();

            for (Field field : filmFields){
                System.out.println("Field:" + field.toString());
                field.setAccessible(true);

                Object value = field.get(partial);
                if(value != null){
                    field.set(_found, value);
                }
                field.setAccessible(false);
            }

            System.out.println("Update Field(found - updated):" + _found.getUpdated().toString());
            Film saved = filmRepository.save(_found);
            System.out.println("Update Field(saved):" + saved.getUpdated().toString());
            return new ResponseEntity<>(saved, HttpStatus.OK);
//            List<Film> _films = filmRepository.findByTitleAndYear(title, year);
//
//            System.out.println("_film:" + _films.toString());
//            if(_films.size() == 1){
//                Film file_to_update = _films.get(0);
//                file_to_update.setWatched(watched);
//                //rc = setWatchedForId(_films[0].id)
//                System.out.println("Setting Watched:");
//                Film _film = filmRepository.save(file_to_update);
//            } else{
//                System.out.println("Too many films returned:" + _films.size());
//                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//            }

//            return new ResponseEntity<>(_films.get(0), HttpStatus.OK);


//            Film _film = filmRepository.save(new Film(film.getTitle(), film.getImdbid(), film.getYear(),
//                    film.getRuntime(), film.getImdb_rating(), film.getClassification(), film.getMedia_type(), film.getWatched()));
//            return new ResponseEntity<>(_film, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/film/{imdbid}")
    public ResponseEntity<Film> updateFilm(@PathVariable("imdbid") String imdbid, @RequestBody Film old) {
       System.out.println("Called for /film PUT " + imdbid + " " + old.toString() );

        try {
            List<Film> found = filmRepository.findByImdbid(imdbid);
            if(found.isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            Film _found = found.get(0);
            System.out.println("Update Field(found):" + _found.getUpdated().toString());

            Class<?> filmClass= Film.class;
            Field[] filmFields=filmClass.getDeclaredFields();

            for (Field field : filmFields){
                System.out.println("Field:" + field.toString());
                field.setAccessible(true);

                Object value = field.get(old);
                if(value != null){
                    field.set(_found, value);
                    System.out.println("Value:" + value.toString());
                }
                field.setAccessible(false);
            }

            System.out.println("Update Field(found - updated):" + _found.getUpdated().toString());
            Film saved = filmRepository.save(_found);
            System.out.println("Update Field(saved successfully):" + saved.getUpdated().toString());
            return new ResponseEntity<>(saved, HttpStatus.OK);

        } catch (Exception e){
            System.out.println("Failed to update:" + e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @PutMapping("/film/set")
//    public ResponseEntity<Film> setParam(@RequestParam("title") String title, @RequestParam("year") Short year, @RequestParam("watched") Boolean watched) {
//        System.out.println("Called for /film PUT " + title + " " + year.toString() + " watched:" + watched.toString());
//
//        try {
//
//            List<Film> _films = filmRepository.findByTitleAndYear(title, year);
//
//            System.out.println("_film:" + _films.toString());
//            if(_films.size() == 1){
//                Film file_to_update = _films.get(0);
//                file_to_update.setWatched(watched);
//                //rc = setWatchedForId(_films[0].id)
//                System.out.println("Setting Watched:");
//                Film _film = filmRepository.save(file_to_update);
//            } else{
//                System.out.println("Too many films returned:" + _films.size());
//                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//
//            return new ResponseEntity<>(_films.get(0), HttpStatus.OK);
//
//
////            Film _film = filmRepository.save(new Film(film.getTitle(), film.getImdbid(), film.getYear(),
////                    film.getRuntime(), film.getImdb_rating(), film.getClassification(), film.getMedia_type(), film.getWatched()));
////            return new ResponseEntity<>(_film, HttpStatus.CREATED);
//        } catch (Exception e){
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
