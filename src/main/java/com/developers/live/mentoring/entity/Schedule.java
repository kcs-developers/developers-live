package com.developers.live.mentoring.entity;

import com.developers.live.common.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update mentoring_room_schedule set deleted_at = CURRENT_TIMESTAMP where schedule_id = ?")
@Table(name = "mentoring_room_schedule")
@Entity
@DynamicUpdate
public class Schedule extends BaseTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "schedule_id", nullable = false)
  private Long scheduleId;
  @Column(name = "mentoring_room_id", nullable = false)
  private Long mentoringRoomId;
  @Column(name = "mentor_id", nullable = false)
  private Long mentorId;
  @Column(name = "mentor_name", nullable = false)
  private String mentorName;
  @Column(name = "mentee_id")
  private Long menteeId;
  @Column(name = "mentee_name")
  private String menteeName;
  @Column(name = "start", nullable = false)
  private LocalDateTime start;
  @Column(name = "end", nullable = false)
  private LocalDateTime end;

  public void changeMentee(Long menteeId, String menteeName) {
    this.menteeId = menteeId;
    this.menteeName = menteeName;
  }

  @Builder
  public Schedule(Long mentoringRoomId, Long mentorId, String mentorName, Long menteeId, String menteeName, LocalDateTime start, LocalDateTime end) {
    this.mentoringRoomId = mentoringRoomId;
    this.mentorId = mentorId;
    this.mentorName = mentorName;
    this.menteeName = menteeName;
    this.menteeId = menteeId;
    this.start = start;
    this.end = end;
  }
}
