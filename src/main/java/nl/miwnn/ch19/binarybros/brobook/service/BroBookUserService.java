
/*
 * @author Mart Stukje
 * */

package nl.miwnn.ch19.binarybros.brobook.service;

import nl.miwnn.ch19.binarybros.brobook.dto.NewUserFormDTO;
import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Image;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import nl.miwnn.ch19.binarybros.brobook.repository.ImageRepository;
import nl.miwnn.ch19.binarybros.brobook.service.mapper.BroBookUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BroBookUserService implements UserDetailsService {

    private final BroBookUserRepository userRepository;
    private final ImageService imageService;
    private final BroBookUserMapper broBookUserMapper;

    public BroBookUserService(BroBookUserRepository userRepository,
                              ImageService imageService, BroBookUserMapper broBookUserMapper) {
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.broBookUserMapper = broBookUserMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Gebruiker niet gevonden: " + username));
    }

    public List<BroBookUser> findAll() {
        return userRepository.findAll();
    }

    public BroBookUser getUserById(Long id) {
        return userRepository.getReferenceById(id);
    }

    public void save(BroBookUser user, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            user.setProfilePicture(imageService.saveImage(imageFile));
        }
        userRepository.save(user);
    }

    public void saveNewUser(NewUserFormDTO dto) {
        BroBookUser newUser = broBookUserMapper.toBroBookUser(dto);
        userRepository.save(newUser);
    }
}