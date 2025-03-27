package com.brigada.is.dto.response;

import com.brigada.is.domain.ImportStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImportHistoryResponseDTO {
    private Long id;
    private ImportStatus status;
    private Long userId;
    private Long objectsCount;
    private String fileName;
}
