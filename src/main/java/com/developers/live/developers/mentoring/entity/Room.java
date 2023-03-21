package com.developers.live.developers.mentoring.entity;

import com.developers.live.developers.common.entity.BaseTime;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor
@ToString
@Getter
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update mentoring_room set deleted_at = CURRENT_TIMESTAMP where room_id = ?")
@Table(name = "mentoring_room")
@Entity
public class Room extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentoringRoomId", nullable = false)
    private Long roomId;
    @Column(name = "mentorId", nullable = false)
    private Long mentorId;
    @Column(name = "title", nullable = false, length = 50)
    private String title;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "point", nullable = false)
    private Long point;

    @Builder
    public Room(Long mentorId, String title, String description, Long point) {
        this.mentorId = mentorId;
        this.title = title;
        this.description = description;
        this.point = point;
    }
}
