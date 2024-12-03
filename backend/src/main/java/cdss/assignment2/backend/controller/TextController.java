package cdss.assignment2.backend.controller;

import cdss.assignment2.backend.dto.AccountCreationRequest;
import cdss.assignment2.backend.dto.TextCreationRequest;
import cdss.assignment2.backend.model.Account;
import cdss.assignment2.backend.model.Text;
import cdss.assignment2.backend.services.AccountService;
import cdss.assignment2.backend.services.TextService;
import cdss.assignment2.backend.utils.XssUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("text")
@RestController
public class TextController {

    public TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Text createText(@RequestBody TextCreationRequest textCreationRequest) {
        return this.textService.createText(textCreationRequest);
    }

    @PostMapping(
            path = "/safe",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Text createTextSafe(@RequestBody TextCreationRequest textCreationRequest) {
        textCreationRequest.setText(XssUtil.escapeHtml(textCreationRequest.getText()));
        return this.textService.createText(textCreationRequest);
    }

    @GetMapping
    public List<Text> getAll() {
        return textService.getAll();
    }
}
