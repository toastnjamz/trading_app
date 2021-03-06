package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@Api(description="CRUD operations pertaining to Rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    private static final Logger log = LoggerFactory.getLogger(BidListController.class);

    /**
     * Loads all Ratings
     * @param model
     * @return ModelAndView
     */
    @GetMapping("/rating/list")
    public ModelAndView home(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("ratingList", ratingService.findAllRatings());
        mav.setViewName("rating/list");
        log.info("GET request received for home()");
        return mav;
    }

    /**
     * Loads Rating add form
     * @param rating
     * @return ModelAndView
     */
    @GetMapping("/rating/add")
    public ModelAndView addRatingForm(Rating rating) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("rating/add");
        log.info("GET request received for addRatingForm()");
        return mav;
    }

    /**
     * Validates and creates a new Rating
     * @param rating
     * @param result
     * @param model
     * @return ModelAndView
     */
    @PostMapping("/rating/validate")
    public ModelAndView validate(@Valid Rating rating, BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (!result.hasErrors()) {
            ratingService.createRating(rating);
            model.addAttribute("ratingList", ratingService.findAllRatings());
            mav.setViewName("redirect:/rating/list");
            log.info("Add Rating " + rating.toString());
            return mav;
        }
        mav.setViewName("rating/add");
        return mav;
    }

    /**
     * Navigates to the update form for the requested Rating
     * @param id
     * @param model
     * @return ModelAndView
     */
    @GetMapping("/rating/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        Rating rating = ratingService.findById(id);
        if (rating != null) {
            model.addAttribute("rating", rating);
            mav.setViewName("rating/update");
            log.info("GET request received for showUpdateForm()");
            return mav;
        }
        return mav;
    }

    /**
     * Validates and updates the requested Rating
     * @param id
     * @param rating
     * @param result
     * @param model
     * @return ModelAndView
     */
    @PostMapping("/rating/update/{id}")
    public ModelAndView updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                                     BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("rating/update");
            return mav;
        }
        rating.setId(id);
        ratingService.updateRating(rating);
        model.addAttribute("ratingList", ratingService.findAllRatings());
        mav.setViewName("redirect:/rating/list");
        log.info("Update Rating " + rating.toString());
        return mav;
    }

    /**
     * Deletes the requested Rating
     * @param id
     * @param model
     * @return ModelAndView
     */
    @GetMapping("/rating/delete/{id}")
    public ModelAndView deleteRating(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        Rating rating = ratingService.findById(id);
        if (rating != null) {
            ratingService.deleteRating(id);
            model.addAttribute("ratingList", ratingService.findAllRatings());
            mav.setViewName("redirect:/rating/list");
            log.info("Delete Rating " + rating.toString());
            return mav;
        }
        return mav;
    }
}
