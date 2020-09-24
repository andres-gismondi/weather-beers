package com.api.beer.beers.meetup.mapper;

import com.api.beer.beers.meetup.dto.MeetupDto;
import com.api.beer.beers.meetup.dto.MeetupInformationDto;
import com.api.beer.beers.meetup.model.Meetup;
import com.api.beer.beers.users.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MeetupMapper {

    private static final int PLUS_BOX = 1;

    public MeetupDto mapToDto(Meetup meetup) {
        MeetupDto meetupDto = new MeetupDto();
        meetupDto.setId(meetup.getId());
        meetupDto.setName(meetup.getName());
        meetupDto.setCity(meetup.getCity());
        meetupDto.setCreatorId(meetup.getUser().getId());
        meetupDto.setDate(meetup.getDate());
        meetupDto.setDuration(meetup.getDuration());
        meetupDto.setBeers(meetup.getBeers());
        meetupDto.setEnrolled(meetup.getMeetupUsers().size());
        return meetupDto;
    }

    public List<MeetupDto> mapToDto(List<Meetup> meetups) {
        return meetups.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Meetup mapToModel(MeetupDto meetupDto, User user, int temp) {
        Meetup meetup = new Meetup();
        meetup.setUser(user);
        meetup.setName(meetupDto.getName());
        meetup.setCity(meetupDto.getCity());
        meetup.setBeers(meetupDto.getBeers());
        meetup.setDate(meetupDto.getDate());
        meetup.setDuration(meetupDto.getDuration());
        meetup.setTemp(temp);
        return meetup;
    }

    public MeetupInformationDto mapMeetupInformation(int beersBox, int temp, String description) {
        MeetupInformationDto meetupInformationDto = new MeetupInformationDto();
        meetupInformationDto.setBeers(beersBox + PLUS_BOX);
        meetupInformationDto.setTemp(temp);
        meetupInformationDto.setDescription(description);
        return meetupInformationDto;
    }

}
