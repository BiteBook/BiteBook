package com.codeup.bitebook.controllers;

import com.codeup.bitebook.models.Post;
import com.codeup.bitebook.models.User;
import com.codeup.bitebook.repositories.PostRepository;
import com.codeup.bitebook.repositories.UserRepository;
import com.codeup.bitebook.services.Authenticator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserRepository userDao;


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
        model.addAttribute("userPosts", userPosts);

        return "/posts/user";
    }

}