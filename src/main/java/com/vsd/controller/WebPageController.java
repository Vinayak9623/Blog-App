package com.vsd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebPageController {

    @GetMapping({"/", "/articles", "/home"})
    public String index() {
        return "index";
    }

    @GetMapping({"/article/{id}", "/articles/{id}"})
    public String articleDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("articleId", id);
        return "article-detail";
    }

    @GetMapping({"/create-article", "/articles/create", "/articles/new"})
    public String createArticle() {
        return "create-article";
    }

    @GetMapping({"/edit-article/{id}", "/articles/edit/{id}"})
    public String editArticle(@PathVariable("id") Long id, Model model) {
        model.addAttribute("articleId", id);
        return "edit-article";
    }

    @GetMapping({"/categories", "/explore"})
    public String categories() {
        return "categories";
    }

    @GetMapping({"/dashboard", "/my-articles"})
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping({"/admin", "/admin-panel"})
    public String admin() {
        return "admin";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
}
