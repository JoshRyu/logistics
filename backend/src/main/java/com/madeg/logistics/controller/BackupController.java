package com.madeg.logistics.controller;

import com.madeg.logistics.domain.CommonRes;
import com.madeg.logistics.enums.ResponseCode;
import com.madeg.logistics.scheduler.DatabaseBackupScheduler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Backup")
@RestController
@RequestMapping(path = "/api/v1/backup")
public class BackupController {

    @Autowired
    private DatabaseBackupScheduler databaseBackupScheduler;

    @Operation(summary = "Restore Database")
    @ApiResponse(content = @Content(schema = @Schema(implementation = CommonRes.class)))
    @PostMapping
    public ResponseEntity<Object> restoreData(
            @RequestParam String fileName) {
        try {
            databaseBackupScheduler.restoreDatabase(fileName);
            return ResponseEntity
                    .status(ResponseCode.SUCCESS.getStatus())
                    .body(new CommonRes(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage("DB restore")));
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(new CommonRes(ex.getStatusCode().value(), ex.getReason()));
        }
    }

}
