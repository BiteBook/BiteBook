package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.Post;
import com.codeup.bitebook.models.User;
import com.codeup.bitebook.repositories.PostRepository;
import com.codeup.bitebook.repositories.UserRepository;
import com.codeup.bitebook.services.Authenticator;
import lombok.AllArgsConstructor;
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
        model.addAttribute("title", "Posts");
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        model.addAttribute("title", "Post Details");
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("post", optionalPost.get());
        return "/posts/show";
    }

    @GetMapping("/create")
    public String showCreate(Model model) {
        model.addAttribute("newPost", new Post());
        model.addAttribute("title", "Create Post");
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
        model.addAttribute("title", "Edit Post");
        return "/posts/create";
    }

    @GetMapping("/user/{username}")
    public String showUserPosts(@PathVariable String username, Model model) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            return "redirect:/posts";
        }

        List<Post> userPosts = postDao.findByCreatorOrderByCreatedDateDesc(user);
        List<Post> firstThreePosts;
        if (userPosts.size() >= 3) {
            firstThreePosts = userPosts.subList(0, 3);
        } else {
            firstThreePosts = userPosts;
        }
        model.addAttribute("userPosts", firstThreePosts);
        model.addAttribute("title", "User Posts");
        return "users/userPosts";
    }


    @DeleteMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        Post post = postRepository.findById(id).orElseThrow();
        if (!post.getCreator().equals(currentUser)) {
            return "redirect:/error";
        }
        postRepository.deleteById(id);
        return "redirect:/posts";
    }

    @PostMapping("/{id}/edit")
    public String editPost(@PathVariable Long id, @ModelAttribute Post post) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        Post existingPost = postRepository.findById(id).orElseThrow();
        if (!existingPost.getCreator().equals(currentUser)) {
            return "redirect:/error";
        }
        existingPost.setTitle(post.getTitle());
        existingPost.setBody(post.getBody());
        postRepository.save(existingPost);
        return "redirect:/posts/" + id;
    }

}