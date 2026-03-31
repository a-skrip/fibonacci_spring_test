package ru.skriplex.springnewsapplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewsDto {
    private Long id;
    private String title;
    private String text;
    private Instant date;
    private String category;
}
