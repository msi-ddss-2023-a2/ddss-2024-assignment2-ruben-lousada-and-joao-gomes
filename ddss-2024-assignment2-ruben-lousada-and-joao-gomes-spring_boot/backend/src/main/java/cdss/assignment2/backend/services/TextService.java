package cdss.assignment2.backend.services;

import cdss.assignment2.backend.dto.AccountCreationRequest;
import cdss.assignment2.backend.dto.TextCreationRequest;
import cdss.assignment2.backend.model.Account;
import cdss.assignment2.backend.model.Text;
import cdss.assignment2.backend.repository.TextRepository;
import cdss.assignment2.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TextService {

    private final TextRepository textRepository;

    public TextService(TextRepository textRepository) {
        this.textRepository = textRepository;
    }

    public Text createText(TextCreationRequest textCreationRequest) {
        Text text = new Text();
        text.setText(textCreationRequest.getText());
        return textRepository.save(text);
    }

    public List<Text> getAll() {
        return textRepository.findAll();
    }
}
