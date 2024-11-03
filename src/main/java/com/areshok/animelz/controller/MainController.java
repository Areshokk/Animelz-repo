package com.areshok.animelz.controller;

import com.areshok.animelz.model.Anime;
import com.areshok.animelz.service.AnimeService;
import com.areshok.animelz.service.GenreService;
import com.areshok.animelz.service.UserService;
import com.areshok.animelz.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    private final AnimeService animeService;
    private final UserService userService;
    private final GenreService genreService;

    @Autowired
    public MainController(AnimeService animeService, UserService userService, GenreService genreService) {
        this.animeService = animeService;
        this.userService = userService;
        this.genreService = genreService;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        List<Anime> animeList = animeService.findAll();
        model.addAttribute("animeList", animeList);

        return "main";
    }

    @GetMapping("/search")
    public String search(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 24) Pageable pageable,
            @RequestParam String filter,
            Model model
    ) {
        Page<Anime> page = animeService.findAnimeByTitleOrAlternativeNameLikeIgnoreCase(filter, filter, pageable);
        int[] pagination = ControllerUtils.computePagination(page);

        model.addAttribute("pagination", pagination);
        model.addAttribute("url", "/anime");
        model.addAttribute("page", page);
        model.addAttribute("genres", genreService.findAll());

        return "allAnime";
    }


    @GetMapping("/contacts")
    public String getContacts() {
        return "contacts";
    }


    @GetMapping("/random")
    public String getRandomAnime(){
        Long animeId = animeService.getRandomAnime().getId();

        return "redirect:/anime/" + animeId;

    }

}
