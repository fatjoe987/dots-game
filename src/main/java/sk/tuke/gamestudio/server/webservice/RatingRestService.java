package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;
@RestController
@RequestMapping("/api/rating")
public class RatingRestService {
    @Autowired
    private RatingService ratingService;

    @RequestMapping(value = "/{game}",method = RequestMethod.GET)
    public int getAverageRating(@PathVariable String game) {
        return ratingService.getAverageRating(game);
    }

    @RequestMapping(value = "/{game}/{player}",method = RequestMethod.GET)
    public int getRating(@PathVariable String game,@PathVariable String player) {
        return ratingService.getRating(game,player);
    }

    @PostMapping
    public void setRating(@RequestBody Rating rating) {
        ratingService.setRating(rating);
    }
}
