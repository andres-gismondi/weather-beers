package com.api.beer.meetup.service;

import com.api.beer.beers.meetup.client.WeatherApiClient;
import com.api.beer.beers.meetup.client.model.DayTemperature;
import com.api.beer.beers.meetup.client.model.MainTemperature;
import com.api.beer.beers.meetup.client.model.WeatherResponse;
import com.api.beer.beers.meetup.mapper.MeetupMapper;
import com.api.beer.beers.meetup.model.Meetup;
import com.api.beer.beers.meetup.model.MeetupUser;
import com.api.beer.beers.meetup.repository.IMeetupRepository;
import com.api.beer.beers.meetup.repository.IMeetupUserRepository;
import com.api.beer.beers.meetup.service.MeetupService;
import com.api.beer.beers.users.model.User;
import com.api.beer.beers.users.model.UserRole;
import com.api.beer.beers.users.repository.IUserRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class MeetupServiceTest {

    @MockBean
    private IMeetupRepository meetupRepository;
    @MockBean
    private IUserRepository userRepository;
    @MockBean
    private IMeetupUserRepository meetupUserRepository;
    private MeetupMapper mapper = new MeetupMapper();
    @MockBean
    private WeatherApiClient apiClient;

    @InjectMocks
    private MeetupService meetupService;


    @Test
    public void getTemperatureTest() throws IOException {
        Mockito.when(meetupRepository.findMeetupById(any())).thenReturn(Optional.of(this.buildMeetup()));
        Mockito.when(apiClient.getWeather(any())).thenReturn(this.buildApiClientResponse());
        int temp = this.meetupService.getTemperature(1L);

        Assertions.assertEquals(temp, 26);
    }

    private Meetup buildMeetup() {
        Meetup meetup = new Meetup();
        User user = this.buildUser();
        meetup.setUser(user);
        meetup.setCity("Mendoza");
        meetup.setName("Cervezas");
        meetup.setTemp(0);
        meetup.setDuration(2);
        meetup.setDate(LocalDateTime.now());
        meetup.setBeers(0);
        meetup.setId(1L);
        MeetupUser meetupUser = new MeetupUser(meetup, user, Boolean.FALSE);
        meetup.getMeetupUsers().add(meetupUser);
        return meetup;
    }

    private User buildUser() {
        User user = new User();
        user.setFirstName("test-first-name");
        user.setLastName("test-last-name");
        user.setSurName("test-surname");
        user.setPassword("1234");
        user.setAge(55);
        user.setId(1L);
        user.setMeetups(Lists.emptyList());
        user.setRole(UserRole.USER);
        return user;
    }

    private WeatherResponse buildApiClientResponse() {
        List<DayTemperature> days = Lists.newArrayList();
        DayTemperature day = new DayTemperature();
        MainTemperature main = new MainTemperature();
        main.setTempMax(26);
        day.setMain(main);
        days.add(day);

        WeatherResponse weather = new WeatherResponse();
        weather.setDays(days);

        return weather;
    }

}
