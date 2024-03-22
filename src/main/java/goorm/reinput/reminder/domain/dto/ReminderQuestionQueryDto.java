package goorm.reinput.reminder.domain.dto;

import goorm.reinput.insight.domain.HashTag;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ReminderQuestionQueryDto {
    private String reminderQuestion;
    private LocalDateTime reminderUpdatedAt;
    private Long insightId;
    private Long reminderId;
    private String insightTitle;
    private String insightMainImage;
    private LocalDateTime lastRemindedAt;
    private List<String> insightTagList;

    @Builder
    public ReminderQuestionQueryDto(String reminderQuestion, Long insightId, Long reminderId, String insightTitle, String insightMainImage, List<String> insightTagList) {
        this.reminderQuestion = reminderQuestion;
        this.insightId = insightId;
        this.reminderId = reminderId;
        this.insightTitle = insightTitle;
        this.insightMainImage = insightMainImage;
        this.insightTagList = insightTagList;
    }


}
