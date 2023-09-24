package faang.school.paymentservice.controller;

import faang.school.paymentservice.config.context.UserContext;
import faang.school.paymentservice.dto.PendingOperationDto;
import faang.school.paymentservice.service.PendingOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("pending/operations")
@Slf4j
@Validated
public class PendingOperationController {

    private final PendingOperationService pendingOperationService;
    private final UserContext userContext;

    @PostMapping
    public PendingOperationDto initOperation(@RequestBody PendingOperationDto pendingOperationDto) {
        Long userId = userContext.getUserId();
        log.info("Received request to create pending operation: {}, for user: {}", pendingOperationDto, userId);
        return pendingOperationService.initOperation(pendingOperationDto, userId);
    }
}
