package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.Post;
import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.User;
import com.codeup.bitebook.repositories.PostRepository;
import com.codeup.bitebook.repositories.UserRepository;
import com.codeup.bitebook.services.Authenticator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor

@Controller
@RequestMapping("/posts")
public class PostController {
    private PostRepository postDao;
    private PostRepository postRepository;
    private UserRepository userDao;
    private UserRepository userRepository;


    @GetMapping("")
    public String posts(Model model){
        List<Post> posts = postDao.findAll();

        model.addAttribute("posts",posts);
        return "/posts/index";
    }
    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Post> optionalPost = postDao.findById(id);
        if (optionalPost.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "The post does not exist.");
            return "redirect:/posts";
        }

        model.addAttribute("post", optionalPost.get());
        return "/posts/show";
    }


    @GetMapping("/create")
    public String showCreate(Model model) {
        model.addAttribute("newPost", new Post());
        return "/posts/create";
    }

    @PostMapping("/create")
    public String doCreate(@ModelAttribute Post post) {
        User loggedInUser = Authenticator.getLoggedInUser();
        post.setCreator(loggedInUser);
        post.setCreatedDate(LocalDateTime.now());
        postDao.save(post);

        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Post postToEdit = postDao.getReferenceById(id);
        model.addAttribute("newPost", postToEdit);
        return "/posts/create";
    }
    // PostController.java

    @GetMapping("/user/{username}")
    public String showUserPosts(@PathVariable String username, Model model) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            return "redirect:/posts";
        }

        List<Post> userPosts = postDao.findByCreatorOrderByCreatedDateDesc(user);
        List<Post> firstThreePosts = userPosts.subList(0,2);
        model.addAttribute("userPosts", firstThreePosts);

        return "users/userPosts";
    }
    @DeleteMapping("/posts/{id}")
    public String deletePost(@PathVariable Long id) {
        org.springframework.security.core.userdetails.UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        Post post = postRepository.findById(id).orElseThrow();
        if (!post.getUser().equals(currentUser)) {
            return "redirect:/error";
        }
        postRepository.deleteById(id);
        return "redirect:/posts";
    }
    @GetMapping("/posts/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model, Authentication authentication) {
        Post post = postRepository.findById(id).orElseThrow();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        if (!post.getUser().equals(currentUser)) {
            return "redirect:/error";
        }

        model.addAttribute("post", post);
        return "editPost";
    }
    @PutMapping("/posts/edit/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post updatedPost, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        Post currentPost = postRepository.findById(id).orElseThrow();

        if (!currentPost.getUser().equals(currentUser)) {
            return "redirect:/error";
        }

        updatedPost.setUser(currentUser);
        updatedPost.setId(id);
        postRepository.save(updatedPost);
        return "redirect:/posts/" + id;
    }

}