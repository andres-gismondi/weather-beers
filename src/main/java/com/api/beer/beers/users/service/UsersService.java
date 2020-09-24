package com.api.beer.beers.users.service;

import com.api.beer.beers.meetup.dto.MeetupDto;
import com.api.beer.beers.meetup.mapper.MeetupMapper;
import com.api.beer.beers.meetup.model.Meetup;
import com.api.beer.beers.meetup.model.MeetupUser;
import com.api.beer.beers.meetup.repository.IMeetupUserRepository;
import com.api.beer.beers.meetup.service.MeetupService;
import com.api.beer.beers.users.dto.UserDto;
import com.api.beer.beers.users.dto.UserLogin;
import com.api.beer.beers.users.errors.UserAlreadyExistException;
import com.api.beer.beers.users.errors.UserNotFoundException;
import com.api.beer.beers.users.mapper.UserMapper;
import com.api.beer.beers.users.model.User;
import com.api.beer.beers.users.repository.IUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private static Logger LOGGER = LoggerFactory.getLogger(UsersService.class);

    private static final String SECRET = "1234aSedRtcGTaSIyf654fSd_@A"; //HARDCODED

    private IUserRepository userRepository;
    private UserMapper userMapper;
    private MeetupService meetupService;
    private MeetupMapper meetupMapper;
    private IMeetupUserRepository meetupUserRepository;

    @Autowired
    public UsersService(IUserRepository userRepository, UserMapper userMapper,
                        MeetupService meetupService, IMeetupUserRepository meetupUserRepository,
                        MeetupMapper meetupMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.meetupService = meetupService;
        this.meetupUserRepository = meetupUserRepository;
        this.meetupMapper = meetupMapper;
    }

    public void create(UserDto userDto) throws UserAlreadyExistException {
        if(userDto.getId() != null || this.userRepository.findBySurName(userDto.getSurName()).isPresent()) {
            LOGGER.error("User with id {} already exist", userDto.getId());
            throw new UserAlreadyExistException("User with id: "+userDto.getId()+" already exist");
        }
        User user = this.userMapper.mapToModel(userDto);
        this.userRepository.save(user);
    }

    public void checkinMeetup(Long userId, Long meetupId) throws UserNotFoundException {
        User user = this.findUser(userId);
        Meetup meetup = this.meetupService.findMeetup(meetupId);
        Optional<MeetupUser> opMeetupUser = this.meetupUserRepository.findMeetupUserByUserAndMeetup(user, meetup);
        if(!opMeetupUser.isPresent()) {
            LOGGER.error("User is not inscribed to this meetup");
            throw new UserNotFoundException("User is not inscribe to this meetup");
        }
        MeetupUser meetupUser = opMeetupUser.get();
        if(meetupUser.getCheckin()) {
            LOGGER.error("User already checkin");
            throw new UserNotFoundException("User already checkin");
        }
        meetupUser.setCheckin(Boolean.TRUE);
        this.meetupUserRepository.save(meetupUser);
    }

    public List<MeetupDto> getMeetups(Long id) throws UserNotFoundException {
        User user = this.findUser(id);
        List<Meetup> meetups = user.getMeetups();
        return this.meetupMapper.mapToDto(meetups);
    }

    @Transactional
    public List<MeetupDto> getMeetupsInscribed(Long id) throws UserNotFoundException {
        User user = this.findUser(id);
        List<MeetupUser> meetupUsers = user.getMeetupUsers();
        return meetupUsers.stream()
                .map(MeetupUser::getMeetup)
                .map(mp -> this.meetupMapper.mapToDto(mp))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MeetupDto> getCheckinMeetups(Long id) throws UserNotFoundException {
        User user = this.findUser(id);
        List<MeetupUser> meetupUsers = user.getMeetupUsers();
        return meetupUsers.stream()
                .filter(MeetupUser::getCheckin)
                .map(MeetupUser::getMeetup)
                .map(mp -> this.meetupMapper.mapToDto(mp))
                .collect(Collectors.toList());
    }

    @Cacheable("users")
    public UserDto getUser(Long id) throws UserNotFoundException {
        User user = this.findUser(id);
        return this.userMapper.mapToDto(user);
    }

    public String getJWTToken(UserLogin userLogin) throws UserNotFoundException {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        this.userRepository
                .findBySurNameAndPassword(userLogin.getUsername(), userLogin.getPassword())
                .orElseThrow(() -> new UserNotFoundException("User not exist"));

        String token = Jwts.builder()
                .setId("meetupJWT")
                .setSubject(userLogin.getUsername())
                .claim("authorities", grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();

        return "Bearer =" + token;
    }

    private User findUser(Long userId) throws UserNotFoundException {
        LOGGER.info("Getting user with id: {}", userId);
        return this.userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

}
