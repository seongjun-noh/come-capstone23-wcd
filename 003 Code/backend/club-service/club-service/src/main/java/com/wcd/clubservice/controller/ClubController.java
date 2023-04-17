package com.wcd.clubservice.controller;

import com.wcd.clubservice.dto.ClubDto;
import com.wcd.clubservice.dto.ClubMemberDto;
import com.wcd.clubservice.jpa.ClubEntity;
import com.wcd.clubservice.jpa.ClubMemberEntity;
import com.wcd.clubservice.service.ClubService;
import com.wcd.clubservice.vo.RequestClub;
import com.wcd.clubservice.vo.RequestUpdateClub;
import com.wcd.clubservice.vo.ResponseClub;
import com.wcd.clubservice.vo.ResponseMember;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/")
public class ClubController {
    Environment env;
    ClubService clubService;

    public ClubController(Environment env, ClubService clubService) {
        this.env = env;
        this.clubService = clubService;
    }

    @GetMapping("health_check")
    public String status() {
        return String.format("It's Working in Club Service on PORT %s", env.getProperty("local.server.port"));
    }

    @GetMapping("/clubs")
    public ResponseEntity<List<ResponseClub>> getClub() {
        Iterable<ClubEntity> clubList = clubService.getClub();

        List<ResponseClub> result = new ArrayList<>();
        clubList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseClub.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/clubs")
    public ResponseEntity<ResponseClub> createClub(@RequestBody RequestClub club) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ClubDto clubDto = mapper.map(club, ClubDto.class);
        clubDto = clubService.createClub(clubDto);

        ResponseClub responseClub = mapper.map(clubDto, ResponseClub.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseClub);
    }

    @GetMapping("/clubs/{club-id}")
    public ResponseEntity<ResponseClub> getClubById(@PathVariable("club-id") Long clubId) {
        ClubDto clubDto = clubService.getClubById(clubId);

        ResponseClub returnValue = new ModelMapper().map(clubDto, ResponseClub.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @PutMapping("/clubs/{club-id}")
    public ResponseEntity<ResponseClub> updateClub(@PathVariable("club-id") Long clubId, @RequestBody RequestUpdateClub requestUpdateClub) {
        ClubDto clubDto = clubService.updateClubById(clubId, requestUpdateClub);

        ResponseClub returnValue = new ModelMapper().map(clubDto, ResponseClub.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @DeleteMapping("/clubs/{club-id}")
    public ResponseEntity<Void> deleteClub(@PathVariable("club-id") Long clubId) {
        clubService.deleteClub(clubId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clubs/{club-id}/member")
    public ResponseEntity<List<ResponseMember>> getClubMember(@PathVariable("club-id") Long clubId) {
        Iterable<ClubMemberEntity> clubMemberList = clubService.getClubMember(clubId);

        List<ResponseMember> result = new ArrayList<>();
        clubMemberList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseMember.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/clubs/{club-id}/member/{user-id}")
    public ResponseEntity<ResponseMember> createClubMember(@PathVariable("club-id") Long clubId, @PathVariable("user-id") Long userId) {
        ClubMemberDto clubMemberDto = clubService.createClubMember(clubId, userId);

        ResponseMember responseMember = new ModelMapper().map(clubMemberDto, ResponseMember.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMember);
    }

    @DeleteMapping("/clubs/{club-id}/member/{user-id}")
    public ResponseEntity<Void> deleteClubMember(@PathVariable("club-id") Long clubId, @PathVariable("user-id") Long userId) {
        clubService.deleteClubMember(clubId, userId);

        return ResponseEntity.noContent().build();
    }
}
