package com.codeup.bitebook.repositories;


import com.codeup.bitebook.models.Post;
import com.codeup.bitebook.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository <Post, Long>{
    List<Post> findByCreator(User creator);

    List<Post> findByCreatorOrderByCreatedDateDesc(User loggedInUser);
}
