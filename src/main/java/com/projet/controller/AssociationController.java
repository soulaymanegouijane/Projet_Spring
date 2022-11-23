package com.projet.controller;

import com.projet.entity.Member;
import com.projet.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RestController
@RequestMapping("/api/association")
public class AssociationController {

    @Autowired
    MemberService memberService;

    @GetMapping("/{id}/members")
    public Set<Member> retreiveAllMembers(@PathVariable long id){
        return memberService.retreiveAllMembers(id);
    }
}
