package com.developers.live.mentoring.entity;

import com.developers.live.common.entity.BaseTime;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update mentoring_room set deleted_at = CURRENT_TIMESTAMP where mentoring_room_id = ?")
@Table(name = "mentoring_room")
@Entity
@DynamicUpdate
public class Room extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentoring_room_id", nullable = false)
    private Long mentoringRoomId;
    @Column(name = "mentor_id", nullable = false)
    private Long mentorId;
    @Column(name = "mentor_name", nullable = false)
    private String mentorName;
    @Column(name = "title", nullable = false, length = 50)
    private String title;
    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(mappedBy = "mentoringRoomId")
    private List<Schedule> schedules = new ArrayList<>();

    public void updateRoomInfo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Builder
    public Room(Long mentorId, String mentorName, String title, String description) {
        this.mentorId = mentorId;
        this.mentorName = mentorName;
        this.title = title;
        this.description = description;
    }
}
