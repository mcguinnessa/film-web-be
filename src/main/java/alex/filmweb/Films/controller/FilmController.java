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
@CrossOrigin(origins = "http://localhost:3000")
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


}
