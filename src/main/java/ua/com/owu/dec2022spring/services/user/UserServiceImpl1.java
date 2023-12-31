package ua.com.owu.dec2022spring.services.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.owu.dec2022spring.dao.UserDAO;
import ua.com.owu.dec2022spring.models.User;
import ua.com.owu.dec2022spring.models.dto.UserDTO;
import ua.com.owu.dec2022spring.services.mail.MailService;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service("one")
@AllArgsConstructor
public class UserServiceImpl1 implements UserService {

    private UserDAO userDAO;
    private MailService mailService;

    public void saveUser(UserDTO userDTO) {
        String name = userDTO.getUsername();
        User user = new User(name);
        if (user.getId() < 0) {
            throw new RuntimeException("id!!!!");
        }
        userDAO.save(user);

    }

    public ResponseEntity<User> getUserById(int id) {
        if (id < 0) {
            throw new RuntimeException();
        }
        return new ResponseEntity<>(userDAO.findById(id).get(), HttpStatusCode.valueOf(200));

    }

    public ResponseEntity<List<UserDTO>> getAllUsers() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("customHeader", "custom value");
        List<UserDTO> collect = userDAO.findAll()
                .stream().map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getAvatar())).collect(Collectors.toList());

        return new ResponseEntity<>(collect, httpHeaders, HttpStatus.ACCEPTED);
    }

    @Override
    public void save(User user) {
        userDAO.save(user);
        mailService.sendEmailToUser(user);
    }

    @Override
    public void save(User user, File file) {
        userDAO.save(user);
        mailService.sendEmailToUser(user,file);
    }


}
