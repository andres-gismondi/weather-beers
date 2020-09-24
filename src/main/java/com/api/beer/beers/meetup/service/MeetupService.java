package com.api.beer.beers.meetup.service;

import com.api.beer.beers.meetup.client.WeatherApiClient;
import com.api.beer.beers.meetup.client.model.DayTemperature;
import com.api.beer.beers.meetup.client.model.WeatherResponse;
import com.api.beer.beers.meetup.dto.MeetupDto;
import com.api.beer.beers.meetup.dto.MeetupInformationDto;
import com.api.beer.beers.meetup.errors.MeetupAlreadyExistException;
import com.api.beer.beers.meetup.errors.MeetupErrorSavingException;
import com.api.beer.beers.meetup.errors.UserAssociatedException;
import com.api.beer.beers.meetup.mapper.MeetupMapper;
import com.api.beer.beers.meetup.model.Meetup;
import com.api.beer.beers.meetup.model.MeetupUser;
import com.api.beer.beers.meetup.repository.IMeetupRepository;
import com.api.beer.beers.meetup.repository.IMeetupUserRepository;
import com.api.beer.beers.users.errors.UserNotFoundException;
import com.api.beer.beers.users.errors.UserWithNoPermissionException;
import com.api.beer.beers.users.model.User;
import com.api.beer.beers.users.model.UserRole;
import com.api.beer.beers.users.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.el.MethodNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class MeetupService {

     private static Logger LOGGER = LoggerFactory.getLogger(MeetupService.class);

     private static final int BEERS_BOX = 6;

     private IMeetupRepository meetupRepository;
     private IUserRepository userRepository;
     private IMeetupUserRepository meetupUserRepository;
     private MeetupMapper mapper;
     private WeatherApiClient apiClient;

     @Autowired
     public MeetupService(IMeetupRepository meetupRepository, IUserRepository userRepository, IMeetupUserRepository meetupUserRepository,
                          MeetupMapper mapper, WeatherApiClient apiClient) {
          this.meetupRepository = meetupRepository;
          this.userRepository = userRepository;
          this.meetupUserRepository = meetupUserRepository;
          this.mapper = mapper;
          this.apiClient = apiClient;
     }

     public void create(MeetupDto meetupDto) throws MeetupAlreadyExistException, IOException, UserNotFoundException, MeetupErrorSavingException {
          LOGGER.info("Create/Update meetup with name: {} and date: {}", meetupDto.getName(), meetupDto.getDate());
          if(meetupDto.getId() == null || this.meetupRepository.findMeetupByDateAndName(meetupDto.getDate(), meetupDto.getName()).isPresent()) {
               LOGGER.error("Meetup already exist");
               throw new MeetupAlreadyExistException("Meetup already exist");
          }
          User user = this.findUser(meetupDto.getCreatorId());
          Meetup meetup = this.mapper.mapToModel(meetupDto, user, this.getTemp(meetupDto.getCity(), meetupDto.getDate()));
          LOGGER.info("Saving meetup");
          this.saveMeetup(meetup);
     }

     public void addUserToMeetup(Long meetupId, Long userId) throws UserNotFoundException, UserAssociatedException, MeetupErrorSavingException {
          Meetup meetup = this.findMeetup(meetupId);
          User user = this.findUser(userId);
          if(this.meetupUserRepository.findMeetupUserByUserAndMeetup(user, meetup).isPresent()) {
               LOGGER.error("User is already associated to this meetup");
               throw new UserAssociatedException("User is already associated to this meetup");
          }
          MeetupUser meetupUser = new MeetupUser(meetup, user, Boolean.FALSE);
          meetup.getMeetupUsers().add(meetupUser);
          this.saveMeetup(meetup);
     }

     public MeetupDto getMeetup(Long id) {
          Meetup meetup = this.findMeetup(id);
          return this.mapper.mapToDto(meetup);
     }

     public List<MeetupDto> getMeetups(Long userId) throws UserNotFoundException {
          User user = this.findUser(userId);
          List<Meetup> meetups = this.meetupRepository.findAllByUser(user);
          return this.mapper.mapToDto(meetups);
     }

     public int getTemperature(Long id) throws IOException {
          Meetup meetup = this.findMeetup(id);
          return this.getTemp(meetup.getCity(), meetup.getDate());
     }

     public MeetupInformationDto getTempAndBeerInformation(Long id) throws IOException, UserNotFoundException, UserWithNoPermissionException {
          Meetup meetup = this.findMeetup(id);
          User user = this.findUser(meetup.getUser().getId());
          if(!UserRole.ADMIN.equals(user.getRole())) {
               LOGGER.info("User with user role only");
               throw new UserWithNoPermissionException("Only ADMIN user have access");
          }
          int meetupTemp = this.getTemp(meetup.getCity(), meetup.getDate());
          if(meetupTemp == 0) {
               return this.mapper.mapMeetupInformation(0,meetupTemp, "Please wait a few days to know the temp");
          }
          return this.calculateBeers(meetup.getMeetupUsers().size(),meetupTemp);
     }

     public Meetup findMeetup(Long id) {
          LOGGER.info("Getting meetup with id: {}", id);
          return this.meetupRepository.findMeetupById(id).orElseThrow(() -> new MethodNotFoundException("Meetup not found"));
     }

     public void saveMeetup(Meetup meetup) throws MeetupErrorSavingException {
          try {
               this.meetupRepository.save(meetup);
          } catch(Exception e) {
               throw new MeetupErrorSavingException("Error saving meetup");
          }
     }

     private MeetupInformationDto calculateBeers(int enrolled, int temp) {
          if(temp > 24) {
               return this.mapper.mapMeetupInformation(this.calculateBoxes(2, enrolled),temp, "It will be hot, so buy some beers");
          } else if(temp < 20) {
               return this.mapper.mapMeetupInformation(this.calculateBoxes((int)(Double.parseDouble("0.75")), enrolled),temp, "It will be a little cold, try some drinks");
          }
          return this.mapper.mapMeetupInformation(this.calculateBoxes(1,enrolled),temp, "Let's try more than just one");
     }

     private int calculateBoxes(int beerPerPerson, int enrolled) {
          int beersBoxes;
          beersBoxes = ((beerPerPerson * enrolled)/ BEERS_BOX);
          return beersBoxes == 0 ? 1 : beersBoxes;
     }

     private int getTemp(String city, LocalDateTime date) throws IOException {
          WeatherResponse days = apiClient.getWeather(city);
          LOGGER.info("Response weather: {}", days);
          LocalDateTime today = LocalDateTime.now();
          long daysBetweenDates = today.until(date, ChronoUnit.DAYS);
          int temperature = 0;
          if(daysBetweenDates < days.getDays().size()) {
               DayTemperature day = IntStream.range(0, days.getDays().size())
                       .filter(i -> i == daysBetweenDates)
                       .mapToObj(i -> days.getDays().get(i))
                       .findFirst()
                       .get();
               return day.getMain().getTempMax();
          }
          return temperature;
     }

     private User findUser(Long userId) throws UserNotFoundException {
          LOGGER.info("Getting user with id: {}", userId);
          return this.userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
     }

}
