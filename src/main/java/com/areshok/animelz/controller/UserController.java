package com.areshok.animelz.controller;

import com.areshok.animelz.model.Anime;
import com.areshok.animelz.model.Genre;
import com.areshok.animelz.model.User;
import com.areshok.animelz.repository.AnimeRepository;
import com.areshok.animelz.service.AnimeService;
import com.areshok.animelz.service.GenreService;
import com.areshok.animelz.service.UserService;
import com.areshok.animelz.util.ControllerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final AnimeRepository animeRepository;

    private final AnimeService animeService;

    private final GenreService genreService;


    @Value("${upload.path}")
    private String uploadPath;

    public UserController(UserService userService, AnimeRepository animeRepository, AnimeService animeService, GenreService genreService) {
        this.userService = userService;
        this.animeRepository = animeRepository;
        this.animeService = animeService;
        this.genreService = genreService;
    }


    @GetMapping()
    public String userCabinet(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        List<Anime> watchingList = user.getWatchingList();
        List<Anime> wantToWatchList = user.getWantToWatchList();
        List<Anime> watchedList = user.getWatchedList();

        model.addAttribute("watchingList", watchingList);
        model.addAttribute("wantToWatchList", wantToWatchList);
        model.addAttribute("watchedList", watchedList);
        model.addAttribute("user", user);
        return "userCabinet";
    }


    @PostMapping("/processForm")
    public ResponseEntity<String> processForm(@RequestParam("selectedOption") String selectedOption,
                                              @RequestParam("animeId") Long animeId,
                                              Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Anime anime = animeRepository.findById(animeId).orElseThrow();

        animeService.checkAnimeList(user, anime, selectedOption);

        userService.save(user);
        log.debug("USER changed anime option animeId={}, option={}",
                anime.getId(), selectedOption);
        return ResponseEntity.ok().build();
    }


    @GetMapping("edit")
    public String showEditForm(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);

        return "/userEditForm";
    }


    @PostMapping("edit")
    public String editUser(@AuthenticationPrincipal User user,
                           @RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email
    ) {
        userService.updateProfile(user, username, password, email);

        log.debug("{} change personal info: username={} password={}, email={}",
                user.getUsername(), username, password, email);

        return "redirect:/user";
    }


    @PostMapping("/edit/avatar")
    public String updateImageUrl(@RequestParam String imageUrl, Principal principal) {

        String userName = principal.getName();

        userService.updateProfileImageByUserId(userName, imageUrl);

        log.debug("USER changed profile photo userName={}, imageUrl={}",
                userName, imageUrl);

        return "userCabinet";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("add")
    public String addProductToBd(Model model) {
        List<Genre> genreList = genreService.findAll();
        model.addAttribute("genreList", genreList);
        return "admin/addToDb";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public String addAnime(@Valid Anime anime, BindingResult bindingResult, @RequestParam List<String> genres, Model model) {

        Set<Genre> genreSet = genres.stream()
                .map(genreService::findByName)
                .collect(Collectors.toSet());
        anime.setGenres(genreSet);

        animeService.save(anime);

        log.debug("ADMIN added new anime: title={}", anime.getTitle());

        return "redirect:/user";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("animeList")
    public String getAllProducts(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 12) Pageable pageable, Model model) {
        Page<Anime> page = animeService.findAll(pageable);
        int[] pagination = ControllerUtils.computePagination(page);

        model.addAttribute("pagination", pagination);
        model.addAttribute("url", "animeList");
        model.addAttribute("page", page);

        return "admin/animeList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("animeEdit/{anime}")
    public String editProduct(@PathVariable Anime anime, Model model) {
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("anime", anime);

        return "/admin/animeEdit";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("animeList")
    public String saveEditedAnime(@Valid Anime anime,
                                  BindingResult bindingResult,
                                  @RequestParam List<String> genres,
                                  Model model) {

        animeService.saveInfoById(anime.getTitle(), anime.getAlternativeName(), anime.getType(), anime.getEpisodes(),
                anime.getStatus(), genres, anime.getSource(), anime.getSeason(), anime.getStudio(),
                anime.getAgeRestrictions(), anime.getDuration(), anime.getVoiceover(), anime.getProtagonists(), anime.getDescription(),
                anime.getVideoUrl(), anime.getId());

        log.debug("ADMIN edited anime: id={}, title={}", anime.getId(), anime.getTitle());

        return "redirect:/user";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("genre")
    public String genreForm(Model model) {
        List<Genre> genreList = genreService.findAll();

        model.addAttribute("genreList", genreList);

        return "/admin/genrePage";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("genre/add")
    public String addGenre(String name) {
        genreService.save(name);

        log.debug("ADMIN added new genre: genreName={}", name);

        return "redirect:/user/genre";
    }

@PreAuthorize("hasAuthority('ADMIN')")
@GetMapping("genre/edit/{id}")
public String editGenreForm(@PathVariable Long id, Model model) {
    Genre genre = genreService.findById(id).orElseThrow(() -> new RuntimeException("Genre not found"));
    model.addAttribute("genre", genre);
    return "/admin/editGenre";
}

@PreAuthorize("hasAuthority('ADMIN')")
@PostMapping("genre/edit/{id}")
public String editGenre(@PathVariable Long id, @RequestParam String name) {
    genreService.editGenre(id, name);
    log.debug("ADMIN edited genre: genreId={}, genreName={}", id, name);
    return "redirect:/user/genre";
}

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("genre/delete/{id}")
    public String deleteGenre(@PathVariable Long id) {
        try {
            genreService.deleteById(id);
            log.debug("ADMIN deleted genre: genreId={}", id);
            return "redirect:/user/genre";
        } catch (Exception e) {
            log.error("Error deleting genre: genreId={}", id, e);
            return "redirect:/user/genre";
        }
    }

}