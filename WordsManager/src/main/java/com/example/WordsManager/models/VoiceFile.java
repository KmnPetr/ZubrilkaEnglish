package com.example.WordsManager.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "voice_files")
public class VoiceFile {
    @Id
    @Column("id")
    private int id;
    @Column("file_name")
    private String fileName;
    @Column("file_data")
    private byte[] fileData;

}
