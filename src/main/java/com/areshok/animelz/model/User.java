package com.areshok.animelz.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "username", "activationCode"})
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Імя користувача не може бути пустим")
    private String username;

    @NotBlank(message = "Пароль не може бути пустим")
    private String password;

    @Email(message = "Неправильний email")
    @NotBlank(message = "Email не може бути пустим")
    private String email;

    private boolean active;

    @Column(name = "activation_code")
    private String activationCode;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Column(name = "user_image")
    private String userImage;

    @Column(name = "join_date")
    private String joinDate;

    @ManyToMany
    private List<Anime> watchingList = new ArrayList<>();
    @ManyToMany
    private List<Anime> wantToWatchList = new ArrayList<>();
    @ManyToMany
    private List<Anime> watchedList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnimeRating> animeRatings = new ArrayList<>();

    public void addToWatchingList(Anime anime) {
        this.watchingList.add(anime);
    }

    public void addToWantToWatchList(Anime anime) {
        this.wantToWatchList.add(anime);
    }

    public void addToWatchedList(Anime anime) {
        this.watchedList.add(anime);
    }

    public String getName() {
        return this.username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
