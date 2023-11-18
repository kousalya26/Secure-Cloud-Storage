package com.cahcet.FinalProject.web;

import com.cahcet.FinalProject.model.User;
import com.cahcet.FinalProject.repository.FileRepository;
import com.cahcet.FinalProject.repository.UserRepository;
import com.cahcet.FinalProject.service.UserService;
import com.cahcet.FinalProject.web.dto.UserKeyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FileRepository fileRepository;
	UserService userService;
	private String preHtml = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>List of uploads</title><style>@list-height: 90px; @image-width: 60px; @list-bg-color: #f1f1f1; @list-hover-color: #fff; @border-color: #e1e1e1; @color-primary: #FF4E00; *{ margin:0; padding:0; box-sizing: border-box; vertical-align:middle; } body{ padding:60px 100px; background-color:#f1f1f1; } .navbar { background-color: #333; overflow: hidden; } .navbar a { float: left; color: white; text-align: center; padding: 14px 16px; text-decoration: none; } .navbar a:hover { background-color: #ddd; color: black; } .file-list{ list-style:none; border-bottom:solid 1px @border-color; border-top:solid 1px @border-color; li{ margin:0; a{ position:relative; padding:1rem; display:block; background-color:@list-bg-color; color:#333; border-bottom:solid 1px @border-color; transition: all ease .3s; .thumb-nail{ display:inline-block; width:@image-width; height:@list-height; overflow:hidden; border-radius:2px; float:left; box-shadow:0 -1px 6px rgba(0,0,0,.2); img{ width:100%; height:auto; display:inline-block;} } h3{ margin:0 1rem; display:inline-block; font-size:1.6rem; font-weight:normal; height:@list-height; line-height:@list-height; } &:after{ content: \"\\f063\"; display:inline-block; //position:absolute; font:normal normal normal 14px/1 FontAwesome; float:right; width:@image-width; height:@list-height; line-height:@list-height; font-size:2rem; opacity:0; transition: all ease .3s; } &:hover{ z-index:1; background-color: @list-hover-color; box-shadow: 0 6px 12px rgba(0,0,0,.1); border-bottom:solid 1px transparent; transform: translateY(-6px); &:after{ opacity:.6; color: @color-primary; } } } } }</style></head><body><div class=\"navbar\"><a href=\"/\">Home</a><a href=\"/upload\">Upload</a><a href=\"/login/logout\">Logout</a></div>";
	private String preList = "<ul class=\"file-list\"><li><a href=\"/download/" ;
	private String mid = "\" target=\"_blank\"><h3>";
	private String postList = "</h3><i></i></a></li></ul>";
	private String postHtml = "</body></html>";
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/success")
	public String uploadsuccess() {
		return "success";
	}
	@GetMapping("/failed")
	public String uploadfailed() {
		return "failed";
	}
//
//		@GetMapping("/")
//	public String home() {
//		return "index";
//	}
	@ModelAttribute("user")
	public UserKeyDto UserKeyDto() {
		return new UserKeyDto();
}

	@GetMapping("/uploader")
	public String uploaded(){
		return "uploader";
	}

	@GetMapping("/uploader/{userid}")
	public String upload(HttpServletResponse response, @PathVariable("userid")String username) {

		User temp =userRepository.findKeyByEmail(username);
		Cookie c = new Cookie("key", temp.getSecKey());
		c.setPath("/");
		response.addCookie(c);
		return "uploader";
	}
	@GetMapping("/list")
	public ResponseEntity<String> list() {
//		List<String> downloads = new ArrayList<>();
		List<String> fileNames = fileRepository.findAllFileNames();
		String downloads="";
		for (String fileName : fileNames) {
			downloads+=preList + fileName + mid+ fileName+ postList;

		}
			String response = String.join("", downloads);
			return ResponseEntity.ok()
					.contentType(MediaType.TEXT_HTML)
					.body(response);
		}


	@GetMapping("/error")
	public String error() { return "error";}
}
