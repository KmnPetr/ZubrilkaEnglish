package com.example.ZubrilkaEnglishServer.controllers;

import com.example.ZubrilkaEnglishServer.controllers.exeptions.MyValidationExeption;
import com.example.ZubrilkaEnglishServer.dto.WordDTO;
import com.example.ZubrilkaEnglishServer.models.Word;
import com.example.ZubrilkaEnglishServer.services.WordService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/words")
public class WordsController {
    private final WordService wordService;
    private final ModelMapper modelMapper;
    @Autowired
    public WordsController(WordService wordService, ModelMapper modelMapper) {
        this.wordService = wordService;
        this.modelMapper = modelMapper;
    }
    @GetMapping()
    public List<WordDTO>getWords(){
        return wordService.findAll().stream().map(this::convertToWordsDTO).collect(Collectors.toList());
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus>create(@RequestBody @Valid WordDTO wordDTO,
                                            BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String,String> validationErrors=new HashMap<>();
            List<FieldError> errors=bindingResult.getFieldErrors();
            for(FieldError error:errors){
                validationErrors.put(error.getField(), error.getDefaultMessage());
            }
            throw new MyValidationExeption("Validation Errors",validationErrors,HttpStatus.BAD_REQUEST);
        }
        wordService.save(convertToWord(wordDTO));

        return ResponseEntity.ok(HttpStatus.CREATED);//отправляем http с пустым телом и со статусом 200
    }
    /////////////////////////////////
    /////////////////////////////////
    /////////////////////////////////
    private WordDTO convertToWordsDTO(Word word){
        return modelMapper.map(word, WordDTO.class);
    }
    private Word convertToWord(WordDTO wordDTO){
        return modelMapper.map(wordDTO,Word.class);
    }
    //////////////////////////////////
    //////////////////////////////////
    //////////////////////////////////

}
